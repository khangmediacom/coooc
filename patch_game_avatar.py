import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Replace the text symbol avatar with mascot.png in GameScreen
old_avatar = """                Text(
                    text = if (isWhite) "♔" else "♚",
                    fontSize = 19.sp,
                    color = if (isWhite) Color(0xFF1E293B) else Color(0xFFFFFFFF)
                )"""

new_avatar = """                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.mascot),
                    contentDescription = "Avatar",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().padding(2.dp).clip(CircleShape)
                )"""

if old_avatar in content:
    content = content.replace(old_avatar, new_avatar)
    with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
        f.write(content)
    print("Patched GameScreen.kt avatar!")
else:
    print("Avatar text block not found in GameScreen.kt")
