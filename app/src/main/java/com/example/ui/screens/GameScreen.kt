package com.example.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.AIDifficulty
import com.example.data.model.AppLanguage
import com.example.data.model.BoardState
import com.example.data.model.BoardTheme
import com.example.data.model.ChatMessage
import com.example.data.model.GameMode
import com.example.data.model.GameStatus
import com.example.data.model.Move
import com.example.data.model.Piece
import com.example.data.model.PieceColor
import com.example.data.model.PieceStyle
import com.example.data.model.Position
import com.example.data.model.UserPreferences
import com.example.ui.components.AngkorWarmBackground
import com.example.ui.components.ChessBoardView
import com.example.ui.components.GameOverDialog
import com.example.ui.components.InGameAudioDialog
import com.example.ui.components.InGameThemeSettingsDialog
import com.example.ui.components.LiveChatDialog
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.JadeEmerald

/**
 * Modern Responsive Khmer Chess Game Screen.
 * Designed for optimal UX:
 * - Portrait: Board fills the width with clean player cards and slim controls (no unnecessary scrolling).
 * - Landscape: Massive 8x8 Board placed in the DEAD CENTER, flanked by compact sidebars for players and controls.
 */
@Composable
fun GameScreen(
    boardState: BoardState,
    historySnapshots: List<BoardState> = emptyList(),
    reviewMoveIndex: Int? = null,
    selectedPos: Position?,
    legalMoves: List<Move>,
    preferences: UserPreferences,
    userProfile: UserProfileEntity,
    gameMode: GameMode,
    difficulty: AIDifficulty,
    playerColor: PieceColor,
    isAiThinking: Boolean,
    chatMessages: List<ChatMessage>,
    onSquareClick: (Position) -> Unit,
    onSelectMoveIndex: (Int?) -> Unit = {},
    onStepMove: (Int) -> Unit = {},
    onJumpToStart: () -> Unit = {},
    onJumpToLive: () -> Unit = {},
    onToggleSound: (Boolean) -> Unit = {},
    onUpdateSoundVolume: (Float) -> Unit = {},
    onToggleMusic: (Boolean) -> Unit = {},
    onUpdateMusicVolume: (Float) -> Unit = {},
    onSelectTheme: (BoardTheme) -> Unit = {},
    onSelectPieceStyle: (PieceStyle) -> Unit = {},
    onUndo: () -> Unit,
    onHint: () -> Unit,
    onResign: () -> Unit,
    onDrawOffer: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSendEmote: (String) -> Unit,
    onRematch: () -> Unit,
    onReplay: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val language = preferences.language
    var isBoardFlipped by remember { mutableStateOf(playerColor == PieceColor.BLACK) }
    var showResignConfirm by remember { mutableStateOf(false) }
    var showChatDialog by remember { mutableStateOf(false) }
    var showAudioDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    val opponentColor = playerColor.opposite()
    val opponentName = when (gameMode) {
        GameMode.AI -> "${difficulty.titleVi} AI"
        GameMode.LOCAL_2P -> "Người chơi 2"
        GameMode.ONLINE_MATCH -> "Sothea"
        GameMode.TACTICS_LESSON -> "Thử thách thế cờ"
    }
    val opponentTitle = when (gameMode) {
        GameMode.AI -> "${difficulty.elo} Elo"
        GameMode.LOCAL_2P -> "Local"
        GameMode.ONLINE_MATCH -> "1480 Elo"
        GameMode.TACTICS_LESSON -> "Puzzle"
    }

    val modeLabel = when (gameMode) {
        GameMode.AI -> AppStrings.get(language, "play_vs_ai")
        GameMode.LOCAL_2P -> AppStrings.get(language, "local_2p")
        GameMode.ONLINE_MATCH -> AppStrings.get(language, "online_match")
        GameMode.TACTICS_LESSON -> AppStrings.get(language, "tactics_puzzles")
    }

    val isGameOver = boardState.status in listOf(
        GameStatus.CHECKMATE,
        GameStatus.DRAW_BY_COUNT,
        GameStatus.STALEMATE,
        GameStatus.RESIGNED,
        GameStatus.TIMEOUT
    )

    val displayBoard = if (reviewMoveIndex != null && reviewMoveIndex in historySnapshots.indices) {
        historySnapshots[reviewMoveIndex]
    } else {
        boardState
    }

    val topPlayerColor = if (isBoardFlipped) playerColor else opponentColor
    val topPlayerName = if (isBoardFlipped) userProfile.username else opponentName
    val topPlayerTitle = if (isBoardFlipped) "${userProfile.elo} Elo" else opponentTitle
    val topPlayerTime = if (topPlayerColor == PieceColor.WHITE) boardState.whiteTimeMillis else boardState.blackTimeMillis
    val topCaptured = if (topPlayerColor == PieceColor.WHITE) boardState.capturedByWhite else boardState.capturedByBlack

    val bottomPlayerColor = if (isBoardFlipped) opponentColor else playerColor
    val bottomPlayerName = if (isBoardFlipped) opponentName else userProfile.username
    val bottomPlayerTitle = if (isBoardFlipped) opponentTitle else "${userProfile.elo} Elo"
    val bottomPlayerTime = if (bottomPlayerColor == PieceColor.WHITE) boardState.whiteTimeMillis else boardState.blackTimeMillis
    val bottomCaptured = if (bottomPlayerColor == PieceColor.WHITE) boardState.capturedByWhite else boardState.capturedByBlack

    AngkorWarmBackground(modifier = modifier) {
        if (isLandscape) {
            // ==========================================
            // LANDSCAPE 3-COLUMN RESPONSIVE LAYOUT
            // Left: Opponent & Quick Actions | Center: BIG CHESSBOARD | Right: Player & In-Game Actions
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT COLUMN: Opponent Info & System Navigation
                val leftScroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.75f)
                        .verticalScroll(leftScroll)
                        .padding(end = 6.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // System header buttons (Exit, Flip, Theme, Sound)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SmallHeaderIconButton(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            desc = "Back",
                            tag = "game_back_home_btn",
                            onClick = onHome
                        )
                        SmallHeaderIconButton(
                            icon = Icons.Default.Palette,
                            desc = "Theme",
                            tag = "theme_settings_btn",
                            tint = Color(0xFFD97706),
                            onClick = { showThemeDialog = true }
                        )
                        SmallHeaderIconButton(
                            icon = if (preferences.musicEnabled || preferences.soundEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                            desc = "Audio",
                            tag = "audio_settings_btn",
                            tint = if (preferences.musicEnabled || preferences.soundEnabled) JadeEmerald else Color(0xFF94A3B8),
                            onClick = { showAudioDialog = true }
                        )
                        SmallHeaderIconButton(
                            icon = Icons.Default.Refresh,
                            desc = "Flip",
                            tag = "flip_board_btn",
                            tint = Color(0xFFD97706),
                            onClick = { isBoardFlipped = !isBoardFlipped }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Opponent compact badge
                    CompactPlayerCard(
                        name = topPlayerName,
                        title = topPlayerTitle,
                        color = topPlayerColor,
                        isCurrentTurn = boardState.currentTurn == topPlayerColor,
                        timeMillis = topPlayerTime,
                        capturedPieces = topCaptured,
                        isThinking = isAiThinking && (topPlayerColor == opponentColor)
                    )

                    // Counting Rule / In Check Alert
                    if (boardState.inCheck) {
                        InCheckBanner(language = language)
                    } else if (boardState.countingRule.isCountingActive) {
                        CompactCountingBanner(boardState = boardState, language = language)
                    }
                }

                // CENTER COLUMN: MASSIVE CHESSBOARD IN DEAD CENTER
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ChessBoardView(
                        boardState = displayBoard,
                        selectedPos = if (reviewMoveIndex == null) selectedPos else null,
                        legalMoves = if (reviewMoveIndex == null) legalMoves else emptyList(),
                        boardTheme = preferences.boardTheme,
                        pieceStyle = preferences.pieceStyle,
                        showCoordinates = preferences.showCoordinates,
                        isFlipped = isBoardFlipped,
                        onSquareClick = onSquareClick,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                    )
                }

                // RIGHT COLUMN: Player Info & Action Buttons
                val rightScroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.75f)
                        .verticalScroll(rightScroll)
                        .padding(start = 6.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Game mode title & Turn status
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFFFFF).copy(alpha = 0.85f))
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = modeLabel,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val isMyTurn = boardState.currentTurn == playerColor
                        Text(
                            text = if (isMyTurn) AppStrings.get(language, "your_turn") else if (isAiThinking) AppStrings.get(language, "ai_thinking") else AppStrings.get(language, "opponent_turn"),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isMyTurn) Color(0xFF16A34A) else Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Player compact badge
                    CompactPlayerCard(
                        name = bottomPlayerName,
                        title = bottomPlayerTitle,
                        color = bottomPlayerColor,
                        isCurrentTurn = boardState.currentTurn == bottomPlayerColor,
                        timeMillis = bottomPlayerTime,
                        capturedPieces = bottomCaptured,
                        isThinking = false
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Action Controls toolbar
                    LandscapeActionButtons(
                        boardState = boardState,
                        isAiThinking = isAiThinking,
                        isGameOver = isGameOver,
                        language = language,
                        onUndo = onUndo,
                        onHint = onHint,
                        onOpenChat = { showChatDialog = true },
                        onDrawOffer = onDrawOffer,
                        onResign = { showResignConfirm = true }
                    )
                }
            }
        } else {
            // ==========================================
            // PORTRAIT SINGLE-SCREEN CLEAN IMMERSIVE LAYOUT
            // Full Width Big Board, Compact Avatar Cards (No Cutoff)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Ultra-clean Top Header Bar
                GameHeaderBar(
                    modeLabel = modeLabel,
                    boardState = boardState,
                    playerColor = playerColor,
                    isAiThinking = isAiThinking,
                    language = language,
                    preferences = preferences,
                    onHome = onHome,
                    onOpenTheme = { showThemeDialog = true },
                    onOpenAudio = { showAudioDialog = true },
                    onFlipBoard = { isBoardFlipped = !isBoardFlipped }
                )

                // 2. Opponent Player Banner (ZingPlay style)
                ZingPlayPlayerBanner(
                    name = topPlayerName,
                    title = topPlayerTitle,
                    color = topPlayerColor,
                    isCurrentTurn = boardState.currentTurn == topPlayerColor,
                    timeMillis = topPlayerTime,
                    capturedPieces = topCaptured,
                    isThinking = isAiThinking && (topPlayerColor == opponentColor),
                    isTop = true
                )

                // 3. Counting Rule Active Banner (if active)
                if (boardState.countingRule.isCountingActive) {
                    CompactCountingBanner(boardState = boardState, language = language)
                }

                // 4. MAXIMIZED 8X8 CHESSBOARD (FULL WIDTH)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ChessBoardView(
                        boardState = displayBoard,
                        selectedPos = if (reviewMoveIndex == null) selectedPos else null,
                        legalMoves = if (reviewMoveIndex == null) legalMoves else emptyList(),
                        boardTheme = preferences.boardTheme,
                        pieceStyle = preferences.pieceStyle,
                        showCoordinates = preferences.showCoordinates,
                        isFlipped = isBoardFlipped,
                        onSquareClick = onSquareClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

                // 5. User Player Banner (ZingPlay style)
                ZingPlayPlayerBanner(
                    name = bottomPlayerName,
                    title = bottomPlayerTitle,
                    color = bottomPlayerColor,
                    isCurrentTurn = boardState.currentTurn == bottomPlayerColor,
                    timeMillis = bottomPlayerTime,
                    capturedPieces = bottomCaptured,
                    isThinking = false,
                    isTop = false
                )

                // 6. Action Controls Toolbar (Bottom Circular Buttons)
                PortraitCircularActionToolbar(
                    boardState = boardState,
                    isAiThinking = isAiThinking,
                    isGameOver = isGameOver,
                    language = language,
                    onUndo = onUndo,
                    onHint = onHint,
                    onOpenChat = { showChatDialog = true },
                    onDrawOffer = onDrawOffer,
                    onResign = { showResignConfirm = true }
                )
            }
        }
    }

    // THEME SETTINGS DIALOG
    if (showThemeDialog) {
        InGameThemeSettingsDialog(
            preferences = preferences,
            language = language,
            onDismiss = { showThemeDialog = false },
            onSelectTheme = onSelectTheme,
            onSelectPieceStyle = onSelectPieceStyle
        )
    }

    // AUDIO SETTINGS DIALOG
    if (showAudioDialog) {
        InGameAudioDialog(
            preferences = preferences,
            language = language,
            onDismiss = { showAudioDialog = false },
            onToggleSound = onToggleSound,
            onUpdateSoundVolume = onUpdateSoundVolume,
            onToggleMusic = onToggleMusic,
            onUpdateMusicVolume = onUpdateMusicVolume
        )
    }

    // GAME OVER MODAL DIALOG
    if (isGameOver) {
        val winner = when (boardState.status) {
            GameStatus.CHECKMATE -> boardState.currentTurn.opposite()
            GameStatus.RESIGNED -> boardState.currentTurn.opposite()
            GameStatus.TIMEOUT -> boardState.currentTurn.opposite()
            else -> null
        }
        val isWin = winner == playerColor
        val eloDiff = if (isWin) +15 else if (winner == null) 0 else -12

        GameOverDialog(
            status = boardState.status,
            winner = winner,
            playerColor = playerColor,
            language = language,
            eloChange = if (gameMode == GameMode.ONLINE_MATCH || gameMode == GameMode.AI) eloDiff else 0,
            onRematch = onRematch,
            onReplay = onReplay,
            onHome = onHome
        )
    }

    // LIVE CHAT DIALOG
    if (showChatDialog) {
        LiveChatDialog(
            messages = chatMessages,
            onSendMessage = onSendMessage,
            onSendEmote = onSendEmote,
            onDismiss = { showChatDialog = false }
        )
    }

    // RESIGN CONFIRMATION DIALOG
    if (showResignConfirm) {
        AlertDialog(
            onDismissRequest = { showResignConfirm = false },
            title = { Text(text = AppStrings.get(language, "resign") + "?", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)) },
            text = { Text(text = "Bạn có chắc chắn muốn xin thua ván cờ này?", color = Color(0xFF64748B)) },
            confirmButton = {
                Button(
                    onClick = {
                        showResignConfirm = false
                        onResign()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(AppStrings.get(language, "resign"), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResignConfirm = false }) {
                    Text(AppStrings.get(language, "cancel"), color = Color(0xFF64748B))
                }
            },
            containerColor = Color(0xFFFFFDF8),
            shape = RoundedCornerShape(18.dp)
        )
    }
}

