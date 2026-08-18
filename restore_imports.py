with open('app/src/main/java/com/example/ui/screens/GameScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('import com.example.ui.components.CapturedRow', 'import com.example.ui.components.CapturedRow\nimport com.example.ui.components.GameOverDialog\nimport com.example.ui.components.LiveChatDialog')

with open('app/src/main/java/com/example/ui/screens/GameScreen.kt', 'w') as f:
    f.write(content)

with open('app/src/main/java/com/example/ui/screens/OnlineLobbyScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('TextFieldDefaults.outlinedTextFieldColors', 'TextFieldDefaults.colors')

with open('app/src/main/java/com/example/ui/screens/OnlineLobbyScreen.kt', 'w') as f:
    f.write(content)
