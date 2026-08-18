import re

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "r") as f:
    content = f.read()

# Change background color
content = re.sub(
    r'\.background\(Color\(0xE6FDFCF9\)\)',
    r'.background(Color(0xFFF9F1E2))',
    content
)

# Update the pill color
content = re.sub(
    r'JadeEmerald\.copy\(alpha = 0\.1f\)',
    r'Color(0xFFDDE6DD)',
    content
)
content = re.sub(
    r'JadeEmerald\.copy\(alpha = 0\.4f\)',
    r'Color(0xFFB1C8B1)',
    content
)
content = re.sub(
    r'color = JadeEmerald',
    r'color = Color(0xFF388E3C)',
    content
)
content = re.sub(
    r'\.background\(JadeEmerald\)',
    r'.background(Color(0xFF388E3C))',
    content
)

# Add the Lovable style divider at the bottom
divider = """        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFD4C1A0)))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD4C1A0)))
            Spacer(modifier = Modifier.width(8.dp))
            // Diamond
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .androidx.compose.ui.draw.rotate(45f)
                    .border(1.5.dp, Color(0xFFD4C1A0))
                    .padding(2.dp)
                    .background(Color(0xFFD4C1A0))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD4C1A0)))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFD4C1A0)))
        }
    }
}
"""
content = re.sub(r'        \}\n    \}\n\}\n?$', divider, content)

with open("app/src/main/java/com/example/ui/components/AppShellTopBar.kt", "w") as f:
    f.write(content)
