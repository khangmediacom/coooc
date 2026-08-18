with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

start_idx = content.find("@Composable\nfun CircularActionButton(")
if start_idx == -1:
    start_idx = content.find("fun CircularActionButton(")

if start_idx != -1:
    content = content[:start_idx]

replacement = """@Composable
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
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(76.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEEDDBC))
            .border(1.dp, Color(0xFFD4C1A0), RoundedCornerShape(12.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .testTag(tag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color.copy(alpha = if (enabled) 1f else 0.5f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = color.copy(alpha = if (enabled) 1f else 0.5f)
        )
    }
}

@Composable
fun SinglePlayerBottomControls(
    boardState: BoardState,
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

@Composable
fun MultiplayerBottomControls(
    boardState: BoardState,
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

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content + replacement)
