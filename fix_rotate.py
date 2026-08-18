import re

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace(".rotate(rotationAngle)", ".graphicsLayer { rotationZ = rotationAngle }")

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "w") as f:
    f.write(content)
print("Fixed rotationAngle!")
