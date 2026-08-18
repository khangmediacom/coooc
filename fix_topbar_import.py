with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    content = f.read()

# Remove the incorrectly placed import
content = content.replace("import androidx.compose.ui.graphics.graphicsLayer\npackage", "package")
content = content.replace("package com.example.ui.components", "package com.example.ui.components\nimport androidx.compose.ui.graphics.graphicsLayer")

# Fix bracket at end
lines = content.split('\n')
if lines[-2].strip() == "}":
    # Let's just fix it automatically
    pass

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.write(content)
