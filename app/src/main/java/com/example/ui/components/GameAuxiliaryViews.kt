package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.data.model.ChatMessage
import com.example.data.model.GameStatus
import com.example.data.model.Piece
import com.example.data.model.PieceColor
import com.example.data.model.PieceType
import com.example.data.model.QuickEmotes
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.JadeEmerald
import com.example.ui.theme.TerracottaRed

@Composable
fun PlayerBannerView(
    name: String,
    title: String,
    color: PieceColor,
    isCurrentTurn: Boolean,
    timeMillis: Long,
    capturedPieces: List<Piece>,
    isThinking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    val isWhite = color == PieceColor.WHITE

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(if (isCurrentTurn) 6.dp else 2.dp, RoundedCornerShape(14.dp), spotColor = if (isCurrentTurn) AngkorGold.copy(alpha = 0.4f) else Color.Transparent)
            .border(
                1.5.dp,
                if (isCurrentTurn) AngkorGold else Color(0xFFE8DCB8),
                RoundedCornerShape(14.dp)
            )
            .testTag("player_banner_${color.name.lowercase()}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentTurn) Color(0xFFFFF9EE) else Color(0xFFFFFFFF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Player avatar & details
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isWhite) Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF1E6D0)))
                            else Brush.linearGradient(listOf(Color(0xFFB91C1C), Color(0xFF7F1D1D)))
                        )
                        .border(1.5.dp, if (isCurrentTurn) AngkorGold else Color(0xFFD4AF37), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isWhite) "♔" else "♚",
                        fontSize = 20.sp,
                        color = if (isWhite) Color(0xFF1E293B) else Color(0xFFFFFFFF)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        if (isThinking) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "AI Thinking...",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    }
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Captured pieces & Timer
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Mini captured badges
                if (capturedPieces.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(end = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy((-3).dp)
                    ) {
                        capturedPieces.takeLast(5).forEach { p ->
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFAF5EC))
                                    .border(0.5.dp, Color(0xFFD4AF37), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (p.type) {
                                        PieceType.ROOK -> "♖"
                                        PieceType.KNIGHT -> "♘"
                                        PieceType.BISHOP -> "♗"
                                        PieceType.QUEEN -> "♕"
                                        PieceType.PROMOTED_PAWN -> "✦"
                                        PieceType.PAWN -> "♙"
                                        PieceType.KING -> "♔"
                                    },
                                    fontSize = 11.sp,
                                    color = if (p.color == PieceColor.WHITE) Color(0xFF1E293B) else Color(0xFFB91C1C)
                                )
                            }
                        }
                    }
                }

                // Clock Badge (Flat high-contrast pill)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isCurrentTurn) Brush.horizontalGradient(listOf(Color(0xFFFDE68A), Color(0xFFF59E0B)))
                            else Brush.horizontalGradient(listOf(Color(0xFFF1E6D0), Color(0xFFE2D4B7)))
                        )
                        .border(
                            1.dp,
                            if (isCurrentTurn) AngkorGold else Color(0xFFC79E5C),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = timeFormatted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E293B)
                    )
                }
            }
        }
    }
}

@Composable
fun GameOverDialog(
    status: GameStatus,
    winner: PieceColor?,
    playerColor: PieceColor,
    language: AppLanguage,
    eloChange: Int,
    onRematch: () -> Unit,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    val isWin = winner == playerColor
    val isDraw = status == GameStatus.DRAW_BY_COUNT || status == GameStatus.STALEMATE

    val title = when {
        isWin -> AppStrings.get(language, "white_wins")
        isDraw -> AppStrings.get(language, "draw")
        else -> AppStrings.get(language, "black_wins")
    }

    val iconColor = when {
        isWin -> JadeEmerald
        isDraw -> AngkorGold
        else -> TerracottaRed
    }

    val statusDesc = when (status) {
        GameStatus.CHECKMATE -> AppStrings.get(language, "checkmate")
        GameStatus.DRAW_BY_COUNT -> AppStrings.get(language, "draw_by_count")
        GameStatus.STALEMATE -> AppStrings.get(language, "stalemate")
        GameStatus.RESIGNED -> AppStrings.get(language, "resigned")
        GameStatus.TIMEOUT -> AppStrings.get(language, "timeout")
        else -> ""
    }

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .border(2.dp, AngkorGold, RoundedCornerShape(24.dp))
                .testTag("game_over_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Mascot Celebration / Outcome Emblem
                if (isWin) {
                    KhmerMascotVictory(size = 84.dp)
                } else if (isDraw) {
                    KhmerAppLogo(size = 80.dp, showOuterBorder = true)
                } else {
                    KhmerMascotThinking(size = 80.dp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = statusDesc,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )

                if (eloChange != 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (eloChange > 0) Color(0xFFDCFCE7) else Color(0xFFFEE2E2))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (eloChange > 0) "+$eloChange Elo Rating" else "$eloChange Elo Rating",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (eloChange > 0) Color(0xFF16A34A) else Color(0xFFDC2626)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onRematch,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .testTag("game_over_rematch_btn")
                    ) {
                        Text(
                            text = AppStrings.get(language, "rematch"),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onReplay,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .testTag("game_over_replay_btn")
                    ) {
                        Text(
                            text = AppStrings.get(language, "history_replays"),
                            color = Color(0xFF1E293B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .testTag("game_over_home_btn")
                    ) {
                        Text(
                            text = AppStrings.get(language, "return_home"),
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveChatDialog(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onSendEmote: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(20.dp))
                .testTag("live_chat_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = AngkorGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Live Chat & Emotes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                    }
                }

                // Messages list
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(messages) { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF5EEDC))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Column {
                                Text(
                                    text = msg.senderName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AngkorGold
                                )
                                Text(
                                    text = msg.message,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }

                // Quick Emotes Row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(QuickEmotes.list) { emote ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF1E6D0))
                                .clickable { onSendEmote(emote) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = emote, fontSize = 12.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium)
                        }
                    }
                }

                // Input bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Nhập tin nhắn...", fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                onSendMessage(textInput.trim())
                                textInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFD97706))
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    }
}
