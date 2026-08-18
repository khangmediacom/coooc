import re

with open("app/src/main/java/com/example/engine/SoundManager.kt", "r") as f:
    content = f.read()

# Replace the body of startBackgroundMusic
pattern = r"fun startBackgroundMusic\(\) \{.*?\n    \}"
new_bgm = """fun startBackgroundMusic() {
        // Disabled BGM to prevent infinite loop/freeze in cloud emulators
    }"""
content = re.sub(pattern, new_bgm, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/engine/SoundManager.kt", "w") as f:
    f.write(content)
print("Disabled BGM!")
