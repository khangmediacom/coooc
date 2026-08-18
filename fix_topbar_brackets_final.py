with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    lines = f.read().splitlines()

# find if we have extra braces at the end
count = 0
for line in reversed(lines):
    if line.strip() == "}":
        count += 1
    elif line.strip():
        break
        
print("Found", count, "braces at end")
if count > 2:
    lines = lines[:-(count-2)]

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.write('\n'.join(lines) + '\n')

