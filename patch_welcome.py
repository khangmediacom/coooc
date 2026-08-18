import re

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "r") as f:
    content = f.read()

# 1. Mascot size: 200.dp to 280.dp. Box size 135.dp to 280.dp
content = re.sub(
    r'\.size\(135\.dp\)',
    r'.size(280.dp)',
    content
)
content = re.sub(
    r'\.size\(200\.dp\)',
    r'.size(280.dp)',
    content
)

# 2. Push frame to bottom.
# Before KbachFramedCard, there is `Spacer(modifier = Modifier.height(10.dp))`
content = re.sub(
    r'Spacer\(modifier = Modifier\.height\(10\.dp\)\)\s*// 4\. Kbach-framed Welcome Card',
    r'Spacer(modifier = Modifier.weight(1f))\n            // 4. Kbach-framed Welcome Card',
    content
)
# And remove `Spacer(modifier = Modifier.height(16.dp))` after it
content = re.sub(
    r'Spacer\(modifier = Modifier\.height\(16\.dp\)\)\s*\}\s*\}',
    r'}\n    }',
    content
)
# Also change padding in the parent column
content = re.sub(
    r'modifier = Modifier\n                \.fillMaxSize\(\)\n                \.padding\(bottom = 16\.dp\),',
    r'modifier = Modifier\n                .fillMaxSize()\n                .padding(bottom = 0.dp),',
    content
)

# 3. Style the enter button like the Quick Play button.
button_replacement = """                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(com.example.ui.theme.RoyalGoldDark, com.example.ui.theme.RoyalGold.copy(alpha = 0.9f), com.example.ui.theme.GoldLight)
                                )
                            )
                            .clickable { onEnter() }
                            .testTag("btn_enter_temple"),
                        contentAlignment = Alignment.Center
                    ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = AppStrings.get(currentLanguage, "enter"),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )"""
content = re.sub(
    r'Button\(\s*onClick = \{\s*onEnter\(\)\s*\},\s*shape = RoundedCornerShape\(16\.dp\),\s*colors = ButtonDefaults\.buttonColors\(\s*containerColor = GoldAmber,\s*contentColor = Color\.White\s*\),\s*elevation = ButtonDefaults\.buttonElevation\(\s*defaultElevation = 0\.dp,\s*pressedElevation = 0\.dp\s*\),\s*modifier = Modifier\s*\.fillMaxSize\(\)\s*\.testTag\("btn_enter_temple"\)\s*\)\s*\{\s*Row\(\s*verticalAlignment = Alignment\.CenterVertically,\s*horizontalArrangement = Arrangement\.Center\s*\)\s*\{\s*Text\(\s*text = AppStrings\.get\(currentLanguage, "enter"\),\s*color = Color\.White,\s*fontSize = 15\.sp,\s*fontWeight = FontWeight\.Bold,\s*fontFamily = FontFamily\.Serif\s*\)',
    button_replacement,
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/ui/screens/WelcomeScreen.kt", "w") as f:
    f.write(content)