/**
 * ZingPlay-inspired Player Banner: Clean, avatar with gold badge, clear countdown clock.
 */
@Composable
private fun ZingPlayPlayerBanner(
    name: String,
    title: String,
    color: PieceColor,
    isCurrentTurn: Boolean,
    timeMillis: Long,
    capturedPieces: List<Piece>,
    isThinking: Boolean = false,
    isTop: Boolean = false,
    modifier: Modifier = Modifier
) {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val isWhite = color == PieceColor.WHITE

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Avatar + Name + Elo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isWhite) Brush.linearGradient(listOf(Color(0xFFFFFBEB), Color(0xFFE2C474)))
                        else Brush.linearGradient(listOf(Color(0xFF991B1B), Color(0xFF450A0A)))
                    )
                    .border(2.dp, if (isCurrentTurn) AngkorGold else Color(0xFFD4AF37).copy(alpha = 0.5f), CircleShape)
                    .shadow(if (isCurrentTurn) 4.dp else 0.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isWhite) "♔" else "♚",
                    fontSize = 19.sp,
                    color = if (isWhite) Color(0xFF1E293B) else Color(0xFFFFFFFF)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isThinking) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Thinking...",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD97706)
                        )
                    }
                }
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Right: Captured pieces & Glowing Timer
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (capturedPieces.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy((-4).dp)
                ) {
                    capturedPieces.takeLast(4).forEach { p ->
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFAF5EC))
                                .border(0.5.dp, Color(0xFFD4AF37), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (p.color) {
                                    PieceColor.WHITE -> "♙"
                                    PieceColor.BLACK -> "♟"
                                },
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Digital Timer Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isCurrentTurn) Color(0xFFFEF3C7) else Color(0xFFF1F5F9))
                    .border(
                        1.dp,
                        if (isCurrentTurn) Color(0xFFD97706) else Color(0xFFCBD5E1),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "⏱ $timeFormatted",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentTurn) Color(0xFF92400E) else Color(0xFF475569)
                )
            }
        }
    }
}

