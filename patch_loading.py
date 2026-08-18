import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Replace the LaunchEffect block with a simple navigateTo
content = re.sub(
    r'LaunchedEffect\(Unit\) \{.*?\n\s+viewModel\.navigateTo\(CurrentScreen\.Welcome\)\n\s+\}',
    r'LaunchedEffect(Unit) {\n                viewModel.navigateTo(CurrentScreen.Welcome)\n            }',
    content, flags=re.DOTALL
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
