import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

content = content.replace("LandscapeActionButtons(", "SinglePlayerBottomControls(")
content = content.replace("SinglePlayerBottomControls(boardState = boardState,isAiThinking = isAiThinking,isGameOver = isGameOver,language = language,onUndo = onUndo,onHint = onHint,onOpenChat = onOpenChat,onDrawOffer = onDrawOffer,onResign = { showResignConfirm = true })", "SinglePlayerBottomControls(boardState = boardState,isAiThinking = isAiThinking,isGameOver = isGameOver,language = language,onUndo = onUndo,onHint = onHint,onResign = { showResignConfirm = true })")

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
