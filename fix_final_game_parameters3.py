import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Fix all remaining instances of onOpenChat and onDrawOffer passed to SinglePlayerBottomControls
content = re.sub(r'onOpenChat\s*=\s*\{ showChatDialog = true \},\s*onDrawOffer\s*=\s*onDrawOffer,', '', content)
content = re.sub(r'onOpenChat\s*=\s*onOpenChat,\s*onDrawOffer\s*=\s*onDrawOffer,', '', content)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)

