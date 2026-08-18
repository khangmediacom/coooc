import re
with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

content = content.replace('Icons.Default.Sync', 'Icons.Default.Autorenew')
content = content.replace('Icons.Default.Refresh', 'Icons.Default.Refresh') # keep it, but add import
if 'import androidx.compose.material.icons.filled.Refresh' not in content:
    content = 'import androidx.compose.material.icons.filled.Refresh\nimport androidx.compose.material.icons.filled.Autorenew\n' + content

content = content.replace('AppStrings.get(language, "flip_board", "Xoay bàn")', 'AppStrings.get(language, "flip_board")')
content = content.replace('AppStrings.get(language, "new_game", "Ván mới")', 'AppStrings.get(language, "new_game")')

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    content2 = f.read()
# Let's fix the brackets for AppShellTopBar.kt once and for all
lines = content2.splitlines()
while lines[-1].strip() == "}":
    lines.pop()
with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.write("\n".join(lines))
    f.write("\n    }\n}\n")
