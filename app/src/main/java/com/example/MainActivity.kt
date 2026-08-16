package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AIDifficulty
import com.example.data.model.AppLanguage
import com.example.data.model.PieceColor
import com.example.ui.components.GoogleSignInDialog
import com.example.ui.localization.AppStrings
import com.example.ui.screens.CustomizationScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MatchHistoryScreen
import com.example.ui.screens.OnlineLobbyScreen
import com.example.ui.screens.ReplayViewerScreen
import com.example.ui.screens.TacticsPlayScreen
import com.example.ui.screens.TacticsScreen
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.KhmerChessTheme
import com.example.viewmodel.CurrentScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val preferences by viewModel.preferences.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val showSignInDialog by viewModel.showSignInDialog.collectAsState()

            val showBottomBar = currentScreen !is CurrentScreen.Game && currentScreen !is CurrentScreen.TacticsPlay && currentScreen !is CurrentScreen.Replay

            KhmerChessTheme(darkTheme = preferences.isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            ElegantBottomBar(
                                currentScreen = currentScreen,
                                language = preferences.language,
                                onNavigate = { target -> viewModel.navigateTo(target) }
                            )
                        }
                    }
                ) { innerPadding ->
                    MainContent(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                // Global Google Sign In Dialog
                if (showSignInDialog) {
                    GoogleSignInDialog(
                        language = preferences.language,
                        onSignInSuccess = { email, name ->
                            viewModel.signInWithGoogle(email, name)
                        },
                        onContinueAsGuest = {
                            viewModel.closeSignInDialog()
                        },
                        onDismiss = {
                            viewModel.closeSignInDialog()
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = viewModel.preferences.value
        if (prefs.musicEnabled) {
            com.example.engine.AudioHaptics.startBgm()
        }
    }

    override fun onStop() {
        super.onStop()
        com.example.engine.AudioHaptics.stopBgm()
    }

    override fun onDestroy() {
        super.onDestroy()
        com.example.engine.AudioHaptics.stopBgm()
    }
}

@Composable
fun ElegantBottomBar(
    currentScreen: CurrentScreen,
    language: AppLanguage,
    onNavigate: (CurrentScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A).copy(alpha = 0.95f))
            .border(width = 1.dp, color = Color(0xFF334155).copy(alpha = 0.6f))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            label = AppStrings.get(language, "nav_home"),
            icon = Icons.Default.Home,
            selected = currentScreen is CurrentScreen.Home,
            onClick = { onNavigate(CurrentScreen.Home) },
            tag = "nav_home"
        )
        BottomNavItem(
            label = AppStrings.get(language, "nav_play"),
            icon = Icons.Default.SportsEsports,
            selected = currentScreen is CurrentScreen.OnlineLobby,
            onClick = { onNavigate(CurrentScreen.OnlineLobby) },
            tag = "nav_play"
        )
        BottomNavItem(
            label = AppStrings.get(language, "nav_learn"),
            icon = Icons.Default.MenuBook,
            selected = currentScreen is CurrentScreen.Tactics,
            onClick = { onNavigate(CurrentScreen.Tactics) },
            tag = "nav_learn"
        )
        BottomNavItem(
            label = AppStrings.get(language, "nav_profile"),
            icon = Icons.Default.Person,
            selected = currentScreen is CurrentScreen.Customization || currentScreen is CurrentScreen.Leaderboard,
            onClick = { onNavigate(CurrentScreen.Customization) },
            tag = "nav_profile"
        )
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag(tag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFFE5A83B) else Color(0xFF94A3B8),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color(0xFFE5A83B) else Color(0xFF94A3B8)
        )
    }
}