/**
 * Compact Player Card for Landscape Sidebars
 */
@Composable
private fun CompactPlayerCard(
    name: String,
    title: String,
    color: PieceColor,
    isCurrentTurn: Boolean,
    timeMillis: Long,
    capturedPieces: List<Piece>,
    isThinking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val isWhite = color == PieceColor.WHITE

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                if (isCurrentTurn) AngkorGold else Color(0xFFE8DCB8),
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentTurn) Color(0xFFFFFBEB) else Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isWhite) Brush.linearGradient(listOf(Color(0xFFFFFBEB), Color(0xFFE2C474)))
                        else Brush.linearGradient(listOf(Color(0xFF991B1B), Color(0xFF450A0A)))
                    )
                    .border(1.5.dp, if (isCurrentTurn) AngkorGold else Color(0xFFD4AF37), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isWhite) "♔" else "♚",
                    fontSize = 18.sp,
                    color = if (isWhite) Color(0xFF1E293B) else Color(0xFFFFFFFF)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )

            if (isThinking) {
                Text(
                    text = "Thinking...",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Timer
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isCurrentTurn) Color(0xFFFEF3C7) else Color(0xFFF1F5F9))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = timeFormatted,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentTurn) Color(0xFF92400E) else Color(0xFF475569)
                )
            }
        }
    }
}

