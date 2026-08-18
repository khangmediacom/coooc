import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Fix the unresolved LandscapeActionButtons by just using SinglePlayerBottomControls for now
content = content.replace("LandscapeActionButtons(", "SinglePlayerBottomControls(")

# Ensure correct AppStrings replacement
content = re.sub(r'AppStrings\.get\(language, "flip_board", "Xoay bàn"\)', 'AppStrings.get(language, "flip_board")', content)
content = re.sub(r'AppStrings\.get\(language, "new_game", "Ván mới"\)', 'AppStrings.get(language, "new_game")', content)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
