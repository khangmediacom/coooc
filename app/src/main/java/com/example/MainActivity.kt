package com.example

import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.components.AppShellTopBar

import com.example.ui.localization.AppStrings
import com.example.ui.screens.CustomizationScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlayScreen
import com.example.ui.screens.OnlineScreen
import com.example.ui.screens.TacticsScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.CustomizationScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.CustomizationScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.OnlineLobbyScreen
import com.example.ui.screens.ReplayViewerScreen
import com.example.ui.screens.TacticsScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.CustomizationScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.KhmerChessTheme
import com.example.viewmodel.CurrentScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            val preferences by viewModel.preferences.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val showSignInDialog by viewModel.showSignInDialog.collectAsState()

            val showBottomBar = currentScreen !is CurrentScreen.Game && 
                 
                currentScreen !is CurrentScreen.Replay && 
                currentScreen !is CurrentScreen.Welcome

            KhmerChessTheme(darkTheme = preferences.isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFFDFBF7),
                    contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
                    
                    topBar = {
                        if (showBottomBar) {
                            val titleKey = when (currentScreen) {
                                is CurrentScreen.Home -> "home"
                                is CurrentScreen.Play -> "play"
                                is CurrentScreen.OnlineLobby -> "online_match"
                                is CurrentScreen.Tactics -> "learn"
                                is CurrentScreen.Leaderboard -> "ranks"
                                is CurrentScreen.Customization -> "settings"
                                is CurrentScreen.History -> "history_replays"
                                else -> "app_title"
                            }
                            AppShellTopBar(
                                title = AppStrings.get(preferences.language, titleKey),
                                subtitle = AppStrings.get(preferences.language, "app_subtitle"),
                                onlineCountText = AppStrings.get(preferences.language, "online_count")
                            )
                        }
                    },
                    bottomBar = {
                        if (showBottomBar) {
                            ElegantBottomBar(
                                currentScreen = currentScreen,
                                language = preferences.language,
                                onNavigate = { target -> viewModel.navigateTo(target) },
                                modifier = Modifier.navigationBarsPadding()
                            )
                        }
                    }
                ) { innerPadding ->
                    MainContent(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .statusBarsPadding()
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
        // AudioHaptics disabled to prevent startup freeze
    }

    override fun onStop() {
        super.onStop()
        // AudioHaptics disabled
    }

    override fun onDestroy() {
        super.onDestroy()
        // AudioHaptics disabled
    }
}

@Composable
fun ElegantBottomBar(
    currentScreen: CurrentScreen,
    language: AppLanguage,
    onNavigate: (CurrentScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F1E2)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = AppStrings.get(language, "home"),
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                selected = currentScreen is CurrentScreen.Home,
                onClick = { onNavigate(CurrentScreen.Home) },
                tag = "nav_home"
            )
            BottomNavItem(
                label = AppStrings.get(language, "play"),
                selectedIcon = Icons.Filled.SportsEsports,
                unselectedIcon = Icons.Outlined.SportsEsports,                
                selected = currentScreen is CurrentScreen.Play,
                onClick = { onNavigate(CurrentScreen.Play) },
                tag = "nav_play"
            )
            BottomNavItem(
                label = AppStrings.get(language, "learn"),
                selectedIcon = Icons.Filled.MenuBook,
                unselectedIcon = Icons.Outlined.MenuBook,
                selected = currentScreen is CurrentScreen.Tactics,
                onClick = { onNavigate(CurrentScreen.Tactics) },
                tag = "nav_learn"
            )
            BottomNavItem(
                label = AppStrings.get(language, "ranks"),
                selectedIcon = Icons.Filled.EmojiEvents,
                unselectedIcon = Icons.Outlined.EmojiEvents,
                selected = currentScreen is CurrentScreen.Leaderboard,
                onClick = { onNavigate(CurrentScreen.Leaderboard) },
                tag = "nav_ranks"
            )
            BottomNavItem(
                label = AppStrings.get(language, "settings"),
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                selected = currentScreen is CurrentScreen.Customization,
                onClick = { onNavigate(CurrentScreen.Customization) },
                tag = "nav_settings"
            )
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    val navModifier = if (selected) {
        Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFEEDDBC))
    } else {
        Modifier.clip(RoundedCornerShape(24.dp))
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = navModifier
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .testTag(tag)
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = if (selected) Color(0xFF8B5E34) else Color(0xFF78716C),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color(0xFF8B5E34) else Color(0xFF78716C)
        )
        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(2.dp)
                    .background(Color(0xFF8B5E34))
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
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
        is CurrentScreen.Loading -> {
            val context = androidx.compose.ui.platform.LocalContext.current
            LaunchedEffect(Unit) {
                viewModel.navigateTo(CurrentScreen.Welcome)
            }
            Box(
                modifier = modifier.fillMaxSize().background(Color(0xFFF9F7F1)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading Assets...", color = Color(0xFF867E75))
                }
            }
        }
        is CurrentScreen.Welcome -> {
            WelcomeScreen(
                currentLanguage = preferences.language,
                onSelectLanguage = { lang -> viewModel.updateLanguage(lang) },
                onEnter = { viewModel.navigateTo(CurrentScreen.Home) },
                modifier = modifier
            )
        }

        is CurrentScreen.Play -> {
            PlayScreen(
                language = preferences.language,
                onStartAi = { diff, color -> viewModel.startAiGame(diff, color) },
                onStartLocal2P = { viewModel.startLocal2PGame() },
                modifier = modifier
            )
        }

        is CurrentScreen.OnlineLobby -> {
            OnlineScreen(
                language = preferences.language,
                onFindMatch = { viewModel.startOnlineQuickMatch() },
                modifier = modifier
            )
        }

        is CurrentScreen.Tactics -> {
            TacticsScreen(
                language = preferences.language,
                onStartLesson = { lessonId -> 
                    val lesson = com.example.data.model.TacticsData.lessons.find { it.id == lessonId }
                    if (lesson != null) viewModel.startTacticsLesson(lesson)
                },
                modifier = modifier
            )
        }

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

        is CurrentScreen.TacticsPlay -> {}
        is CurrentScreen.History -> {
            HistoryScreen(
                matches = matchHistoryList,
                language = preferences.language,
                onReplayMatch = { match -> viewModel.loadMatchForReplay(match) },
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
                language = preferences.language,
                modifier = modifier
            )
        }
is CurrentScreen.Customization -> {
            CustomizationScreen(
                preferences = preferences,
                onLanguageChange = { lang -> viewModel.updateLanguage(lang) },
                onToggleSound = { enabled -> viewModel.toggleSound(enabled) },
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
