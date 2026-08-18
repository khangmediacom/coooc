import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

replacement = """        CircularActionButton(
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
            icon = Icons.Default.Sync,
            label = AppStrings.get(language, "flip_board", "Xoay bàn"),
            color = Color(0xFF6B4226),
            enabled = true,
            tag = "flip_btn",
            onClick = { /* Add flip logic later */ }
        )
        CircularActionButton(
            icon = Icons.Default.Refresh,
            label = AppStrings.get(language, "new_game", "Ván mới"),
            color = Color(0xFF6B4226),
            enabled = true,
            tag = "new_game_btn",
            onClick = onResign
        )"""

# In both SinglePlayerBottomControls and MultiplayerBottomControls, replace the existing buttons.
content = re.sub(
    r'CircularActionButton\(\s*icon = Icons\.AutoMirrored\.Filled\.Undo,.*onClick = onResign\s*\)',
    replacement,
    content,
    flags=re.DOTALL
)

# And also change the container color for the row:
content = re.sub(
    r'Modifier\.fillMaxWidth\(\)\s*\.clip\(RoundedCornerShape\(14\.dp\)\)\s*\.background\(Color\(0xFF24160C\)\.copy\(alpha = 0\.90f\)\)\s*\.border\(1\.2\.dp, Color\(0xFFD4AF37\)\.copy\(alpha = 0\.50f\), RoundedCornerShape\(14\.dp\)\)',
    r'Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.Transparent)',
    content,
    flags=re.DOTALL
)

# Update the CircularActionButton style
button_replacement = """fun CircularActionButton(
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
            .width(80.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEEDDBC))
            .border(1.dp, Color(0xFFD4C1A0), RoundedCornerShape(20.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(tag),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color.copy(alpha = if (enabled) 1f else 0.5f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = color.copy(alpha = if (enabled) 1f else 0.5f)
        )
    }
}"""
content = re.sub(
    r'fun CircularActionButton\([\s\S]*?\}\s*\}\s*\}',
    button_replacement,
    content
)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
