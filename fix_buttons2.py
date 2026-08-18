with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Find the start of CircularActionButton
start_idx = content.find("fun CircularActionButton(")
if start_idx == -1:
    print("Not found!")
    exit(1)

# Find the end of it. We can just cut the rest of the file and manually recreate SinglePlayerBottomControls and MultiplayerBottomControls since they are at the very end of the file.
# Wait, let's see what is after CircularActionButton.
