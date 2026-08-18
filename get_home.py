import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()
print("Lines:", len(content.splitlines()))

# find history
history_match = re.search(r'//\s*.*History', content, re.IGNORECASE)
if history_match:
    print("Found history at:", history_match.group(0))
