import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = re.sub(
    r'\s*items\(modes\) \{ mode ->\s*ModeCard\(mode\)\s*Spacer\(modifier = Modifier\.height\(10\.dp\)\)\s*\}\s*\}\s*\}\s*\}\s*\}\s*\}',
    r'''
        items(modes) { mode ->
            ModeCard(mode)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
    }
}
''',
    content
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
