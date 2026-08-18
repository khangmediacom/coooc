with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    lines = f.readlines()
# Ensure file ends correctly
while lines[-1].strip() == "}":
    lines.pop()
with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.writelines(lines)
    f.write("    }\n}\n")
