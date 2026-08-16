package com.example.engine

import com.example.data.model.BoardState
import com.example.data.model.CountingRuleState
import com.example.data.model.GameStatus
import com.example.data.model.Move
import com.example.data.model.Piece
import com.example.data.model.PieceColor
import com.example.data.model.PieceType
import com.example.data.model.Position

object KhmerChessRules {

    fun createInitialBoard(): BoardState {
        val board = Array(8) { arrayOfNulls<Piece>(8) }

        // Black initial placement (Row 0 - Rank 8)
        board[0][0] = Piece(PieceType.ROOK, PieceColor.BLACK, id = "b_r_0")
        board[0][1] = Piece(PieceType.KNIGHT, PieceColor.BLACK, id = "b_n_1")
        board[0][2] = Piece(PieceType.BISHOP, PieceColor.BLACK, id = "b_b_2")
        board[0][3] = Piece(PieceType.QUEEN, PieceColor.BLACK, id = "b_q_3")
        board[0][4] = Piece(PieceType.KING, PieceColor.BLACK, id = "b_k_4")
        board[0][5] = Piece(PieceType.BISHOP, PieceColor.BLACK, id = "b_b_5")
        board[0][6] = Piece(PieceType.KNIGHT, PieceColor.BLACK, id = "b_n_6")
        board[0][7] = Piece(PieceType.ROOK, PieceColor.BLACK, id = "b_r_7")

        // Black Pawns (Row 2 - Rank 6 in traditional Khmer chess)
        for (col in 0..7) {
            board[2][col] = Piece(PieceType.PAWN, PieceColor.BLACK, id = "b_p_$col")
        }

        // White Pawns (Row 5 - Rank 3 in traditional Khmer chess)
        for (col in 0..7) {
            board[5][col] = Piece(PieceType.PAWN, PieceColor.WHITE, id = "w_p_$col")
        }

        // White initial placement (Row 7 - Rank 1)
        board[7][0] = Piece(PieceType.ROOK, PieceColor.WHITE, id = "w_r_0")
        board[7][1] = Piece(PieceType.KNIGHT, PieceColor.WHITE, id = "w_n_1")
        board[7][2] = Piece(PieceType.BISHOP, PieceColor.WHITE, id = "w_b_2")
        board[7][3] = Piece(PieceType.KING, PieceColor.WHITE, id = "w_k_3")
        board[7][4] = Piece(PieceType.QUEEN, PieceColor.WHITE, id = "w_q_4")
        board[7][5] = Piece(PieceType.BISHOP, PieceColor.WHITE, id = "w_b_5")
        board[7][6] = Piece(PieceType.KNIGHT, PieceColor.WHITE, id = "w_n_6")
        board[7][7] = Piece(PieceType.ROOK, PieceColor.WHITE, id = "w_r_7")

        return BoardState(
            board = board,
            currentTurn = PieceColor.WHITE,
            status = GameStatus.PLAYING
        )
    }

    fun getLegalMoves(state: BoardState, from: Position): List<Move> {
        val piece = state.board[from.row][from.col] ?: return emptyList()
        if (piece.color != state.currentTurn) return emptyList()

        val candidateMoves = getCandidateMoves(state.board, from, piece, state.inCheck)
        return candidateMoves.filter { move ->
            val simulatedBoard = simulateBoardAfterMove(state.board, move)
            !isKingInCheck(simulatedBoard, piece.color)
        }
    }

