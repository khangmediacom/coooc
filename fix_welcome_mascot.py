import re

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "r") as f:
    content = f.read()

broken_code = """                SubcomposeAsyncImage(
                    model = R.drawable.mascot1,
                    contentDescription = "Hanuman Mascot",
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                    modifier = Modifier
                        .size(200.dp,
                    loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator(color = GoldAmber) } }
                )
                        .offset(y = (-5).dp)
                        .graphicsLayer { translationY = floatOffsetY }
                )"""

fixed_code = """                SubcomposeAsyncImage(
                    model = R.drawable.mascot1,
                    contentDescription = "Hanuman Mascot",
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                    modifier = Modifier
                        .size(200.dp)
                        .offset(y = (-5).dp)
                        .graphicsLayer { translationY = floatOffsetY },
                    loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator(color = GoldAmber) } }
                )"""

content = content.replace(broken_code, fixed_code)
with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "w") as f:
    f.write(content)
print("Fixed mascot block!")