@Composable
private fun GameHeaderBar(
    modeLabel: String,
    boardState: BoardState,
    playerColor: PieceColor,
    isAiThinking: Boolean,
    language: AppLanguage,
    preferences: UserPreferences,
    onHome: () -> Unit,
    onOpenTheme: () -> Unit,
    onOpenAudio: () -> Unit,
    onFlipBoard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exit Game Button
        IconButton(
            onClick = onHome,
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFFFFF))
                .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(10.dp))
                .testTag("game_back_home_btn")
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(18.dp)
            )
        }

        // Game Status & Turn / Check Indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = modeLabel,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            if (boardState.inCheck) {
                Text(
                    text = "⚠ " + AppStrings.get(language, "check"),
                    fontWeight = FontWeight.Black,
                    fontSize = 10.5.sp,
                    color = Color(0xFFDC2626)
                )
            } else {
                val isMyTurn = boardState.currentTurn == playerColor
                Text(
                    text = if (isMyTurn) AppStrings.get(language, "your_turn") else if (isAiThinking) AppStrings.get(language, "ai_thinking") else AppStrings.get(language, "opponent_turn"),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isMyTurn) Color(0xFF16A34A) else Color(0xFF64748B)
                )
            }
        }

        // Right Actions: Palette, Audio, Flip
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SmallHeaderIconButton(
                icon = Icons.Default.Palette,
                desc = "Theme",
                tag = "theme_settings_btn",
                tint = Color(0xFFD97706),
                onClick = onOpenTheme
            )
            SmallHeaderIconButton(
                icon = if (preferences.musicEnabled || preferences.soundEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                desc = "Audio",
                tag = "audio_settings_btn",
                tint = if (preferences.musicEnabled || preferences.soundEnabled) JadeEmerald else Color(0xFF94A3B8),
                onClick = onOpenAudio
            )
            SmallHeaderIconButton(
                icon = Icons.Default.Refresh,
                desc = "Flip",
                tag = "flip_board_btn",
                tint = Color(0xFFD97706),
                onClick = onFlipBoard
            )
        }
    }
}

