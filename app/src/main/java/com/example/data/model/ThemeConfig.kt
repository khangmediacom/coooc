package com.example.data.model

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    VIETNAMESE("vi", "Tiếng Việt", "🇻🇳"),
    ENGLISH("en", "English", "🇬🇧"),
    KHMER("km", "ភាសាខ្មែរ", "🇰🇭"),
    FRENCH("fr", "Français", "🇫🇷");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: VIETNAMESE
        }
    }
}

enum class BoardTheme(
    val id: String,
    val titleVi: String,
    val titleEn: String,
    val titleKm: String,
    val titleFr: String,
    val lightSquareColorHex: Long,
    val darkSquareColorHex: Long,
    val borderHex: Long,
    val description: String
) {
    CLASSIC(
        "classic",
        "Cổ điển (Classic Wood)",
        "Classic Wood",
        "ក្តារឈើបុរាណ",
        "Bois Classique",
        0xFFF0D9B5,
        0xFFB58863,
        0xFF8A6546,
        "Clean traditional wooden tournament board"
    ),
    DARK(
        "dark",
        "Đêm Huyền Bí (Dark Slate)",
        "Dark Slate",
        "ក្តារថ្មងងឹត",
        "Ardoise Sombre",
        0xFF475569,
        0xFF1E293B,
        0xFF0F172A,
        "High-contrast slate dark board"
    ),
    WOOD(
        "wood",
        "Gỗ Hoàng Gia (Royal Teak)",
        "Royal Teak Wood",
        "ឈើប្រណិតរាជវង្ស",
        "Teck Royal",
        0xFFDEB887,
        0xFF8B4513,
        0xFF5C2D0C,
        "Warm rich teakwood board"
    ),
    MARBLE(
        "marble",
        "Đá Cẩm Thạch (Marble)",
        "Marble Stone",
        "ថ្មម៉ាបសហសម័យ",
        "Pierre de Marbre",
        0xFFE2E8F0,
        0xFF64748B,
        0xFF334155,
        "Smooth contemporary marble texture"
    );

    companion object {
        fun fromId(id: String): BoardTheme {
            return entries.find { it.id.equals(id, ignoreCase = true) || it.name.equals(id, ignoreCase = true) } ?: CLASSIC
        }
    }
}

enum class PieceStyle(
    val id: String,
    val titleVi: String,
    val titleEn: String,
    val titleKm: String,
    val titleFr: String,
    val description: String
) {
    CLASSIC(
        "ada-red",
        "Crimson (Đỏ & Trắng)",
        "Crimson & Ivory",
        "ពណ៌ក្រហម និង ភ្លុក",
        "Pourpre & Ivoire",
        "Authentic royal Crimson & Ivory vectorized Makruk pieces"
    ),
    SCULPTED(
        "ada",
        "Ivory (Trắng Ngà)",
        "Ivory & Charcoal",
        "ភ្លុកបុរាណ",
        "Ivoire & Anthracite",
        "Crisp Ivory and deep Charcoal pieces"
    ),
    MINIMALIST(
        "cambodian",
        "Ouk (Khmer Truyền Thống)",
        "Ouk Chaktrang Cambodian",
        "អុកចត្រង្គខ្មែរ",
        "Ouk Chaktrang Cambodgien",
        "Traditional Angkor Cambodian stone vector pieces"
    );

    companion object {
        fun fromId(id: String): PieceStyle {
            return entries.find { it.id.equals(id, ignoreCase = true) || it.name.equals(id, ignoreCase = true) } ?: CLASSIC
        }
    }
}

data class UserPreferences(
    val boardTheme: BoardTheme = BoardTheme.CLASSIC,
    val pieceStyle: PieceStyle = PieceStyle.CLASSIC,
    val language: AppLanguage = AppLanguage.VIETNAMESE,
    val soundEnabled: Boolean = true,
    val soundVolume: Float = 0.85f,
    val musicEnabled: Boolean = true,
    val musicVolume: Float = 0.60f,
    val hapticsEnabled: Boolean = true,
    val showLegalMoves: Boolean = true,
    val showCoordinates: Boolean = true,
    val isDarkMode: Boolean = false,
    val autoPromote: Boolean = true
)
