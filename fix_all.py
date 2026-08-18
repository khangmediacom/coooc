with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    content = f.read()

# fix the potential import issue in topbar
content = content.replace("import androidx.compose.ui.graphics.graphicsLayer\npackage", "package")
if "package com.example.ui.components\nimport androidx.compose.ui.graphics.graphicsLayer" not in content:
    content = content.replace("package com.example.ui.components", "package com.example.ui.components\nimport androidx.compose.ui.graphics.graphicsLayer")

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

content = content.replace("import androidx.compose.material.icons.filled.Refresh\nimport androidx.compose.material.icons.filled.Autorenew\npackage", "package")
if "import androidx.compose.material.icons.filled.Refresh" not in content:
    content = content.replace("package com.example.ui.screens", "package com.example.ui.screens\nimport androidx.compose.material.icons.filled.Refresh\nimport androidx.compose.material.icons.filled.Autorenew")

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)