    fun getAllLegalMoves(state: BoardState, color: PieceColor = state.currentTurn): List<Move> {
        val moves = mutableListOf<Move>()
        val board = state.board
        val inCheck = state.inCheck

        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board[r][c]
                if (piece != null && piece.color == color) {
                    val from = Position(r, c)
                    val candidateMoves = getCandidateMoves(board, from, piece, inCheck)
                    for (move in candidateMoves) {
                        val simulatedBoard = simulateBoardAfterMove(board, move)
                        if (!isKingInCheck(simulatedBoard, color)) {
                            moves.add(move)
                        }
                    }
                }
            }
        }
        return moves
    }

    private fun simulateBoardAfterMove(board: Array<Array<Piece?>>, move: Move): Array<Array<Piece?>> {
        val newBoard = Array(8) { r ->
            Array(8) { c ->
                board[r][c]
            }
        }
        val piece = newBoard[move.from.row][move.from.col] ?: return newBoard
        val finalPiece = if (move.isPromotion || (piece.type == PieceType.PAWN &&
                    ((piece.color == PieceColor.WHITE && move.to.row <= 2) ||
                     (piece.color == PieceColor.BLACK && move.to.row >= 5)))) {
            piece.copy(type = PieceType.PROMOTED_PAWN, hasMoved = true)
        } else {
            piece.copy(hasMoved = true)
        }

        newBoard[move.from.row][move.from.col] = null
        newBoard[move.to.row][move.to.col] = finalPiece
        return newBoard
    }

    private fun getCandidateMoves(
        board: Array<Array<Piece?>>,
        from: Position,
        piece: Piece,
        isInCheck: Boolean
    ): List<Move> {
        val moves = mutableListOf<Move>()
        val color = piece.color
        val oppColor = color.opposite()
        val r = from.row
        val c = from.col

        when (piece.type) {
            PieceType.KING -> {
                val dirs = arrayOf(
                    -1 to -1, -1 to 0, -1 to 1,
                    0 to -1,           0 to 1,
                    1 to -1,  1 to 0,  1 to 1
                )
                for ((dr, dc) in dirs) {
                    val tr = r + dr
                    val tc = c + dc
                    if (tr in 0..7 && tc in 0..7) {
                        val target = board[tr][tc]
                        if (target == null || target.color == oppColor) {
                            moves.add(Move(from, Position(tr, tc), piece, target))
                        }
                    }
                }

                // Special Khmer 1st move leap (Knight jump) if unmoved and not in check
                if (!piece.hasMoved && !isInCheck) {
                    val knightDirs = arrayOf(
                        -2 to -1, -2 to 1,
                        -1 to -2, -1 to 2,
                        1 to -2,  1 to 2,
                        2 to -1,  2 to 1
                    )
                    for ((dr, dc) in knightDirs) {
                        val tr = r + dr
                        val tc = c + dc
                        if (tr in 0..7 && tc in 0..7) {
                            if (board[tr][tc] == null) {
                                moves.add(Move(from, Position(tr, tc), piece, null, isKingLeap = true))
                            }
                        }
                    }
                }
            }

            PieceType.QUEEN, PieceType.PROMOTED_PAWN -> {
                val diagDirs = arrayOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
                for ((dr, dc) in diagDirs) {
                    val tr = r + dr
                    val tc = c + dc
                    if (tr in 0..7 && tc in 0..7) {
                        val target = board[tr][tc]
                        if (target == null || target.color == oppColor) {
                            moves.add(Move(from, Position(tr, tc), piece, target))
                        }
                    }
                }

                // Special Queen 1st move: 2 steps straight forward if unmoved and empty
                if (piece.type == PieceType.QUEEN && !piece.hasMoved) {
                    val forwardDir = if (color == PieceColor.WHITE) -1 else 1
                    val step1r = r + forwardDir
                    val step2r = r + forwardDir * 2
                    if (step1r in 0..7 && step2r in 0..7) {
                        if (board[step1r][c] == null && board[step2r][c] == null) {
                            moves.add(Move(from, Position(step2r, c), piece, null, isQueenLeap = true))
                        }
                    }
                }
            }

            PieceType.BISHOP -> {
                val forwardDir = if (color == PieceColor.WHITE) -1 else 1
                val directions = arrayOf(
                    forwardDir to 0,
                    -1 to -1, -1 to 1, 1 to -1, 1 to 1
                )
                for ((dr, dc) in directions) {
                    val tr = r + dr
                    val tc = c + dc
                    if (tr in 0..7 && tc in 0..7) {
                        val target = board[tr][tc]
                        if (target == null || target.color == oppColor) {
                            moves.add(Move(from, Position(tr, tc), piece, target))
                        }
                    }
                }
            }

            PieceType.KNIGHT -> {
                val knightDirs = arrayOf(
                    -2 to -1, -2 to 1,
                    -1 to -2, -1 to 2,
                    1 to -2,  1 to 2,
                    2 to -1,  2 to 1
                )
                for ((dr, dc) in knightDirs) {
                    val tr = r + dr
                    val tc = c + dc
                    if (tr in 0..7 && tc in 0..7) {
                        val target = board[tr][tc]
                        if (target == null || target.color == oppColor) {
                            moves.add(Move(from, Position(tr, tc), piece, target))
                        }
                    }
                }
            }

            PieceType.ROOK -> {
                val orthoDirs = arrayOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
                for ((dr, dc) in orthoDirs) {
                    var tr = r + dr
                    var tc = c + dc
                    while (tr in 0..7 && tc in 0..7) {
                        val target = board[tr][tc]
                        if (target == null) {
                            moves.add(Move(from, Position(tr, tc), piece, null))
                        } else {
                            if (target.color == oppColor) {
                                moves.add(Move(from, Position(tr, tc), piece, target))
                            }
                            break
                        }
                        tr += dr
                        tc += dc
                    }
                }
            }

            PieceType.PAWN -> {
                val forwardDir = if (color == PieceColor.WHITE) -1 else 1
                val promotionRow = if (color == PieceColor.WHITE) 2 else 5

                // 1 step forward non-capture
                val fr = r + forwardDir
                if (fr in 0..7 && board[fr][c] == null) {
                    val willPromote = (color == PieceColor.WHITE && fr <= promotionRow) || (color == PieceColor.BLACK && fr >= promotionRow)
                    moves.add(Move(from, Position(fr, c), piece, null, isPromotion = willPromote))
                }

                // Diagonal captures
                val capCols = intArrayOf(c - 1, c + 1)
                for (cc in capCols) {
                    if (fr in 0..7 && cc in 0..7) {
                        val target = board[fr][cc]
                        if (target != null && target.color == oppColor) {
                            val willPromote = (color == PieceColor.WHITE && fr <= promotionRow) || (color == PieceColor.BLACK && fr >= promotionRow)
                            moves.add(Move(from, Position(fr, cc), piece, target, isPromotion = willPromote))
                        }
                    }
                }
            }
        }

        return moves
    }

    fun isKingInCheck(board: Array<Array<Piece?>>, color: PieceColor): Boolean {
        var kingPos: Position? = null
        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board[r][c]
                if (piece != null && piece.type == PieceType.KING && piece.color == color) {
                    kingPos = Position(r, c)
                    break
                }
            }
            if (kingPos != null) break
        }
        if (kingPos == null) return false

        val oppColor = color.opposite()
        for (r in 0..7) {
            for (c in 0..7) {
                val piece = board[r][c]
                if (piece != null && piece.color == oppColor) {
                    val attacks = getCandidateMoves(board, Position(r, c), piece, isInCheck = false)
                    if (attacks.any { it.to == kingPos }) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun applyMove(state: BoardState, move: Move, checkLegality: Boolean = true): BoardState {
        val newBoard = simulateBoardAfterMove(state.board, move)
        val originalPiece = state.board[move.from.row][move.from.col] ?: return state
        val captured = state.board[move.to.row][move.to.col]

        val nextTurn = state.currentTurn.opposite()
        val inCheck = isKingInCheck(newBoard, nextTurn)

        val newCapturedWhite = state.capturedByWhite.toMutableList()
        val newCapturedBlack = state.capturedByBlack.toMutableList()
        if (captured != null) {
            if (state.currentTurn == PieceColor.WHITE) {
                newCapturedWhite.add(captured)
            } else {
                newCapturedBlack.add(captured)
            }
        }

        val isPromoted = move.isPromotion || (originalPiece.type == PieceType.PAWN &&
                ((originalPiece.color == PieceColor.WHITE && move.to.row <= 2) ||
                 (originalPiece.color == PieceColor.BLACK && move.to.row >= 5)))

        val notation = buildNotation(move, originalPiece, captured, isPromoted)
        val detailedMove = move.copy(notation = notation)

        if (!checkLegality) {
            // Lightweight fast path for AI / Replay simulation without deep recursion
            return state.copy(
                board = newBoard,
                currentTurn = nextTurn,
                moveCount = state.moveCount + 1,
                moveHistory = state.moveHistory + detailedMove,
                capturedByWhite = newCapturedWhite,
                capturedByBlack = newCapturedBlack,
                inCheck = inCheck,
                lastMove = detailedMove
            )
        }

        var newStatus = GameStatus.PLAYING
        var winner: PieceColor? = null

        // Full check only for human/actual game state
        val simulatedNextState = BoardState(
            board = newBoard,
            currentTurn = nextTurn,
            inCheck = inCheck
        )
        val nextLegalMoves = getAllLegalMoves(simulatedNextState, nextTurn)

        if (nextLegalMoves.isEmpty()) {
            if (inCheck) {
                newStatus = GameStatus.CHECKMATE
                winner = state.currentTurn
            } else {
                newStatus = GameStatus.STALEMATE
            }
        } else if (inCheck) {
            newStatus = GameStatus.CHECK
        }

        val updatedCounting = updateCountingRule(newBoard, state.countingRule, nextTurn)
        if (updatedCounting.isCountingActive && updatedCounting.currentMovesLeft <= 0 && newStatus == GameStatus.PLAYING) {
            newStatus = GameStatus.DRAW_BY_COUNT
        }

        return state.copy(
            board = newBoard,
            currentTurn = nextTurn,
            moveCount = state.moveCount + 1,
            moveHistory = state.moveHistory + detailedMove,
            capturedByWhite = newCapturedWhite,
            capturedByBlack = newCapturedBlack,
            status = newStatus,
            winner = winner,
            inCheck = inCheck,
            lastMove = detailedMove,
            countingRule = updatedCounting
        )
    }

    private fun updateCountingRule(
        board: Array<Array<Piece?>>,
        currentRule: CountingRuleState,
        nextTurn: PieceColor
    ): CountingRuleState {
        val whitePieces = mutableListOf<Piece>()
        val blackPieces = mutableListOf<Piece>()
        var hasUnpromotedPawns = false

        for (r in 0..7) {
            for (c in 0..7) {
                val p = board[r][c] ?: continue
                if (p.type == PieceType.PAWN) hasUnpromotedPawns = true
                if (p.color == PieceColor.WHITE) whitePieces.add(p) else blackPieces.add(p)
            }
        }

        if (hasUnpromotedPawns) {
            return CountingRuleState(isCountingActive = false)
        }

        var defender: PieceColor? = null
        if (whitePieces.size == 1 && whitePieces[0].type == PieceType.KING && blackPieces.size > 1) {
            defender = PieceColor.WHITE
        } else if (blackPieces.size == 1 && blackPieces[0].type == PieceType.KING && whitePieces.size > 1) {
            defender = PieceColor.BLACK
        }

        if (defender == null) {
            return CountingRuleState(isCountingActive = false)
        }

        if (!currentRule.isCountingActive || currentRule.defenderColor != defender) {
            val attackerPieces = if (defender == PieceColor.WHITE) blackPieces else whitePieces
            val rooks = attackerPieces.count { it.type == PieceType.ROOK }
            val bishops = attackerPieces.count { it.type == PieceType.BISHOP }
            val knights = attackerPieces.count { it.type == PieceType.KNIGHT }

            val maxMoves = when {
                rooks >= 2 -> 8
                rooks == 1 -> 16
                bishops >= 2 -> 22
                knights >= 2 -> 32
                bishops == 1 -> 44
                knights == 1 -> 64
                else -> maxOf(16, 64 - (attackerPieces.size + 1))
            }

            return CountingRuleState(
                isCountingActive = true,
                defenderColor = defender,
                initialMaxMoves = maxMoves,
                currentMovesLeft = maxMoves,
                reason = "Luật đếm cờ Ốc: $maxMoves nước"
            )
        } else {
            val newMovesLeft = if (nextTurn == defender) currentRule.currentMovesLeft - 1 else currentRule.currentMovesLeft
            return currentRule.copy(currentMovesLeft = newMovesLeft)
        }
    }

    private fun buildNotation(
        move: Move,
        piece: Piece,
        captured: Piece?,
        isPromote: Boolean
    ): String {
        val pieceChar = when (piece.type) {
            PieceType.KING -> "K"
            PieceType.QUEEN -> "Q"
            PieceType.BISHOP -> "B"
            PieceType.KNIGHT -> "N"
            PieceType.ROOK -> "R"
            PieceType.PAWN -> "P"
            PieceType.PROMOTED_PAWN -> "pQ"
        }
        val fromStr = move.from.toNotation()
        val toStr = move.to.toNotation()
        val captureChar = if (captured != null) "x" else "-"
        val promoStr = if (isPromote) "=Q" else ""
        return "$pieceChar$fromStr$captureChar$toStr$promoStr"
    }
}
