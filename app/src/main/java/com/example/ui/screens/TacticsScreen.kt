package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*
import com.example.ui.components.AngkorWarmBackground
import com.example.ui.screens.KhmerLotusDivider
import com.example.data.model.PieceType
import com.example.data.model.PieceStyle
import com.example.ui.components.PieceRenderer

data class LessonData(val id: String, val piece: PieceType, val title: String, val goal: String)

val LESSONS = listOf(
    LessonData("l1", PieceType.PAWN, "Trey advance", "Push connected fish to the sixth rank to promote into a Neang."),
    LessonData("l2", PieceType.QUEEN, "Neang first leap", "Use the Neang's two-square opening leap to seize the centre."),
    LessonData("l3", PieceType.BISHOP, "Koul wedge", "The elephant walks diagonally or one step forward — build a wedge."),
    LessonData("l4", PieceType.KNIGHT, "Ses double attack", "Fork the Sdech and Touk with a single horse leap."),
    LessonData("l5", PieceType.ROOK, "Touk on open files", "Open a file and let the boat sweep the enemy camp."),
    LessonData("l6", PieceType.KING, "Sdech safety", "Shelter the king behind fish before launching an attack.")
)

@Composable
fun TacticsScreen(
    language: AppLanguage,
    onStartLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var done by remember { mutableStateOf(setOf<String>()) }
    var openLessonId by remember { mutableStateOf<String?>(LESSONS[0].id) }

    AngkorWarmBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Progress Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Gold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = RoyalGoldDark,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.get(language, "tactics_title"),
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${done.size}/${LESSONS.size} ${AppStrings.get(language, "solved")}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(100))
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(if (LESSONS.isNotEmpty()) done.size.toFloat() / LESSONS.size else 0f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(RoyalGold)
                            )
                        }
                    }
                }
            }

            // Divider
            Box(
                modifier = Modifier.fillMaxWidth().height(24.dp),
                contentAlignment = Alignment.Center
            ) {
                KhmerLotusDivider(
                    color = Gold.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth(0.8f).height(24.dp)
                )
            }

            // Section Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = RoyalGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.get(language, "tactics_subtitle").uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.8.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Lessons List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(LESSONS) { lesson ->
                    val isSolved = done.contains(lesson.id)
                    val isExpanded = openLessonId == lesson.id

                    val borderColor = if (isSolved) JadeEmerald.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline
                    val bgColor = if (isSolved) JadeEmerald.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface

                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                                .background(bgColor)
                                .clickable {
                                    openLessonId = if (isExpanded) null else lesson.id
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFF8B5A2B).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                        .background(Color(0xFF8B5A2B).copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    PieceRenderer(
                                        piece = com.example.data.model.Piece(lesson.piece, com.example.data.model.PieceColor.WHITE),
                                        style = PieceStyle.CLASSIC,
                                        size = 30.dp,
                                        modifier = Modifier
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = lesson.title,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = lesson.piece.name,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSolved) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = JadeEmerald,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Flag,
                                            contentDescription = null,
                                            tint = RoyalGoldDark,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = AppStrings.get(language, "objective").uppercase(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.8.sp,
                                            color = RoyalGoldDark
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = lesson.goal,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(RoyalGold)
                                            .clickable {
                                                done = done + lesson.id
                                            }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isSolved) AppStrings.get(language, "lesson_complete") else AppStrings.get(language, "solved"),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = Color.White
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
}
