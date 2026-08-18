import re

files_to_check = [
    'app/src/main/java/com/example/ui/screens/WelcomeScreen.kt',
    'app/src/main/java/com/example/ui/components/KhmerMascotIcons.kt',
    'app/src/main/java/com/example/ui/components/ChessBoardView.kt',
    'app/src/main/java/com/example/ui/components/HanumanCoachComponent.kt'
]

# We will just replace infiniteRepeatable with snap() or tween(0) to kill the animation overhead, OR just remove the rotation usage.
# Better yet, let's just make the angles constant.

with open('app/src/main/java/com/example/ui/screens/WelcomeScreen.kt', 'r') as f:
    content = f.read()

# Replace animated floats with static values to stop recomposition loop
content = re.sub(
    r'val rotationAngle by infiniteTransition\.animateFloat\([\s\S]*?label = "solar_wheel_rotation"\n    \)',
    'val rotationAngle = 0f',
    content
)
content = re.sub(
    r'val floatOffsetY by infiniteTransition\.animateFloat\([\s\S]*?label = "hanuman_float"\n    \)',
    'val floatOffsetY = 0f',
    content
)
content = re.sub(
    r'val floatRotation by infiniteTransition\.animateFloat\([\s\S]*?label = "hanuman_rotate"\n    \)',
    'val floatRotation = 0f',
    content
)

with open('app/src/main/java/com/example/ui/screens/WelcomeScreen.kt', 'w') as f:
    f.write(content)

