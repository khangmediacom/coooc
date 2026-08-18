import re
with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Replace the CircularActionButton function
button_replacement = """@Composable
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
"""

content = re.sub(
    r'@Composable\s*fun CircularActionButton\([\s\S]*?\}\s*\}\s*\}',
    button_replacement,
    content
)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
