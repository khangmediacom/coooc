import re

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "r") as f:
    content = f.read()

# Fix the offset recomposition issue
content = content.replace(".offset(y = floatOffsetY.dp)", ".offset { androidx.compose.ui.unit.IntOffset(0, floatOffsetY.roundToInt()) }")

# Add import for roundToInt if missing
if "import kotlin.math.roundToInt" not in content:
    content = content.replace("import androidx.compose.ui.unit.sp", "import androidx.compose.ui.unit.sp\nimport kotlin.math.roundToInt")

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "w") as f:
    f.write(content)
print("Fixed WelcomeScreen.kt")
