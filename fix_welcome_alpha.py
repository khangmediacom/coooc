import re

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "r") as f:
    content = f.read()

old_glow = """                // Gold Glow background
                Box(
                    modifier = Modifier
                        .size(110.dp, 55.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-8).dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    GoldAmber.copy(alpha = 0.50f + (floatOffsetY / 40f)),
                                    Color.Transparent
                                )
                            )
                        )
                )"""

new_glow = """                // Gold Glow background
                Box(
                    modifier = Modifier
                        .size(110.dp, 55.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-8).dp)
                        .graphicsLayer { alpha = 0.50f + (floatOffsetY / 40f) }
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    GoldAmber,
                                    Color.Transparent
                                )
                            )
                        )
                )"""

content = content.replace(old_glow, new_glow)
with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "w") as f:
    f.write(content)
print("Fixed glow alpha!")
