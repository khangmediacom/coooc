package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Move
import com.example.data.model.PieceColor
import com.example.data.model.PieceType
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AngkorGold

/**
 * Modern interactive Move History List & Review Controller for Khmer Chess.
 * Allows scrolling through past moves, tapping any move to inspect board state,
 * and stepping through moves forward/backward with real-time feedback.
 */
@Composable
fun MoveHistoryView(
    moves: List<Move>,
    reviewIndex: Int?,
    language: AppLanguage,
    onSelectMoveIndex: (Int?) -> Unit,
    onStepMove: (Int) -> Unit,
    onJumpToStart: () -> Unit,
    onJumpToLive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val totalMoves = moves.size
    val isReviewing = reviewIndex != null
    val currentSelectedIdx = reviewIndex ?: totalMoves

    // Auto-scroll to current move when index changes
    LaunchedEffect(currentSelectedIdx) {
        if (totalMoves > 0) {
            val targetScroll = currentSelectedIdx.coerceIn(0, totalMoves)
            listState.animateScrollToItem(targetScroll)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        // 1. Move History Navigation Bar & Quick Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Title / Status badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = if (isReviewing) Color(0xFFD97706) else Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isReviewing) {
                        "${AppStrings.get(language, "review_mode")}: $currentSelectedIdx/$totalMoves"
                    } else {
                        "${AppStrings.get(language, "move_history")} ($totalMoves)"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isReviewing) Color(0xFFB45309) else Color(0xFF1E293B)
                )
            }

            // Right: Stepper Buttons (|<, <, >, >|)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Jump to Start
                IconButton(
                    onClick = onJumpToStart,
                    enabled = currentSelectedIdx > 0,
                    modifier = Modifier.size(30.dp).testTag("history_start_btn")
                ) {
                    Icon(
                        Icons.Default.FirstPage,
                        contentDescription = "Start",
                        tint = if (currentSelectedIdx > 0) Color(0xFF475569) else Color(0xFFCBD5E1),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Step Back
                IconButton(
                    onClick = { onStepMove(-1) },
                    enabled = currentSelectedIdx > 0,
                    modifier = Modifier.size(30.dp).testTag("history_prev_btn")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous",
                        tint = if (currentSelectedIdx > 0) Color(0xFF475569) else Color(0xFFCBD5E1),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Step Forward
                IconButton(
                    onClick = { onStepMove(1) },
                    enabled = currentSelectedIdx < totalMoves,
                    modifier = Modifier.size(30.dp).testTag("history_next_btn")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = if (currentSelectedIdx < totalMoves) Color(0xFF475569) else Color(0xFFCBD5E1),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Jump to Live
                IconButton(
                    onClick = onJumpToLive,
                    enabled = isReviewing,
                    modifier = Modifier.size(30.dp).testTag("history_live_btn")
                ) {
                    Icon(
                        Icons.Default.LastPage,
                        contentDescription = "Live",
                        tint = if (isReviewing) Color(0xFFD97706) else Color(0xFFCBD5E1),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 2. Horizontal Scrollable Move Chips
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Initial Starting Position Chip (Move 0)
            item {
                val isSelected = currentSelectedIdx == 0
                MoveChip(
                    label = AppStrings.get(language, "start_move"),
                    subLabel = "Init",
                    isSelected = isSelected,
                    isWhite = true,
                    onClick = { onSelectMoveIndex(0) }
                )
            }

            // List of moves with turn numbers (e.g. 1. White, 1... Black, 2. White...)
            itemsIndexed(moves) { index, move ->
                val moveNumber = (index / 2) + 1
                val isWhiteMove = move.piece.color == PieceColor.WHITE
                val turnPrefix = if (isWhiteMove) "$moveNumber." else "$moveNumber..."
                val moveIndex = index + 1
                val isSelected = currentSelectedIdx == moveIndex

                val pieceIcon = when (move.piece.type) {
                    PieceType.KING -> "♔"
                    PieceType.QUEEN -> "♕"
                    PieceType.BISHOP -> "♗"
                    PieceType.KNIGHT -> "♘"
                    PieceType.ROOK -> "♖"
                    PieceType.PAWN -> "♙"
                    PieceType.PROMOTED_PAWN -> "✪"
                }

                val moveLabel = "$turnPrefix $pieceIcon ${move.notation.ifEmpty { move.to.toNotation() }}"

                MoveChip(
                    label = moveLabel,
                    subLabel = if (move.capturedPiece != null) "⚔" else if (move.isPromotion) "★" else null,
                    isSelected = isSelected,
                    isWhite = isWhiteMove,
                    onClick = { onSelectMoveIndex(if (moveIndex == totalMoves) null else moveIndex) }
                )
            }

            // Live Match Target Indicator Chip
            item {
                val isSelected = !isReviewing
                MoveChip(
                    label = "● " + AppStrings.get(language, "current_move"),
                    subLabel = null,
                    isSelected = isSelected,
                    isWhite = false,
                    isLiveTag = true,
                    onClick = { onSelectMoveIndex(null) }
                )
            }
        }

        // 3. Floating Amber Banner when in Review Mode with "Resume Match" button
        AnimatedVisibility(
            visible = isReviewing,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "👀 ",
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${AppStrings.get(language, "review_mode")}: $currentSelectedIdx / $totalMoves",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }

                    Button(
                        onClick = onJumpToLive,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(26.dp).testTag("resume_match_btn")
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = AppStrings.get(language, "resume_game"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MoveChip(
    label: String,
    subLabel: String?,
    isSelected: Boolean,
    isWhite: Boolean,
    isLiveTag: Boolean = false,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected -> AngkorGold
        isLiveTag -> Color(0xFFF0FDF4)
        isWhite -> Color(0xFFFAFAFA)
        else -> Color(0xFFF1F5F9)
    }

    val textColor = when {
        isSelected -> Color(0xFF78350F)
        isLiveTag -> Color(0xFF16A34A)
        else -> Color(0xFF1E293B)
    }

    val borderColor = when {
        isSelected -> Color(0xFFB45309)
        isLiveTag -> Color(0xFF86EFAC)
        else -> Color(0xFFCBD5E1)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
            if (subLabel != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = subLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFF92400E) else Color(0xFFDC2626)
                )
            }
        }
    }
}