@Composable
private fun SmallHeaderIconButton(
    icon: ImageVector,
    desc: String,
    tag: String,
    tint: Color = Color(0xFF1E293B),
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(10.dp))
            .testTag(tag)
    ) {
        Icon(
            icon,
            contentDescription = desc,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun InCheckBanner(language: AppLanguage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFFEE2E2))
            .border(1.dp, Color(0xFFDC2626), RoundedCornerShape(6.dp))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "⚠ " + AppStrings.get(language, "check"),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDC2626)
        )
    }
}

@Composable
private fun CompactCountingBanner(
    boardState: BoardState,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFD97706), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⏳ " + AppStrings.get(language, "counting_active"),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
            )
            Text(
                text = "${boardState.countingRule.currentMovesLeft} " + AppStrings.get(language, "moves_remaining"),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFB45309)
            )
        }
    }
}

@Composable
private fun PortraitCircularActionToolbar(
    boardState: BoardState,
    isAiThinking: Boolean,
    isGameOver: Boolean,
    language: AppLanguage,
    onUndo: () -> Unit,
    onHint: () -> Unit,
    onOpenChat: () -> Unit,
    onDrawOffer: () -> Unit,
    onResign: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFFFFF).copy(alpha = 0.95f))
            .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(12.dp))
            .padding(horizontal = 4.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularActionButton(
            icon = Icons.Default.Undo,
            label = AppStrings.get(language, "undo"),
            color = Color(0xFF475569),
            enabled = boardState.moveHistory.isNotEmpty() && !isAiThinking,
            tag = "undo_move_btn",
            onClick = onUndo
        )

        CircularActionButton(
            icon = Icons.Default.Lightbulb,
            label = AppStrings.get(language, "hint"),
            color = Color(0xFFD97706),
            enabled = !isAiThinking && !isGameOver,
            tag = "hint_btn",
            onClick = onHint
        )

        CircularActionButton(
            icon = Icons.Default.Chat,
            label = AppStrings.get(language, "chat"),
            color = Color(0xFF0284C7),
            enabled = !isGameOver,
            tag = "chat_btn",
            onClick = onOpenChat
        )

        CircularActionButton(
            icon = Icons.Default.Handshake,
            label = AppStrings.get(language, "draw_offer"),
            color = Color(0xFF059669),
            enabled = !isGameOver,
            tag = "draw_offer_btn",
            onClick = onDrawOffer
        )

        CircularActionButton(
            icon = Icons.Default.Flag,
            label = AppStrings.get(language, "resign"),
            color = Color(0xFFDC2626),
            enabled = !isGameOver,
            tag = "resign_btn",
            onClick = onResign
        )
    }
}

