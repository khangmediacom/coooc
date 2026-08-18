with open('app/src/main/java/com/example/ui/screens/HomeScreen.kt', 'r') as f:
    lines = f.readlines()

for i, line in enumerate(lines):
    if line.strip() == "@Composable" and i+1 < len(lines) and "Box(" in lines[i+1]:
        lines[i] = "@Composable\nfun HanumanTipCard() {\n"

with open('app/src/main/java/com/example/ui/screens/HomeScreen.kt', 'w') as f:
    f.writelines(lines)
