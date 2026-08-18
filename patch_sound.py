import re

with open("app/src/main/java/com/example/engine/SoundManager.kt", "r") as f:
    content = f.read()

old_loop = """                        val written = track.write(renderChunk, 0, renderChunk.size)
                        if (written < 0) {
                            delay(250)
                        }"""

new_loop = """                        val written = track.write(renderChunk, 0, renderChunk.size)
                        if (written <= 0) {
                            delay(250)
                        } else {
                            delay(50) // Prevent tight loop in emulators where write() might not block
                        }"""

content = content.replace(old_loop, new_loop)
with open("app/src/main/java/com/example/engine/SoundManager.kt", "w") as f:
    f.write(content)
print("Patched sound loop!")
