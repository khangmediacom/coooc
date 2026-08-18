import re
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace('AppStrings.get(language, "pieces_guide", "Hướng dẫn quân cờ")', 'AppStrings.get(language, "pieces_guide")')

if 'import androidx.compose.material.icons.filled.MenuBook' not in content:
    content = content.replace('import androidx.compose.material.icons.filled.Public', 'import androidx.compose.material.icons.filled.Public\nimport androidx.compose.material.icons.filled.MenuBook')
    
content = content.replace('Icons.AutoMirrored.Filled.MenuBook', 'Icons.Filled.MenuBook')

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)

