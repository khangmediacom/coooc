package com.example.data.model

data class TacticsLesson(
    val id: String,
    val chapter: Int,
    val titleVi: String,
    val titleEn: String,
    val titleKm: String,
    val titleFr: String = "",
    val descriptionVi: String,
    val descriptionEn: String,
    val descriptionKm: String = "",
    val descriptionFr: String = "",
    val fenPieces: List<Triple<Position, PieceType, PieceColor>>,
    val turn: PieceColor,
    val expectedMoves: List<Pair<Position, Position>>, // From to To sequence
    val explanationVi: String,
    val explanationEn: String,
    val explanationKm: String = "",
    val explanationFr: String = "",
    val difficulty: String = "Cơ bản"
)

data class LeaderboardEntry(
    val rank: Int,
    val playerId: String,
    val playerName: String,
    val avatarId: String,
    val countryFlag: String,
    val elo: Int,
    val tier: String,
    val wins: Int,
    val losses: Int,
    val winRate: Int,
    val isCurrentUser: Boolean = false
)

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val senderName: String,
    val message: String,
    val isSelf: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isEmote: Boolean = false
)

object QuickEmotes {
    val list = listOf(
        "👋 Xin chào! (Sousdey)",
        "👏 Nước cờ rất hay!",
        "🤔 Đang tính toán...",
        "⚡ Chiếu tướng!",
        "🛡️ Phòng thủ kiên cố!",
        "🔥 Trận đấu kịch tính!",
        "🤝 Chúc bạn may mắn!",
        "☕ Đấu cờ vui vẻ!"
    )
}
