import re
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Remove HanumanTipCard definition and its usage
content = re.sub(r'@Composable\s*fun HanumanTipCard\(\)\s*\{[\s\S]*?\}\s*\}\s*\}', '', content)
content = content.replace("HanumanTipCard()", "")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
