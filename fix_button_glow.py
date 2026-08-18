import re

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "r") as f:
    content = f.read()

old_button_glow = """                    // Glow effect
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        GoldAmber.copy(alpha = buttonGlowAlpha),
                                        Color.Transparent
                                    )
                                )
                            )
                    )"""

new_button_glow = """                    // Glow effect
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .graphicsLayer { alpha = buttonGlowAlpha }
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        GoldAmber,
                                        Color.Transparent
                                    )
                                )
                            )
                    )"""

content = content.replace(old_button_glow, new_button_glow)
with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "w") as f:
    f.write(content)
print("Fixed button glow alpha!")
