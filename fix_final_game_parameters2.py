import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

content = re.sub(r'onOpenChat = \{ showChatDialog = true \},\s*onDrawOffer = onDrawOffer,', '', content)
content = re.sub(r'onOpenChat = onOpenChat,\s*onDrawOffer = onDrawOffer,', '', content)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)

