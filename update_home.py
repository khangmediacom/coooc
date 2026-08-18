import re
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

pieces_section = """
        item {
            Spacer(modifier = Modifier.height(16.dp))
            KbachDividerLine()
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(title = AppStrings.get(language, "pieces_guide", "Hướng dẫn quân cờ"), icon = Icons.AutoMirrored.Filled.MenuBook)
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp)
            ) {
                val pieces = listOf(
                    Triple(com.example.data.model.PieceType.KING, "Ang (Vua)", "Tướng"),
                    Triple(com.example.data.model.PieceType.QUEEN, "Neang (Hậu)", "Sĩ"),
                    Triple(com.example.data.model.PieceType.BISHOP, "Koul (Tượng)", "Tượng"),
                    Triple(com.example.data.model.PieceType.KNIGHT, "Ses (Mã)", "Mã"),
                    Triple(com.example.data.model.PieceType.ROOK, "Tuuk (Xe)", "Xe"),
                    Triple(com.example.data.model.PieceType.PAWN, "Trey (Tốt)", "Tốt")
                )
                items(pieces.size) { index ->
                    PieceGuideCard(
                        pieceType = pieces[index].first,
                        kmName = pieces[index].second,
                        enName = pieces[index].third,
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
"""

content = re.sub(r'items\(modes\)\s*\{\s*mode\s*->\s*ModeCard\(mode\)\s*Spacer\(modifier\s*=\s*Modifier.height\(10.dp\)\)\s*\}\s*\}\s*\}',
                 r'items(modes) { mode ->\n            ModeCard(mode)\n            Spacer(modifier = Modifier.height(10.dp))\n        }' + pieces_section,
                 content)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
