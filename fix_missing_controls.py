import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# It looks like the regex replaced TOO MUCH and wiped out the function declarations for SinglePlayerBottomControls and MultiplayerBottomControls.
# We need to recreate it.

single_player_controls = """
@Composable
fun SinglePlayerBottomControls(
    boardState: com.example.model.BoardState,
    isAiThinking: Boolean,
    isGameOver: Boolean,
    language: AppLanguage,
    onUndo: () -> Unit,
    onHint: () -> Unit,
    onResign: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Transparent)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularActionButton(
            icon = Icons.AutoMirrored.Filled.Undo,
            label = AppStrings.get(language, "undo"),
            color = Color(0xFF6B4226),
            enabled = boardState.moveHistory.isNotEmpty() && !isAiThinking,
            tag = "undo_move_btn",
            onClick = onUndo
        )
        CircularActionButton(
            icon = Icons.Default.Lightbulb,
            label = AppStrings.get(language, "hint"),
            color = Color(0xFF6B4226),
            enabled = !isAiThinking && !isGameOver,
            tag = "hint_btn",
            onClick = onHint
        )
        CircularActionButton(
            icon = Icons.Default.Autorenew,
            label = AppStrings.get(language, "flip_board"),
            color = Color(0xFF6B4226),
            enabled = true,
            tag = "flip_btn",
            onClick = { /* Add flip logic later */ }
        )
        CircularActionButton(
            icon = Icons.Default.Refresh,
            label = AppStrings.get(language, "new_game"),
            color = Color(0xFF6B4226),
            enabled = true,
            tag = "new_game_btn",
            onClick = onResign
        )
    }
}
"""

multiplayer_controls = """
@Composable
fun MultiplayerBottomControls(
    boardState: com.example.model.BoardState,
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
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Transparent)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularActionButton(
            icon = Icons.AutoMirrored.Filled.Undo,
            label = AppStrings.get(language, "undo"),
            color = Color(0xFF6B4226),
            enabled = boardState.moveHistory.isNotEmpty(),
            tag = "undo_move_btn",
            onClick = onUndo
        )
        CircularActionButton(
            icon = Icons.Default.Lightbulb,
            label = AppStrings.get(language, "hint"),
            color = Color(0xFF6B4226),
            enabled = !isGameOver,
            tag = "hint_btn",
            onClick = onHint
        )
        CircularActionButton(
            icon = Icons.Default.Autorenew,
            label = AppStrings.get(language, "flip_board"),
            color = Color(0xFF6B4226),
            enabled = true,
            tag = "flip_btn",
            onClick = { /* Add flip logic later */ }
        )
        CircularActionButton(
            icon = Icons.Default.Refresh,
            label = AppStrings.get(language, "new_game"),
            color = Color(0xFF6B4226),
            enabled = true,
            tag = "new_game_btn",
            onClick = onResign
        )
    }
}
"""

if "fun SinglePlayerBottomControls" not in content:
    content += single_player_controls + multiplayer_controls

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
