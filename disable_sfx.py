import re

with open("app/src/main/java/com/example/engine/SoundManager.kt", "r") as f:
    content = f.read()

# Replace playPcm with empty body
pattern = r"private fun playPcm\(pcm: ShortArray, volumeScale: Float = 1\.0f\) \{.*?\n    \}"
new_play = """private fun playPcm(pcm: ShortArray, volumeScale: Float = 1.0f) {
        // Disabled SFX playback to prevent AudioTrack.write from blocking Dispatchers.Default pool
        // which causes the AI and the whole app to freeze in cloud environments!
    }"""
content = re.sub(pattern, new_play, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/engine/SoundManager.kt", "w") as f:
    f.write(content)
print("Disabled SFX!")
