package com.example.data.model

enum class GameMode {
    AI,
    LOCAL_2P,
    ONLINE_MATCH,
    TACTICS_LESSON
}

enum class AIDifficulty(
    val titleVi: String,
    val titleEn: String,
    val titleKm: String,
    val titleFr: String,
    val elo: Int,
    val searchDepth: Int
) {
    BEGINNER("Tập sự", "Beginner", "អ្នកទើបចាប់ផ្តើម", "Débutant", 800, 1),
    AMATEUR("Nghiệp dư", "Amateur", "កម្រិតមធ្យម", "Amateur", 1200, 2),
    MASTER("Kiện tướng", "Master", "កម្រិតខ្ពស់", "Maître", 1600, 3),
    GRANDMASTER("Thần cờ Angkor", "Angkor Grandmaster", "មហាកំពូលអ្នកលេង", "Grand Maître d'Angkor", 2000, 4)
}

enum class GameStatus {
    NOT_STARTED,
    PLAYING,
    CHECK,
    CHECKMATE,
    STALEMATE,
    DRAW_BY_COUNT,
    RESIGNED,
    TIMEOUT
}

data class CountingRuleState(
    val isCountingActive: Boolean = false,
    val defenderColor: PieceColor? = null,
    val initialMaxMoves: Int = 0,
    val currentMovesLeft: Int = 0,
    val reason: String = ""
)

data class BoardState(
    val board: Array<Array<Piece?>> = Array(8) { arrayOfNulls<Piece?>(8) },
    val currentTurn: PieceColor = PieceColor.WHITE,
    val moveCount: Int = 0,
    val moveHistory: List<Move> = emptyList(),
    val capturedByWhite: List<Piece> = emptyList(),
    val capturedByBlack: List<Piece> = emptyList(),
    val status: GameStatus = GameStatus.PLAYING,
    val winner: PieceColor? = null,
    val inCheck: Boolean = false,
    val lastMove: Move? = null,
    val countingRule: CountingRuleState = CountingRuleState(),
    val whiteTimeMillis: Long = 600_000L, // 10 mins default
    val blackTimeMillis: Long = 600_000L
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoardState
        if (!board.contentDeepEquals(other.board)) return false
        if (currentTurn != other.currentTurn) return false
        if (moveCount != other.moveCount) return false
        if (moveHistory != other.moveHistory) return false
        if (capturedByWhite != other.capturedByWhite) return false
        if (capturedByBlack != other.capturedByBlack) return false
        if (status != other.status) return false
        if (winner != other.winner) return false
        if (inCheck != other.inCheck) return false
        if (lastMove != other.lastMove) return false
        if (countingRule != other.countingRule) return false
        if (whiteTimeMillis != other.whiteTimeMillis) return false
        if (blackTimeMillis != other.blackTimeMillis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + currentTurn.hashCode()
        result = 31 * result + moveCount
        result = 31 * result + moveHistory.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + (winner?.hashCode() ?: 0)
        result = 31 * result + inCheck.hashCode()
        result = 31 * result + (lastMove?.hashCode() ?: 0)
        result = 31 * result + countingRule.hashCode()
        return result
    }
}
