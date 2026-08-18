with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Replace the mangled import line
content = content.replace(
    "package com.example.ui.screensimport androidx.compose.material.icons.filled.Refreshimport androidx.compose.material.icons.filled.Autorenewimport android.content.res.Configuration",
    "package com.example.ui.screens\nimport androidx.compose.material.icons.filled.Refresh\nimport androidx.compose.material.icons.filled.Autorenew\nimport android.content.res.Configuration"
)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
