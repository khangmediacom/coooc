package com.example.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.R
import com.example.data.model.BoardState
import com.example.data.model.BoardTheme
import com.example.data.model.Move
import com.example.data.model.Piece
import com.example.data.model.PieceStyle
import com.example.data.model.PieceType
import com.example.data.model.Position
import com.example.ui.theme.AngkorGold

data class BoardPieceItem(
    val piece: Piece,
    val row: Int,
    val col: Int
)

@Composable
fun ChessBoardView(
    boardState: BoardState,
    selectedPos: Position?,
    legalMoves: List<Move>,
    boardTheme: BoardTheme = BoardTheme.CLASSIC,
    pieceStyle: PieceStyle = PieceStyle.CLASSIC,
    showCoordinates: Boolean = true,
    isFlipped: Boolean = false,
    onSquareClick: (Position) -> Unit,
    modifier: Modifier = Modifier
) {
    val lightSquare = Color(boardTheme.lightSquareColorHex)
    val darkSquare = Color(boardTheme.darkSquareColorHex)
    val borderColor = Color(boardTheme.borderHex)

    // Breathing pulse for legal moves & check
    val infiniteTransition = rememberInfiniteTransition(label = "board_motion")
    val pulseScale = 1f

    val checkGlowAlpha by infiniteTransition.animateColor(
        initialValue = Color(0x33EF4444),
        targetValue = Color(0x99EF4444),
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "check_glow"
    )

    // Extract active pieces list for smooth animated movement layer
    val activePieces = remember(boardState.board) {
        val list = mutableListOf<BoardPieceItem>()
        for (r in 0..7) {
            for (c in 0..7) {
                val piece = boardState.board[r][c]
                if (piece != null) {
                    list.add(BoardPieceItem(piece, r, c))
                }
            }
        }
        list
    }

    val goldBorderBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFFF3D98A),
                Color(0xFFD4AF37),
                Color(0xFF996515),
                Color(0xFFE8CA72),
                Color(0xFFB37E1C),
                Color(0xFFF3D98A)
            )
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(26.dp),
                spotColor = Color(0x4092400E),
                ambientColor = Color(0x22000000)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFFBA8C59))
            .border(
                width = 5.dp,
                brush = goldBorderBrush,
                shape = RoundedCornerShape(26.dp)
            )
            .padding(5.dp)
            .clip(RoundedCornerShape(21.dp))
            .testTag("chess_board")
    ) {
        val squareSize = minOf(maxWidth, maxHeight) / 8

        // Layer 1: Board Grid, Square Highlights, Target Indicators & Clicks
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(21.dp))
        ) {
            for (displayRow in 0..7) {
                val actualRow = if (isFlipped) 7 - displayRow else displayRow

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for (displayCol in 0..7) {
                        val actualCol = if (isFlipped) 7 - displayCol else displayCol
                        val pos = Position(actualRow, actualCol)
                        val piece = boardState.board[actualRow][actualCol]

                        val isSquareLight = (actualRow + actualCol) % 2 == 0
                        val squareBg = if (isSquareLight) lightSquare else darkSquare

                        val isSelected = selectedPos == pos
                        val isLegalTarget = legalMoves.any { it.to == pos }
                        val isLastMoveFrom = boardState.lastMove?.from == pos
                        val isLastMoveTo = boardState.lastMove?.to == pos
                        val isCheckedKing = boardState.inCheck && piece?.type == PieceType.KING && piece.color == boardState.currentTurn

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(squareBg)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = AngkorGold.copy(alpha = 0.4f)),
                                    onClick = { onSquareClick(pos) }
                                )
                                .testTag("square_${pos.row}_${pos.col}"),
                            contentAlignment = Alignment.Center
                        ) {
                            // Subtle Cell Depth Grid lines
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRect(
                                    color = if (isSquareLight) Color(0x10FFFFFF) else Color(0x18000000),
                                    topLeft = Offset.Zero,
                                    size = size
                                )
                            }

                            // 1. Last Move Trail Highlight
                            if (isLastMoveFrom || isLastMoveTo) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0x35EAB308))
                                )
                            }

                            // 2. Checked King Pulsing Glow
                            if (isCheckedKing) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .drawBehind { drawRect(checkGlowAlpha) }
                                )
                            }

                            // 3. Active Selected Square Aura
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0x40F59E0B))
                                        .border(2.5.dp, AngkorGold, RoundedCornerShape(4.dp))
                                )
                            }

                            // 4. Modern Flat Target Dots & Capture Rings with Motion
                            if (isLegalTarget) {
                                val isCapture = piece != null
                                Canvas(modifier = Modifier.fillMaxSize().zIndex(2f)) {
                                    val center = Offset(size.width / 2, size.height / 2)
                                    if (isCapture) {
                                        // Radiant capture ring
                                        drawCircle(
                                            color = Color(0xEEEF4444),
                                            radius = (size.width * 0.43f) * pulseScale.coerceIn(0.95f, 1.05f),
                                            center = center,
                                            style = Stroke(width = 3.2.dp.toPx())
                                        )
                                        drawCircle(
                                            color = Color(0x33EF4444),
                                            radius = size.width * 0.43f,
                                            center = center
                                        )
                                    } else {
                                        // Modern Emerald Target Pip with soft halo
                                        drawCircle(
                                            color = Color(0x3310B981),
                                            radius = (size.width * 0.22f) * pulseScale,
                                            center = center
                                        )
                                        drawCircle(
                                            color = Color(0xFF10B981),
                                            radius = size.width * 0.14f,
                                            center = center
                                        )
                                    }
                                }
                            }

                            // 5. Coordinates (Rank numbers & File letters)
                            if (showCoordinates) {
                                if (displayCol == 0) {
                                    val rankLabel = (8 - actualRow).toString()
                                    Text(
                                        text = rankLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSquareLight) darkSquare.copy(alpha = 0.85f) else lightSquare.copy(alpha = 0.85f),
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(start = 2.5.dp, top = 1.5.dp)
                                    )
                                }
                                if (displayRow == 7) {
                                    val fileLabel = ('a' + actualCol).toString()
                                    Text(
                                        text = fileLabel,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSquareLight) darkSquare.copy(alpha = 0.85f) else lightSquare.copy(alpha = 0.85f),
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(end = 2.5.dp, bottom = 1.5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Layer 2: Subtle Transition Animations for Moving Pieces
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(21.dp))
        ) {
            for (pieceItem in activePieces) {
                key(pieceItem.piece.id) {
                    val isSelected = selectedPos == Position(pieceItem.row, pieceItem.col)
                    AnimatedPiece(
                        pieceItem = pieceItem,
                        squareSize = squareSize,
                        isSelected = isSelected,
                        isFlipped = isFlipped,
                        pieceStyle = pieceStyle,
                        onPieceClick = {
                            onSquareClick(Position(pieceItem.row, pieceItem.col))
                        }
                    )
                }
            }
        }
    }
}

