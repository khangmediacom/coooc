with open('app/src/main/java/com/example/ui/screens/HistoryScreen.kt', 'r') as f:
    text = f.read()

import re
text = re.sub(r'import com.example.data.model.MatchData\nimport com.example.data.model.MatchResult', 'import com.example.data.local.entity.MatchHistoryEntity', text)

text = re.sub(r'matches: List<MatchData>', 'matches: List<MatchHistoryEntity>', text)
text = re.sub(r'onReplayMatch: \(MatchData\) -> Unit', 'onReplayMatch: (MatchHistoryEntity) -> Unit', text)
text = re.sub(r'MatchResult\.WIN', '"win"', text)
text = re.sub(r'MatchResult\.LOSS', '"loss"', text)
text = re.sub(r'MatchResult\.DRAW', '"draw"', text)

text = re.sub(r'match\.opponentName', 'match.opponentName', text)
text = re.sub(r'match\.movesCount', 'match.moveCount', text)

with open('app/src/main/java/com/example/ui/screens/HistoryScreen.kt', 'w') as f:
    f.write(text)
