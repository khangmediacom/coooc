import re
with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    content = f.read()

content = content.replace('.Modifier.graphicsLayer { rotationZ = 45f }(45f)', '.graphicsLayer { rotationZ = 45f }')

if "import androidx.compose.ui.graphics.graphicsLayer" not in content:
    content = "import androidx.compose.ui.graphics.graphicsLayer\n" + content

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.write(content)
