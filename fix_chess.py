import re

with open("app/src/main/java/com/example/ui/components/ChessBoardView.kt", "r") as f:
    content = f.read()

content = content.replace(".background(checkGlowAlpha)", ".drawBehind { drawRect(checkGlowAlpha) }")
if "import androidx.compose.ui.draw.drawBehind" not in content:
    content = content.replace("import androidx.compose.ui.Modifier", "import androidx.compose.ui.Modifier\nimport androidx.compose.ui.draw.drawBehind")

with open("app/src/main/java/com/example/ui/components/ChessBoardView.kt", "w") as f:
    f.write(content)
print("Fixed ChessBoardView!")
