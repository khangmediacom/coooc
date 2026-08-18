with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    text = f.read()

import re
text = re.sub(
    r'is CurrentScreen\.History, is CurrentScreen\.TacticsPlay -> \{\}',
    'is CurrentScreen.TacticsPlay -> {}\n        is CurrentScreen.History -> {\n            HistoryScreen(\n                matches = matchHistoryList,\n                language = preferences.language,\n                onReplayMatch = { match -> viewModel.loadMatchForReplay(match) },\n                modifier = modifier\n            )\n        }',
    text
)

text = re.sub(
    r'import com.example.ui.screens.CustomizationScreen',
    'import com.example.ui.screens.CustomizationScreen\nimport com.example.ui.screens.HistoryScreen',
    text
)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(text)