/**
 * Renders a single piece with smooth spring glide animations across the grid,
 * elevation shadow during movement, and reactive touch selection.
 */
@Composable
private fun AnimatedPiece(
    pieceItem: BoardPieceItem,
    squareSize: Dp,
    isSelected: Boolean,
    isFlipped: Boolean,
    pieceStyle: PieceStyle,
    onPieceClick: () -> Unit
) {
    val displayRow = if (isFlipped) 7 - pieceItem.row else pieceItem.row
    val displayCol = if (isFlipped) 7 - pieceItem.col else pieceItem.col

    val targetOffsetX = squareSize * displayCol
    val targetOffsetY = squareSize * displayRow

    // Smooth physics-based spring glide for piece movement across squares
    val animatedOffsetX by animateDpAsState(
        targetValue = targetOffsetX,
        animationSpec = spring(
            dampingRatio = 0.82f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "piece_anim_x_${pieceItem.piece.id}"
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = targetOffsetY,
        animationSpec = spring(
            dampingRatio = 0.82f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "piece_anim_y_${pieceItem.piece.id}"
    )

    val isGliding = (animatedOffsetX != targetOffsetX || animatedOffsetY != targetOffsetY)

    // Subtle scale and elevation lift when moving or selected
    val pieceScale by animateFloatAsState(
        targetValue = when {
            isSelected -> 1.14f
            isGliding -> 1.08f
            else -> 1.0f
        },
        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = Spring.StiffnessMedium
        ),
        label = "piece_scale_${pieceItem.piece.id}"
    )

    val shadowElevation by animateDpAsState(
        targetValue = when {
            isSelected -> 8.dp
            isGliding -> 6.dp
            else -> 0.dp
        },
        animationSpec = tween(durationMillis = 200),
        label = "piece_shadow_${pieceItem.piece.id}"
    )

    Box(
        modifier = Modifier
            .offset(x = animatedOffsetX, y = animatedOffsetY)
            .size(squareSize)
            .zIndex(if (isSelected || isGliding) 5f else 3f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onPieceClick
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        PieceView(
            piece = pieceItem.piece,
            style = pieceStyle,
            isSelected = isSelected,
            modifier = Modifier
                .fillMaxSize()
                .scale(pieceScale)
                .then(
                    if (shadowElevation > 0.dp) {
                        Modifier.shadow(
                            elevation = shadowElevation,
                            shape = CircleShape,
                            spotColor = Color(0x77000000)
                        )
                    } else Modifier
                )
        )
    }
}
