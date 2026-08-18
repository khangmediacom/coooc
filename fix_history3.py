with open('app/src/main/java/com/example/ui/screens/HistoryScreen.kt', 'r') as f:
    text = f.read()

import re
text = re.sub(r'val toneColor = when \(match\.result\) \{[\s\S]*?\}', 'val toneColor = when (match.winner) {\n                            match.playerColor -> JadeEmerald\n                            "DRAW" -> Gold\n                            else -> RubyRed\n                        }', text)

text = re.sub(r'val resultText = when \(match\.result\) \{[\s\S]*?\}', 'val resultText = when (match.winner) {\n                            match.playerColor -> AppStrings.get(language, "win")\n                            "DRAW" -> AppStrings.get(language, "draw")\n                            else -> AppStrings.get(language, "loss")\n                        }', text)

text = re.sub(r'match\.moveCount', 'match.totalMoves', text)

with open('app/src/main/java/com/example/ui/screens/HistoryScreen.kt', 'w') as f:
    f.write(text)