@Composable
fun MainContent(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val boardState by viewModel.boardState.collectAsState()
    val historySnapshots by viewModel.historySnapshots.collectAsState()
    val reviewMoveIndex by viewModel.reviewMoveIndex.collectAsState()
    val selectedPos by viewModel.selectedPos.collectAsState()
    val legalMoves by viewModel.legalMoves.collectAsState()
    val preferences by viewModel.preferences.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val matchHistoryList by viewModel.matchHistoryList.collectAsState()
    val lessonProgressList by viewModel.lessonProgressList.collectAsState()
    val isOnlineSearching by viewModel.isOnlineSearching.collectAsState()
    val replayMoves by viewModel.replayMoves.collectAsState()
    val replayIndex by viewModel.replayCurrentIndex.collectAsState()

    when (val screen = currentScreen) {
        is CurrentScreen.Home -> {
            HomeScreen(
                userProfile = userProfile,
                language = preferences.language,
                onStartAi = { diff, color -> viewModel.startAiGame(diff, color) },
                onStartLocal2P = { viewModel.startLocal2PGame() },
                onStartOnline = { viewModel.navigateTo(CurrentScreen.OnlineLobby) },
                onOpenTactics = { viewModel.navigateTo(CurrentScreen.Tactics) },
                onOpenHistory = { viewModel.navigateTo(CurrentScreen.History) },
                onOpenLeaderboard = { viewModel.navigateTo(CurrentScreen.Leaderboard) },
                onOpenCustomization = { viewModel.navigateTo(CurrentScreen.Customization) },
                onPromptSignIn = { viewModel.openSignInDialog() },
                modifier = modifier
            )
        }

        is CurrentScreen.Game -> {
            GameScreen(
                boardState = boardState,
                historySnapshots = historySnapshots,
                reviewMoveIndex = reviewMoveIndex,
                selectedPos = selectedPos,
                legalMoves = legalMoves,
                preferences = preferences,
                userProfile = userProfile,
                gameMode = screen.mode,
                difficulty = screen.difficulty,
                playerColor = screen.playerColor,
                isAiThinking = isAiThinking,
                chatMessages = chatMessages,
                onSquareClick = { pos -> viewModel.onSquareSelected(pos) },
                onSelectMoveIndex = { idx -> viewModel.reviewMove(idx) },
                onStepMove = { delta -> viewModel.stepReview(delta) },
                onJumpToStart = { viewModel.jumpToStart() },
                onJumpToLive = { viewModel.jumpToLive() },
                onToggleSound = { enabled -> viewModel.toggleSound(enabled) },
                onUpdateSoundVolume = { vol -> viewModel.updateSoundVolume(vol) },
                onToggleMusic = { enabled -> viewModel.toggleMusic(enabled) },
                onUpdateMusicVolume = { vol -> viewModel.updateMusicVolume(vol) },
                onSelectTheme = { theme -> viewModel.updateBoardTheme(theme) },
                onSelectPieceStyle = { style -> viewModel.updatePieceStyle(style) },
                onUndo = { viewModel.undoMove() },
                onHint = { viewModel.requestHint() },
                onResign = { viewModel.resignGame() },
                onDrawOffer = { viewModel.offerDraw() },
                onSendMessage = { text -> viewModel.sendChatMessage(text) },
                onSendEmote = { emote -> viewModel.sendChatMessage(emote, isEmote = true) },
                onRematch = {
                    if (screen.mode == com.example.data.model.GameMode.AI) {
                        viewModel.startAiGame(screen.difficulty, screen.playerColor)
                    } else if (screen.mode == com.example.data.model.GameMode.LOCAL_2P) {
                        viewModel.startLocal2PGame()
                    } else {
                        viewModel.startOnlineQuickMatch()
                    }
                },
                onReplay = {
                    val lastMatch = matchHistoryList.firstOrNull()
                    if (lastMatch != null) {
                        viewModel.loadMatchForReplay(lastMatch)
                    }
                },
                onHome = { viewModel.navigateTo(CurrentScreen.Home) },
                modifier = modifier
            )
        }

        is CurrentScreen.Tactics -> {
            TacticsScreen(
                progressList = lessonProgressList,
                language = preferences.language,
                onSelectLesson = { lesson -> viewModel.startTacticsLesson(lesson) },
                onBack = { viewModel.navigateTo(CurrentScreen.Home) },
                modifier = modifier
            )
        }

        is CurrentScreen.TacticsPlay -> {
            val isDone = lessonProgressList.any { it.lessonId == screen.lesson.id && it.completed }
            TacticsPlayScreen(
                lesson = screen.lesson,
                boardState = boardState,
                selectedPos = selectedPos,
                legalMoves = legalMoves,
                preferences = preferences,
                isCompleted = isDone,
                onSquareClick = { pos -> viewModel.onSquareSelected(pos) },
                onHint = { viewModel.requestHint() },
                onNextLesson = { viewModel.navigateTo(CurrentScreen.Tactics) },
                onBack = { viewModel.navigateTo(CurrentScreen.Tactics) },
                modifier = modifier
            )
        }

        is CurrentScreen.History -> {
            MatchHistoryScreen(
                matches = matchHistoryList,
                language = preferences.language,
                onSelectMatch = { match -> viewModel.loadMatchForReplay(match) },
                onBack = { viewModel.navigateTo(CurrentScreen.Home) },
                modifier = modifier
            )
        }

        is CurrentScreen.Replay -> {
            ReplayViewerScreen(
                match = screen.match,
                boardState = boardState,
                replayMoves = replayMoves,
                currentIndex = replayIndex,
                preferences = preferences,
                onSetIndex = { idx -> viewModel.setReplayIndex(idx) },
                onBack = { viewModel.navigateTo(CurrentScreen.History) },
                modifier = modifier
            )
        }

        is CurrentScreen.Leaderboard -> {
            LeaderboardScreen(
                entries = viewModel.getLeaderboard(),
                language = preferences.language,
                isLoggedIn = userProfile.isLoggedIn,
                onPromptSignIn = { viewModel.openSignInDialog() },
                onBack = { viewModel.navigateTo(CurrentScreen.Home) },
                modifier = modifier
            )
        }

        is CurrentScreen.Customization -> {
            CustomizationScreen(
                preferences = preferences,
                userProfile = userProfile,
                onSelectTheme = { theme -> viewModel.updateBoardTheme(theme) },
                onSelectPieceStyle = { style -> viewModel.updatePieceStyle(style) },
                onSelectLanguage = { lang -> viewModel.updateLanguage(lang) },
                onToggleDarkMode = { enabled -> viewModel.toggleDarkMode(enabled) },
                onToggleSound = { enabled -> viewModel.toggleSound(enabled) },
                onUpdateSoundVolume = { vol -> viewModel.updateSoundVolume(vol) },
                onToggleMusic = { enabled -> viewModel.toggleMusic(enabled) },
                onUpdateMusicVolume = { vol -> viewModel.updateMusicVolume(vol) },
                onTestSound = { type -> viewModel.testSoundEffect(type) },
                onUpdateUsername = { name -> viewModel.updateUsername(name) },
                onPromptSignIn = { viewModel.openSignInDialog() },
                onSignOut = { viewModel.signOut() },
                onBack = { viewModel.navigateTo(CurrentScreen.Home) },
                modifier = modifier
            )
        }

        is CurrentScreen.OnlineLobby -> {
            OnlineLobbyScreen(
                isSearching = isOnlineSearching,
                language = preferences.language,
                onQuickMatch = { viewModel.startOnlineQuickMatch() },
                onBack = { viewModel.navigateTo(CurrentScreen.Home) },
                modifier = modifier
            )
        }
    }
}
