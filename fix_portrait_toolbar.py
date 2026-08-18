import re
with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Fix definition of PortraitCircularActionToolbar
content = re.sub(r'onOpenChat:\s*\(\)\s*->\s*Unit,\s*onDrawOffer:\s*\(\)\s*->\s*Unit,', '', content)

# Fix the background modifier of PortraitCircularActionToolbar
old_modifier = """        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF24160C).copy(alpha = 0.90f))
            .border(1.2.dp, Color(0xFFD4AF37).copy(alpha = 0.50f), RoundedCornerShape(16.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp)"""

new_modifier = """        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent)
            .padding(horizontal = 6.dp, vertical = 6.dp)"""

content = content.replace(old_modifier, new_modifier)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
