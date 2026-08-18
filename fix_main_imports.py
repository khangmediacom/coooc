with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("Icons.AutoMirrored.Filled.MenuBook", "Icons.Filled.MenuBook")
content = content.replace("Icons.AutoMirrored.Outlined.MenuBook", "Icons.Outlined.MenuBook")

if "import androidx.compose.foundation.layout.width" not in content:
    content = content.replace("import androidx.compose.foundation.layout.height", "import androidx.compose.foundation.layout.height\nimport androidx.compose.foundation.layout.width")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
