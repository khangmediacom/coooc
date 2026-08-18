with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    text = f.read()

import re
text = re.sub(r'is CurrentScreen\.Customization -> \{.*?\n\s*\}\n', 'is CurrentScreen.Customization -> {\n            CustomizationScreen(\n                preferences = preferences,\n                onLanguageChange = { lang -> viewModel.updateLanguage(lang) },\n                onToggleSound = { enabled -> viewModel.toggleSound(enabled) },\n                onBack = { viewModel.navigateTo(CurrentScreen.Home) },\n                modifier = modifier\n            )\n        }\n', text, flags=re.DOTALL)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(text)