@Composable
private fun LandscapeActionButtons(
    boardState: BoardState,
    isAiThinking: Boolean,
    isGameOver: Boolean,
    language: AppLanguage,
    onUndo: () -> Unit,
    onHint: () -> Unit,
    onOpenChat: () -> Unit,
    onDrawOffer: () -> Unit,
    onResign: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(10.dp))
            .padding(horizontal = 2.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularActionButton(
            icon = Icons.Default.Undo,
            label = AppStrings.get(language, "undo"),
            color = Color(0xFF475569),
            enabled = boardState.moveHistory.isNotEmpty() && !isAiThinking,
            tag = "undo_move_btn",
            onClick = onUndo
        )
        CircularActionButton(
            icon = Icons.Default.Lightbulb,
            label = AppStrings.get(language, "hint"),
            color = Color(0xFFD97706),
            enabled = !isAiThinking && !isGameOver,
            tag = "hint_btn",
            onClick = onHint
        )
        CircularActionButton(
            icon = Icons.Default.Chat,
            label = AppStrings.get(language, "chat"),
            color = Color(0xFF0284C7),
            enabled = !isGameOver,
            tag = "chat_btn",
            onClick = onOpenChat
        )
        CircularActionButton(
            icon = Icons.Default.Flag,
            label = AppStrings.get(language, "resign"),
            color = Color(0xFFDC2626),
            enabled = !isGameOver,
            tag = "resign_btn",
            onClick = onResign
        )
    }
}

@Composable
fun CircularActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    enabled: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag(tag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (enabled) color else Color(0xFFCBD5E1),
            modifier = Modifier.size(19.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (enabled) Color(0xFF334155) else Color(0xFF94A3B8)
        )
    }
}
