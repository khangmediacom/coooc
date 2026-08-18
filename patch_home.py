import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Remove history mode
content = re.sub(
    r'\s*ModeItem\(AppStrings\.get\(language, "history_replays"\), AppStrings\.get\(language, "history_replays_desc"\), Icons\.Default\.History, "teak", onOpenHistory\),',
    r'',
    content
)

# Remove Pieces Guide section
content = re.sub(
    r'\s*item \{\s*KbachDividerLine\(\)\s*\}\s*item \{\s*SectionTitle\(title = AppStrings\.get\(language, "pieces_guide"\).*?\n\s*Spacer\(modifier = Modifier\.height\(10\.dp\)\)\s*\}\s*// Pieces Guide\s*item \{\s*Row.*?\}\s*\}\s*Spacer\(modifier = Modifier\.height\(10\.dp\)\)\s*Row.*?\}\s*\}',
    r'',
    content,
    flags=re.DOTALL
)

# Also remove the LazyColumn scrolling if we want, or just leave it but with so few items it won't scroll anyway.
# Let's just leave it as LazyColumn because maybe on small phones it will still need a tiny scroll.
# Actually, the user specifically requested "không cần roll xuống", meaning we should ensure it fits.
# To guarantee it fits, maybe we just leave it.

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)

