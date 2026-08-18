import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Fix the broken Image modifier
content = re.sub(
    r'modifier = Modifier\.fillMaxSize\(\s*\)\.clip\(RoundedCornerShape\(16\.dp\)\)',
    r'modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))',
    content
)
content = re.sub(
    r'modifier = Modifier\.fillMaxSize\(\n\s*\)\.clip',
    r'modifier = Modifier.fillMaxSize().clip',
    content
)

# And another one down below?
content = re.sub(
    r'modifier = Modifier\.fillMaxSize\(\n\s*\)\.padding\(4\.dp\)\.clip',
    r'modifier = Modifier.fillMaxSize().padding(4.dp).clip',
    content
)

# Fix the end of HomeScreen
content = re.sub(
    r'\s+items\(modes\) \{ mode ->\n\s+ModeCard\(mode\)\n\s+Spacer\(modifier = Modifier\.height\(10\.dp\)\)\n\s+\}\n\s+\}\n\s+\}\n\s+\}\n\s+\}\n\s+\}',
    r"""        items(modes) { mode ->
            ModeCard(mode)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
""",
    content
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
