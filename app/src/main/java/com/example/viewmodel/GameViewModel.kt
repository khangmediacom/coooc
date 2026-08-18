package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.local.entity.MatchHistoryEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.AIDifficulty
import com.example.data.model.AppLanguage
import com.example.data.model.BoardState
import com.example.data.model.BoardTheme
import com.example.data.model.ChatMessage
import com.example.data.model.GameMode
import com.example.data.model.GameStatus
import com.example.data.model.LeaderboardEntry
import com.example.data.model.Move
import com.example.data.model.PieceColor
import com.example.data.model.PieceStyle
import com.example.data.model.Position
import com.example.data.model.TacticsData
import com.example.data.model.TacticsLesson
import com.example.data.model.UserPreferences
import com.example.data.repository.ChessRepository
import com.example.engine.AudioHaptics
import com.example.engine.KhmerChessAI
import com.example.engine.KhmerChessRules
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class CurrentScreen {
    data object Loading : CurrentScreen()
    data object Welcome : CurrentScreen()
    data object Home : CurrentScreen()
    data class Game(val mode: GameMode, val difficulty: AIDifficulty = AIDifficulty.AMATEUR, val playerColor: PieceColor = PieceColor.WHITE) : CurrentScreen()
    data object Tactics : CurrentScreen()
    data class TacticsPlay(val lesson: TacticsLesson) : CurrentScreen()
    data object History : CurrentScreen()
    data class Replay(val match: MatchHistoryEntity) : CurrentScreen()
    data object Leaderboard : CurrentScreen()
    data object Customization : CurrentScreen()
    data object Play : CurrentScreen()
    data object OnlineLobby : CurrentScreen()
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChessRepository(AppDatabase.getDatabase(application))

    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfileEntity())

    val matchHistoryList: StateFlow<List<MatchHistoryEntity>> = repository.matchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lessonProgressList: StateFlow<List<LessonProgressEntity>> = repository.lessonProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow<CurrentScreen>(CurrentScreen.Loading)
    val currentScreen: StateFlow<CurrentScreen> = _currentScreen.asStateFlow()

    private val _boardState = MutableStateFlow(KhmerChessRules.createInitialBoard())
    val boardState: StateFlow<BoardState> = _boardState.asStateFlow()

    // Move History Snapshots & Interactive Review State
    private val _historySnapshots = MutableStateFlow<List<BoardState>>(listOf(KhmerChessRules.createInitialBoard()))
    val historySnapshots: StateFlow<List<BoardState>> = _historySnapshots.asStateFlow()

    private val _reviewMoveIndex = MutableStateFlow<Int?>(null)
    val reviewMoveIndex: StateFlow<Int?> = _reviewMoveIndex.asStateFlow()

    private val _selectedPos = MutableStateFlow<Position?>(null)
    val selectedPos: StateFlow<Position?> = _selectedPos.asStateFlow()

    private val _legalMoves = MutableStateFlow<List<Move>>(emptyList())
    val legalMoves: StateFlow<List<Move>> = _legalMoves.asStateFlow()

    private val _preferences = MutableStateFlow(UserPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _hintMove = MutableStateFlow<Move?>(null)
    val hintMove: StateFlow<Move?> = _hintMove.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _onlineRoomCode = MutableStateFlow("")
    val onlineRoomCode: StateFlow<String> = _onlineRoomCode.asStateFlow()

    private val _isOnlineSearching = MutableStateFlow(false)
    val isOnlineSearching: StateFlow<Boolean> = _isOnlineSearching.asStateFlow()

    // Active Match Config
    var activeGameMode: GameMode = GameMode.AI
    var activeDifficulty: AIDifficulty = AIDifficulty.AMATEUR
    var activePlayerColor: PieceColor = PieceColor.WHITE
    var activeLesson: TacticsLesson? = null

    // Replay State
    private val _replayMoves = MutableStateFlow<List<Move>>(emptyList())
    val replayMoves: StateFlow<List<Move>> = _replayMoves.asStateFlow()

    private val _replayCurrentIndex = MutableStateFlow(0)
    val replayCurrentIndex: StateFlow<Int> = _replayCurrentIndex.asStateFlow()

    private var timerJob: Job? = null
    private var gameStartTime: Long = 0L

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
        }
        viewModelScope.launch {
            repository.userPreferences.collect { pref ->
                _preferences.value = pref
                AudioHaptics.updatePreferences(
                    soundEnabled = pref.soundEnabled,
                    soundVol = pref.soundVolume,
                    musicEnabled = pref.musicEnabled,
                    musicVol = pref.musicVolume
                )
            }
        }
    }

    fun navigateTo(screen: CurrentScreen) {
        _currentScreen.value = screen
    }

    fun startAiGame(difficulty: AIDifficulty, playerColor: PieceColor) {
        activeGameMode = GameMode.AI
        activeDifficulty = difficulty
        activePlayerColor = playerColor
        activeLesson = null

        startNewMatch()
        _currentScreen.value = CurrentScreen.Game(GameMode.AI, difficulty, playerColor)

        if (playerColor == PieceColor.BLACK) {
            triggerAiMove()
        }
    }

    fun startLocal2PGame() {
        activeGameMode = GameMode.LOCAL_2P
        activePlayerColor = PieceColor.WHITE
        activeLesson = null

        startNewMatch()
        _currentScreen.value = CurrentScreen.Game(GameMode.LOCAL_2P)
    }

    fun startOnlineQuickMatch() {
        activeGameMode = GameMode.ONLINE_MATCH
        activePlayerColor = if (Random.nextBoolean()) PieceColor.WHITE else PieceColor.BLACK
        activeLesson = null

        _isOnlineSearching.value = true
        viewModelScope.launch {
            delay(1500) // Simulating fast matchmaking
            _isOnlineSearching.value = false
            _chatMessages.value = listOf(
                ChatMessage(
                    senderName = "Hệ thống",
                    message = "Đã kết nối đối thủ: Sovan Dara (Elo 1420)",
                    isSelf = false
                )
            )
            startNewMatch()
            _currentScreen.value = CurrentScreen.Game(GameMode.ONLINE_MATCH, AIDifficulty.MASTER, activePlayerColor)

            if (activePlayerColor == PieceColor.BLACK) {
                simulateOnlineOpponentMove()
            }
        }
    }

    fun startTacticsLesson(lesson: TacticsLesson) {
        activeGameMode = GameMode.TACTICS_LESSON
        activeLesson = lesson
        activePlayerColor = lesson.turn

        val customBoard = Array(8) { arrayOfNulls<com.example.data.model.Piece>(8) }
        lesson.fenPieces.forEach { (pos, type, color) ->
            customBoard[pos.row][pos.col] = com.example.data.model.Piece(
                type = type,
                color = color,
                id = "${lesson.id}_${color}_${type}_${pos.row}_${pos.col}"
            )
        }

        _boardState.value = BoardState(
            board = customBoard,
            currentTurn = lesson.turn,
            status = GameStatus.PLAYING
        )
        _selectedPos.value = null
        _legalMoves.value = emptyList()
        _hintMove.value = null

        _currentScreen.value = CurrentScreen.TacticsPlay(lesson)
    }

    private fun startNewMatch() {
        val initial = KhmerChessRules.createInitialBoard()
        _boardState.value = initial
        _historySnapshots.value = listOf(initial)
        _reviewMoveIndex.value = null
        _selectedPos.value = null
        _legalMoves.value = emptyList()
        _hintMove.value = null
        _chatMessages.value = emptyList()
        gameStartTime = System.currentTimeMillis()

        // Start Khmer background music if enabled
        if (_preferences.value.musicEnabled) {
            AudioHaptics.startBgm()
        }

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_boardState.value.status == GameStatus.PLAYING || _boardState.value.status == GameStatus.CHECK) {
                delay(1000)
                val current = _boardState.value
                if (current.currentTurn == PieceColor.WHITE) {
                    val newTime = (current.whiteTimeMillis - 1000).coerceAtLeast(0)
                    if (newTime == 0L) {
                        handleGameOver(GameStatus.TIMEOUT, PieceColor.BLACK)
                    } else {
                        _boardState.value = current.copy(whiteTimeMillis = newTime)
                    }
                } else {
                    val newTime = (current.blackTimeMillis - 1000).coerceAtLeast(0)
                    if (newTime == 0L) {
                        handleGameOver(GameStatus.TIMEOUT, PieceColor.WHITE)
                    } else {
                        _boardState.value = current.copy(blackTimeMillis = newTime)
                    }
                }
            }
        }
    }

    fun reviewMove(index: Int?) {
        val snapshots = _historySnapshots.value
        if (index == null || index >= snapshots.size - 1) {
            _reviewMoveIndex.value = null
        } else {
            _reviewMoveIndex.value = index.coerceIn(0, snapshots.size - 1)
        }
        _selectedPos.value = null
        _legalMoves.value = emptyList()
    }

    fun stepReview(delta: Int) {
        val snapshots = _historySnapshots.value
        val current = _reviewMoveIndex.value ?: (snapshots.size - 1)
        val next = (current + delta).coerceIn(0, snapshots.size - 1)
        reviewMove(if (next == snapshots.size - 1) null else next)
    }

    fun jumpToStart() {
        reviewMove(0)
    }

    fun jumpToLive() {
        reviewMove(null)
    }

    fun onSquareSelected(pos: Position) {
        // If reviewing, clicking a square will return to live view
        if (_reviewMoveIndex.value != null) {
            _reviewMoveIndex.value = null
            return
        }

        val currentState = _boardState.value
        if (currentState.status != GameStatus.PLAYING && currentState.status != GameStatus.CHECK) return
        if (_isAiThinking.value) return

        // In AI mode, restrict human clicks to their turn
        if (activeGameMode == GameMode.AI && currentState.currentTurn != activePlayerColor) return

        val clickedPiece = currentState.board[pos.row][pos.col]
        val currentSelected = _selectedPos.value

        if (currentSelected == null) {
            if (clickedPiece != null && clickedPiece.color == currentState.currentTurn) {
                _selectedPos.value = pos
                _legalMoves.value = KhmerChessRules.getLegalMoves(currentState, pos)
                AudioHaptics.triggerHaptic(getApplication())
            }
        } else {
            // Check if clicking on valid target move
            val matchingMove = _legalMoves.value.find { it.to == pos }
            if (matchingMove != null) {
                executeMove(matchingMove)
            } else if (clickedPiece != null && clickedPiece.color == currentState.currentTurn) {
                // Change selection to another friendly piece
                _selectedPos.value = pos
                _legalMoves.value = KhmerChessRules.getLegalMoves(currentState, pos)
                AudioHaptics.triggerHaptic(getApplication())
            } else {
                // Deselect
                _selectedPos.value = null
                _legalMoves.value = emptyList()
            }
        }
    }

    private fun executeMove(move: Move) {
        val prevState = _boardState.value
        val newState = KhmerChessRules.applyMove(prevState, move)
        _boardState.value = newState
        _historySnapshots.value = _historySnapshots.value + newState
        _reviewMoveIndex.value = null
        _selectedPos.value = null
        _legalMoves.value = emptyList()
        _hintMove.value = null

        // Audio & Haptic feedback
        when {
            move.isPromotion -> {
                AudioHaptics.playPromotionSound(getApplication(), _preferences.value.soundEnabled)
                AudioHaptics.triggerHaptic(getApplication(), isHeavy = true)
            }
            move.isKingLeap -> {
                AudioHaptics.playKingLeapSound(getApplication(), _preferences.value.soundEnabled)
                AudioHaptics.triggerHaptic(getApplication())
            }
            move.capturedPiece != null -> {
                AudioHaptics.playCaptureSound(getApplication(), _preferences.value.soundEnabled)
                AudioHaptics.triggerHaptic(getApplication(), isHeavy = true)
            }
            else -> {
                AudioHaptics.playMoveSound(getApplication(), _preferences.value.soundEnabled)
                AudioHaptics.triggerHaptic(getApplication())
            }
        }

        if (newState.inCheck) {
            AudioHaptics.playCheckSound(getApplication(), _preferences.value.soundEnabled)
        }

        // Handle Tactics verification
        if (activeGameMode == GameMode.TACTICS_LESSON && activeLesson != null) {
            val expected = activeLesson!!.expectedMoves.firstOrNull()
            if (expected != null && move.from == expected.first && move.to == expected.second) {
                viewModelScope.launch {
                    repository.saveLessonCompletion(activeLesson!!.id, stars = 3)
                    AudioHaptics.playVictorySound(getApplication(), _preferences.value.soundEnabled)
                }
            }
            return
        }

        // Handle Game Over
        if (newState.status == GameStatus.CHECKMATE || newState.status == GameStatus.DRAW_BY_COUNT || newState.status == GameStatus.STALEMATE) {
            handleGameOver(newState.status, newState.winner)
            return
        }

        // Trigger AI or simulated opponent
        if (activeGameMode == GameMode.AI && newState.currentTurn != activePlayerColor) {
            triggerAiMove()
        } else if (activeGameMode == GameMode.ONLINE_MATCH && newState.currentTurn != activePlayerColor) {
            simulateOnlineOpponentMove()
        }
    }

    private fun triggerAiMove() {
        _isAiThinking.value = true
        viewModelScope.launch {
            delay(Random.nextLong(400, 900)) // Human-like pause
            val bestMove = KhmerChessAI.getBestMove(_boardState.value, activeDifficulty, _boardState.value.currentTurn)
            _isAiThinking.value = false
            if (bestMove != null) {
                executeMove(bestMove)
            }
        }
    }

    private fun simulateOnlineOpponentMove() {
        _isAiThinking.value = true
        viewModelScope.launch {
            delay(Random.nextLong(800, 1800))
            val bestMove = KhmerChessAI.getBestMove(_boardState.value, AIDifficulty.MASTER, _boardState.value.currentTurn)
            _isAiThinking.value = false
            if (bestMove != null) {
                executeMove(bestMove)
            }
        }
    }

    private fun handleGameOver(status: GameStatus, winner: PieceColor?) {
        timerJob?.cancel()
        _boardState.value = _boardState.value.copy(status = status, winner = winner)

        val duration = (System.currentTimeMillis() - gameStartTime) / 1000
        val isWin = winner == activePlayerColor
        val eloChange = if (isWin) 25 else if (winner == null) 0 else -18

        if (isWin) {
            AudioHaptics.playVictorySound(getApplication(), _preferences.value.soundEnabled)
        } else if (winner != null) {
            AudioHaptics.playDefeatSound(getApplication(), _preferences.value.soundEnabled)
        }

        // Record match in Room Database
        viewModelScope.launch {
            val movesNotations = _boardState.value.moveHistory.joinToString(",") { it.notation }
            val movesDetailed = _boardState.value.moveHistory.joinToString(";") {
                "${it.from.row},${it.from.col},${it.to.row},${it.to.col},${it.piece.type.name},${it.piece.color.name}"
            }

            repository.recordMatch(
                gameMode = activeGameMode.name,
                opponentName = if (activeGameMode == GameMode.AI) "Máy (${activeDifficulty.titleVi})" else "Kỳ thủ Trực tuyến",
                aiDifficulty = if (activeGameMode == GameMode.AI) activeDifficulty.name else null,
                playerColor = activePlayerColor.name,
                winner = winner?.name ?: "DRAW",
                endReason = status.name,
                totalMoves = _boardState.value.moveCount,
                movesNotation = movesNotations,
                movesDetailed = movesDetailed,
                durationSeconds = duration,
                eloChange = eloChange
            )
        }
    }

    fun requestHint() {
        viewModelScope.launch {
            _isAiThinking.value = true
            val hint = KhmerChessAI.getBestMove(_boardState.value, AIDifficulty.MASTER, _boardState.value.currentTurn)
            _isAiThinking.value = false
            _hintMove.value = hint
            if (hint != null) {
                _selectedPos.value = hint.from
                _legalMoves.value = KhmerChessRules.getLegalMoves(_boardState.value, hint.from)
                AudioHaptics.triggerHaptic(getApplication())
            }
        }
    }

    fun undoMove() {
        val history = _boardState.value.moveHistory
        if (history.isEmpty()) return

        // If playing vs AI, undo 2 moves (AI move + human move)
        val steps = if (activeGameMode == GameMode.AI && history.size >= 2) 2 else 1
        var replayState = KhmerChessRules.createInitialBoard()
        val newSnapshots = mutableListOf<BoardState>(replayState)

        for (i in 0 until (history.size - steps)) {
            replayState = KhmerChessRules.applyMove(replayState, history[i], checkLegality = false)
            newSnapshots.add(replayState)
        }

        _boardState.value = replayState
        _historySnapshots.value = newSnapshots
        _reviewMoveIndex.value = null
        _selectedPos.value = null
        _legalMoves.value = emptyList()
        _hintMove.value = null
        AudioHaptics.triggerHaptic(getApplication())
    }

    fun resignGame() {
        val winner = activePlayerColor.opposite()
        handleGameOver(GameStatus.RESIGNED, winner)
    }

    fun offerDraw() {
        handleGameOver(GameStatus.DRAW_BY_COUNT, null)
    }

    fun sendChatMessage(msg: String, isEmote: Boolean = false) {
        val user = userProfile.value.username
        val newMsg = ChatMessage(
            senderName = user,
            message = msg,
            isSelf = true,
            isEmote = isEmote
        )
        _chatMessages.value = _chatMessages.value + newMsg

        // Opponent auto-reply simulation in online mode
        if (activeGameMode == GameMode.ONLINE_MATCH) {
            viewModelScope.launch {
                delay(1200)
                val replies = listOf("Nước cờ hay đấy!", "Cảm ơn bạn!", "Tập trung nào!", "Trận đấu rất cân bằng.", "Chúc may mắn!")
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    senderName = "Sovan Dara",
                    message = replies.random(),
                    isSelf = false
                )
            }
        }
    }

    private val _showSignInDialog = MutableStateFlow(false)
    val showSignInDialog: StateFlow<Boolean> = _showSignInDialog.asStateFlow()

    fun openSignInDialog() {
        _showSignInDialog.value = true
    }

    fun closeSignInDialog() {
        _showSignInDialog.value = false
    }

    fun signInWithGoogle(email: String, displayName: String) {
        viewModelScope.launch {
            repository.signInWithGoogle(email, displayName)
            _showSignInDialog.value = false
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }

    fun updateBoardTheme(theme: BoardTheme) {
        _preferences.value = _preferences.value.copy(boardTheme = theme)
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun updatePieceStyle(style: PieceStyle) {
        _preferences.value = _preferences.value.copy(pieceStyle = style)
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun updateLanguage(lang: AppLanguage) {
        _preferences.value = _preferences.value.copy(language = lang)
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(isDarkMode = enabled)
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun toggleSound(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(soundEnabled = enabled)
        AudioHaptics.updatePreferences(
            soundEnabled = enabled,
            soundVol = _preferences.value.soundVolume,
            musicEnabled = _preferences.value.musicEnabled,
            musicVol = _preferences.value.musicVolume
        )
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun updateSoundVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _preferences.value = _preferences.value.copy(soundVolume = clamped)
        AudioHaptics.updatePreferences(
            soundEnabled = _preferences.value.soundEnabled,
            soundVol = clamped,
            musicEnabled = _preferences.value.musicEnabled,
            musicVol = _preferences.value.musicVolume
        )
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun toggleMusic(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(musicEnabled = enabled)
        AudioHaptics.updatePreferences(
            soundEnabled = _preferences.value.soundEnabled,
            soundVol = _preferences.value.soundVolume,
            musicEnabled = enabled,
            musicVol = _preferences.value.musicVolume
        )
        if (enabled) {
            AudioHaptics.startBgm()
        } else {
            AudioHaptics.stopBgm()
        }
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    
    fun updateMusicTrack(track: com.example.data.model.MusicTrack) {
        _preferences.value = _preferences.value.copy(musicTrack = track)
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun updateMusicVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _preferences.value = _preferences.value.copy(musicVolume = clamped)
        AudioHaptics.updatePreferences(
            soundEnabled = _preferences.value.soundEnabled,
            soundVol = _preferences.value.soundVolume,
            musicEnabled = _preferences.value.musicEnabled,
            musicVol = clamped
        )
        viewModelScope.launch { repository.updatePreferences(_preferences.value) }
    }

    fun testSoundEffect(type: String = "move") {
        when (type) {
            "move" -> AudioHaptics.playMoveSound(getApplication(), true)
            "capture" -> AudioHaptics.playCaptureSound(getApplication(), true)
            "check" -> AudioHaptics.playCheckSound(getApplication(), true)
            "victory" -> AudioHaptics.playVictorySound(getApplication(), true)
            "defeat" -> AudioHaptics.playDefeatSound(getApplication(), true)
        }
    }

    fun updateUsername(name: String) {
        viewModelScope.launch { repository.updateUsername(name) }
    }

    fun getLeaderboard(): List<LeaderboardEntry> {
        return repository.getGlobalLeaderboard(userProfile.value)
    }

    // Replay Player Setup
    fun loadMatchForReplay(match: MatchHistoryEntity) {
        val parsedMoves = mutableListOf<Move>()
        if (match.movesDetailed.isNotBlank()) {
            val tokens = match.movesDetailed.split(";")
            for (token in tokens) {
                val parts = token.split(",")
                if (parts.size >= 6) {
                    val from = Position(parts[0].toInt(), parts[1].toInt())
                    val to = Position(parts[2].toInt(), parts[3].toInt())
                    val type = com.example.data.model.PieceType.valueOf(parts[4])
                    val color = com.example.data.model.PieceColor.valueOf(parts[5])
                    parsedMoves.add(Move(from, to, com.example.data.model.Piece(type, color)))
                }
            }
        }
        _replayMoves.value = parsedMoves
        _replayCurrentIndex.value = 0
        _boardState.value = KhmerChessRules.createInitialBoard()
        _currentScreen.value = CurrentScreen.Replay(match)
    }

    fun setReplayIndex(targetIndex: Int) {
        val moves = _replayMoves.value
        val clampedIndex = targetIndex.coerceIn(0, moves.size)
        _replayCurrentIndex.value = clampedIndex

        var state = KhmerChessRules.createInitialBoard()
        for (i in 0 until clampedIndex) {
            state = KhmerChessRules.applyMove(state, moves[i], checkLegality = false)
        }
        _boardState.value = state
    }
}
