with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    lines = f.readlines()
for i, line in enumerate(lines[225:245]):
    print(i + 225, line.rstrip())
