package com.example.engine

import com.example.data.model.AIDifficulty
import com.example.data.model.BoardState
import com.example.data.model.GameStatus
import com.example.data.model.Move
import com.example.data.model.PieceColor
import com.example.data.model.PieceType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.random.Random

object KhmerChessAI {

    private val PIECE_VALUES = mapOf(
        PieceType.KING to 20000,
        PieceType.ROOK to 520,
        PieceType.BISHOP to 340,
        PieceType.KNIGHT to 310,
        PieceType.QUEEN to 280,
        PieceType.PROMOTED_PAWN to 280,
        PieceType.PAWN to 100
    )

    // Center control bonus
    private val CENTER_TABLE = arrayOf(
        intArrayOf(0,  5,  5,  5,  5,  5,  5,  0),
        intArrayOf(5, 10, 15, 15, 15, 15, 10,  5),
        intArrayOf(5, 15, 25, 30, 30, 25, 15,  5),
        intArrayOf(5, 20, 30, 40, 40, 30, 20,  5),
        intArrayOf(5, 20, 30, 40, 40, 30, 20,  5),
        intArrayOf(5, 15, 25, 30, 30, 25, 15,  5),
        intArrayOf(5, 10, 15, 15, 15, 15, 10,  5),
        intArrayOf(0,  5,  5,  5,  5,  5,  5,  0)
    )

    suspend fun getBestMove(
        state: BoardState,
        difficulty: AIDifficulty,
        aiColor: PieceColor = state.currentTurn
    ): Move? = withContext(Dispatchers.Default) {
        try {
            val legalMoves = KhmerChessRules.getAllLegalMoves(state, aiColor)
            if (legalMoves.isEmpty()) return@withContext null

            when (difficulty) {
                AIDifficulty.BEGINNER -> {
                    if (Random.nextFloat() < 0.40f) {
                        legalMoves.random()
                    } else {
                        findMoveAlphaBeta(state, depth = 1, aiColor = aiColor) ?: legalMoves.random()
                    }
                }
                AIDifficulty.AMATEUR -> {
                    findMoveAlphaBeta(state, depth = 2, aiColor = aiColor) ?: legalMoves.random()
                }
                AIDifficulty.MASTER -> {
                    findMoveAlphaBeta(state, depth = 3, aiColor = aiColor) ?: legalMoves.random()
                }
                AIDifficulty.GRANDMASTER -> {
                    findMoveAlphaBeta(state, depth = 3, aiColor = aiColor) ?: legalMoves.random()
                }
            }
        } catch (_: Exception) {
            KhmerChessRules.getAllLegalMoves(state, aiColor).randomOrNull()
        }
    }

    private fun findMoveAlphaBeta(state: BoardState, depth: Int, aiColor: PieceColor): Move? {
        val legalMoves = KhmerChessRules.getAllLegalMoves(state, aiColor)
        if (legalMoves.isEmpty()) return null

        val sortedMoves = orderMoves(legalMoves, state)
        var bestMove: Move? = sortedMoves.firstOrNull()
        var bestScore = Int.MIN_VALUE + 1
        var alpha = Int.MIN_VALUE + 1
        val beta = Int.MAX_VALUE - 1

        for (move in sortedMoves) {
            val nextState = KhmerChessRules.applyMove(state, move, checkLegality = false)
            val score = -alphaBeta(
                state = nextState,
                depth = depth - 1,
                alpha = -beta,
                beta = -alpha,
                perspective = aiColor.opposite()
            )

            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
            alpha = max(alpha, score)
            if (alpha >= beta) break
        }

        return bestMove
    }

    private fun alphaBeta(
        state: BoardState,
        depth: Int,
        alpha: Int,
        beta: Int,
        perspective: PieceColor
    ): Int {
        if (depth <= 0) {
            return evaluateState(state, perspective)
        }

        val legalMoves = KhmerChessRules.getAllLegalMoves(state, state.currentTurn)
        if (legalMoves.isEmpty()) {
            return if (state.inCheck) {
                -100000 - depth
            } else {
                0
            }
        }

        var currentAlpha = alpha
        val sortedMoves = orderMoves(legalMoves, state)

        for (move in sortedMoves) {
            val nextState = KhmerChessRules.applyMove(state, move, checkLegality = false)
            val score = -alphaBeta(
                state = nextState,
                depth = depth - 1,
                alpha = -beta,
                beta = -currentAlpha,
                perspective = perspective.opposite()
            )

            if (score >= beta) {
                return beta
            }
            currentAlpha = max(currentAlpha, score)
        }

        return currentAlpha
    }

    private fun orderMoves(moves: List<Move>, state: BoardState): List<Move> {
        return moves.sortedByDescending { move ->
            var score = 0
            if (move.capturedPiece != null) {
                val victimVal = PIECE_VALUES[move.capturedPiece.type] ?: 0
                val attackerVal = PIECE_VALUES[move.piece.type] ?: 0
                score += 1000 + (victimVal - attackerVal / 10)
            }
            if (move.isPromotion) {
                score += 500
            }
            if (move.isKingLeap || move.isQueenLeap) {
                score += 50
            }
            score += CENTER_TABLE[move.to.row][move.to.col]
            score
        }
    }

    fun evaluateState(state: BoardState, perspective: PieceColor): Int {
        var whiteMaterial = 0
        var blackMaterial = 0
        var whitePositional = 0
        var blackPositional = 0

        for (r in 0..7) {
            for (c in 0..7) {
                val piece = state.board[r][c] ?: continue
                val valBase = PIECE_VALUES[piece.type] ?: 0
                val centerVal = CENTER_TABLE[r][c]

                val advanceBonus = if (piece.type == PieceType.PAWN) {
                    if (piece.color == PieceColor.WHITE) (7 - r) * 15 else r * 15
                } else 0

                if (piece.color == PieceColor.WHITE) {
                    whiteMaterial += valBase
                    whitePositional += centerVal + advanceBonus
                } else {
                    blackMaterial += valBase
                    blackPositional += centerVal + advanceBonus
                }
            }
        }

        val totalWhite = whiteMaterial + whitePositional
        val totalBlack = blackMaterial + blackPositional
        val diff = totalWhite - totalBlack
        return if (perspective == PieceColor.WHITE) diff else -diff
    }
}
