package com.example.data.model

enum class PieceColor {
    WHITE,
    BLACK;

    fun opposite(): PieceColor = if (this == WHITE) BLACK else WHITE
}

enum class PieceType(
    val khmerName: String,
    val vietName: String,
    val englishName: String,
    val value: Int
) {
    KING("ស្តេច (Ang)", "Vua (Ang)", "King", 10000),
    QUEEN("នាង (Neang)", "Hậu / Nàng (Neang)", "Queen / Seed", 300),
    BISHOP("គោល (Koul)", "Tượng / Trụ (Koul)", "Bishop / Nobleman", 350),
    KNIGHT("សេះ (Ses)", "Mã / Ngựa (Ses)", "Knight / Horse", 300),
    ROOK("ទូក (Tuok)", "Xe / Thuyền (Tuok)", "Rook / Boat", 500),
    PAWN("ត្រី (Trey)", "Tốt / Cá (Trey)", "Pawn / Fish", 100),
    PROMOTED_PAWN("ត្រីបំពង (Trey Bompong)", "Tốt lật (Trey Bompong)", "Promoted Fish", 300)
}

data class Position(val row: Int, val col: Int) {
    fun isValid(): Boolean = row in 0..7 && col in 0..7

    fun toNotation(): String {
        val file = ('a' + col).toString()
        val rank = (8 - row).toString()
        return "$file$rank"
    }

    companion object {
        fun fromNotation(notation: String): Position? {
            if (notation.length < 2) return null
            val col = notation[0] - 'a'
            val rank = notation[1].digitToIntOrNull() ?: return null
            val row = 8 - rank
            val pos = Position(row, col)
            return if (pos.isValid()) pos else null
        }
    }
}

data class Piece(
    val type: PieceType,
    val color: PieceColor,
    val hasMoved: Boolean = false,
    val id: String = "${color}_${type}_${java.util.UUID.randomUUID()}"
)

data class Move(
    val from: Position,
    val to: Position,
    val piece: Piece,
    val capturedPiece: Piece? = null,
    val isPromotion: Boolean = false,
    val isKingLeap: Boolean = false,
    val isQueenLeap: Boolean = false,
    val notation: String = ""
)
