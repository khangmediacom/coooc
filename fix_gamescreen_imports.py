with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if line.startswith("import androidx.compose.material.icons.filled.Refreshimport androidx.compose.material.icons.filled.Autorenewpackage"):
        new_lines.append("package com.example.ui.screens\n")
        new_lines.append("import androidx.compose.material.icons.filled.Refresh\n")
        new_lines.append("import androidx.compose.material.icons.filled.Autorenew\n")
    elif line.startswith("import androidx.compose.material.icons.filled.Refresh"):
        # Just clean up if it's messed up
        if "package" in line:
            new_lines.append("package com.example.ui.screens\n")
            new_lines.append("import androidx.compose.material.icons.filled.Refresh\n")
        else:
            new_lines.append(line)
    else:
        new_lines.append(line)

# Let's just fix it by ensuring "package com.example.ui.screens" is the FIRST line that isn't empty, and imports follow.
cleaned_lines = [l for l in new_lines if l.strip()]
valid_lines = []
found_package = False
for line in cleaned_lines:
    if "package com.example.ui.screens" in line:
        found_package = True
        valid_lines = ["package com.example.ui.screens\n"]
    elif "package" in line and not found_package:
        pass # ignore messed up package
    else:
        valid_lines.append(line)

if not found_package:
    valid_lines.insert(0, "package com.example.ui.screens\n")

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write("".join(valid_lines))
