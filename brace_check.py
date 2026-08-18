import sys

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

lines = content.split('\n')
depth = 0
for i, line in enumerate(lines):
    # This is a naive check, ignore quotes for now
    for char in line:
        if char == '{':
            depth += 1
        elif char == '}':
            depth -= 1
    if depth < 0:
        print(f"Error: Negative depth at line {i+1}")
        break
print(f"Final depth: {depth}")
