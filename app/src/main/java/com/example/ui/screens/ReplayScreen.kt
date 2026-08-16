package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.MatchHistoryEntity
import com.example.data.model.AppLanguage
import com.example.data.model.BoardState
import com.example.data.model.Move
import com.example.data.model.UserPreferences
import com.example.ui.components.AngkorWarmBackground
import com.example.ui.components.ChessBoardView
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.JadeEmerald
import com.example.ui.theme.TerracottaRed
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Match History Screen with warm Angkor background.
 */
@Composable
fun MatchHistoryScreen(
    matches: List<MatchHistoryEntity>,
    language: AppLanguage,
    onSelectMatch: (MatchHistoryEntity) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.ENGLISH)

    AngkorWarmBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFFFF))
                        .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = AppStrings.get(language, "match_history_title"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "${matches.size} matches",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (matches.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = AppStrings.get(language, "no_matches_yet"),
                            color = Color(0xFF64748B),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(matches) { match ->
                        val isWin = match.winner == match.playerColor
                        val isDraw = match.winner == "DRAW"
                        val outcomeColor = when {
                            isWin -> JadeEmerald
                            isDraw -> AngkorGold
                            else -> TerracottaRed
                        }
                        val outcomeLabel = when {
                            isWin -> AppStrings.get(language, "win")
                            isDraw -> AppStrings.get(language, "draw")
                            else -> AppStrings.get(language, "loss")
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(16.dp))
                                .clickable { onSelectMatch(match) }
                                .testTag("match_item_${match.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(outcomeColor.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = outcomeLabel,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = outcomeColor
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "vs ${match.opponentName}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${match.totalMoves} moves • ${dateFormat.format(Date(match.timestamp))}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = if (match.eloChange >= 0) "+${match.eloChange} Elo" else "${match.eloChange} Elo",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (match.eloChange >= 0) JadeEmerald else TerracottaRed
                                    )
                                    Text(
                                        text = "${AppStrings.get(language, "replay_title")} ▶",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFD97706)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReplayViewerScreen(
    match: MatchHistoryEntity,
    boardState: BoardState,
    replayMoves: List<Move>,
    currentIndex: Int,
    preferences: UserPreferences,
    onSetIndex: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = preferences.language
    var isAutoPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(isAutoPlaying, currentIndex) {
        if (isAutoPlaying && currentIndex < replayMoves.size) {
            delay(1000)
            onSetIndex(currentIndex + 1)
        } else if (currentIndex >= replayMoves.size) {
            isAutoPlaying = false
        }
    }

    AngkorWarmBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFFFF))
                        .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = AppStrings.get(language, "replay_title"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "$currentIndex / ${replayMoves.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
                    )
                }

                Box(modifier = Modifier.size(40.dp))
            }

            // Board
            ChessBoardView(
                boardState = boardState,
                selectedPos = null,
                legalMoves = emptyList(),
                boardTheme = preferences.boardTheme,
                pieceStyle = preferences.pieceStyle,
                showCoordinates = true,
                isFlipped = false,
                onSquareClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            // Move notation badge
            val lastMove = boardState.lastMove
            Text(
                text = if (lastMove != null) "${AppStrings.get(language, "last_move")} ${lastMove.notation}" else AppStrings.get(language, "start_pos"),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFD97706)
            )

            // Replay Navigation Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFFFFF))
                    .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // First
                IconButton(onClick = { onSetIndex(0); isAutoPlaying = false }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "First", tint = Color(0xFF1E293B))
                }
                // Step Back
                IconButton(onClick = { onSetIndex((currentIndex - 1).coerceAtLeast(0)); isAutoPlaying = false }) {
                    Icon(Icons.Default.FastRewind, contentDescription = "Prev", tint = Color(0xFF1E293B))
                }
                // Auto Play / Pause
                IconButton(
                    onClick = { isAutoPlaying = !isAutoPlaying },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD97706))
                ) {
                    Icon(
                        imageVector = if (isAutoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }
                // Step Forward
                IconButton(onClick = { onSetIndex((currentIndex + 1).coerceAtMost(replayMoves.size)); isAutoPlaying = false }) {
                    Icon(Icons.Default.FastForward, contentDescription = "Next", tint = Color(0xFF1E293B))
                }
                // Last
                IconButton(onClick = { onSetIndex(replayMoves.size); isAutoPlaying = false }) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Last", tint = Color(0xFF1E293B))
                }
            }
        }
    }
}
