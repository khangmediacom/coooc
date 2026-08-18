import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

replacement = """import coil.imageLoader
import coil.request.ImageRequest
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.CircularProgressIndicator

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
                kotlinx.coroutines.Dispatchers.IO.invoke {
                    val imageLoader = context.imageLoader
                    val imagesToLoad = listOf(
                        com.example.R.drawable.mascot1,
                        com.example.R.drawable.mascot,
                        com.example.R.drawable.angkor_bg,
                        com.example.R.drawable.logo_icon
                    )
                    for (img in imagesToLoad) {
                        val request = coil.request.ImageRequest.Builder(context)
                            .data(img)
                            .build()
                        imageLoader.execute(request)
                    }
                }
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
        is CurrentScreen.Welcome -> {"""

# Replace from @Composable fun MainContent to is CurrentScreen.Welcome -> {
pattern = r"@Composable\nfun MainContent\(\n    viewModel: GameViewModel,\n    modifier: Modifier = Modifier\n\) \{.*when \(val screen = currentScreen\) \{\n        is CurrentScreen.Welcome -> \{"

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("Patched MainActivity Loading")
