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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.model.AppLanguage
import com.example.data.model.BoardState
import com.example.data.model.Move
import com.example.data.model.PieceColor
import com.example.data.model.Position
import com.example.data.model.TacticsData
import com.example.data.model.TacticsLesson
import com.example.data.model.UserPreferences
import com.example.ui.components.AngkorWarmBackground
import com.example.ui.components.ChessBoardView
import com.example.ui.components.HanumanMechanicsGuideModal
import com.example.ui.components.HanumanTacticsCoachCard
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.JadeEmerald

/**
 * Tactics and Lessons Screen with warm luminous Angkor daylight background.
 */
@Composable
fun TacticsScreen(
    progressList: List<LessonProgressEntity>,
    language: AppLanguage,
    onSelectLesson: (TacticsLesson) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedMap = progressList.associateBy { it.lessonId }

    AngkorWarmBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top bar
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
                        text = AppStrings.get(language, "tactics_title"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = AppStrings.get(language, "tactics_subtitle"),
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(TacticsData.lessons) { lesson ->
                    val progress = completedMap[lesson.id]
                    val isDone = progress?.completed == true

                    val lessonTitle = when (language) {
                        AppLanguage.VIETNAMESE -> lesson.titleVi
                        AppLanguage.KHMER -> lesson.titleKm
                        AppLanguage.FRENCH -> lesson.titleFr
                        AppLanguage.ENGLISH -> lesson.titleEn
                    }
                    val lessonDesc = when (language) {
                        AppLanguage.VIETNAMESE -> lesson.descriptionVi
                        AppLanguage.KHMER -> lesson.descriptionKm
                        AppLanguage.FRENCH -> lesson.descriptionFr
                        AppLanguage.ENGLISH -> lesson.descriptionEn
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(16.dp))
                            .clickable { onSelectLesson(lesson) }
                            .testTag("tactics_item_${lesson.id}"),
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(if (isDone) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isDone) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JadeEmerald)
                                    } else {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AngkorGold)
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = lessonTitle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = lessonDesc,
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B),
                                        maxLines = 2
                                    )
                                }
                            }

                            // Difficulty badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFFBEB))
                                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = lesson.difficulty,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TacticsPlayScreen(
    lesson: TacticsLesson,
    boardState: BoardState,
    selectedPos: Position?,
    legalMoves: List<Move>,
    preferences: UserPreferences,
    isCompleted: Boolean,
    onSquareClick: (Position) -> Unit,
    onHint: () -> Unit,
    onNextLesson: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = preferences.language
    val lessonTitle = when (language) {
        AppLanguage.VIETNAMESE -> lesson.titleVi
        AppLanguage.KHMER -> lesson.titleKm
        AppLanguage.FRENCH -> lesson.titleFr
        AppLanguage.ENGLISH -> lesson.titleEn
    }
    val lessonDesc = when (language) {
        AppLanguage.VIETNAMESE -> lesson.descriptionVi
        AppLanguage.KHMER -> lesson.descriptionKm
        AppLanguage.FRENCH -> lesson.descriptionFr
        AppLanguage.ENGLISH -> lesson.descriptionEn
    }
    val explanation = when (language) {
        AppLanguage.VIETNAMESE -> lesson.explanationVi
        AppLanguage.KHMER -> lesson.explanationKm
        AppLanguage.FRENCH -> lesson.explanationFr
        AppLanguage.ENGLISH -> lesson.explanationEn
    }

    var showHanumanGuide by remember { mutableStateOf(false) }

    AngkorWarmBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top header
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
                        text = lessonTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = if (isCompleted) AppStrings.get(language, "lesson_complete") else "${AppStrings.get(language, "objective")} ${if (boardState.currentTurn == PieceColor.WHITE) AppStrings.get(language, "white") else AppStrings.get(language, "black")}",
                        fontSize = 12.sp,
                        fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCompleted) JadeEmerald else Color(0xFF64748B)
                    )
                }

                IconButton(
                    onClick = { showHanumanGuide = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFFFF))
                        .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = "Hướng dẫn chi tiết",
                        tint = Color(0xFFD97706)
                    )
                }
            }

            // Hanuman Animated Coach Card for Tactics
            HanumanTacticsCoachCard(
                lessonTitle = lessonTitle,
                hintText = if (isCompleted) explanation else lessonDesc,
                isSolved = isCompleted,
                onShowFullGuide = { showHanumanGuide = true }
            )

            // Board View
            ChessBoardView(
                boardState = boardState,
                selectedPos = selectedPos,
                legalMoves = legalMoves,
                boardTheme = preferences.boardTheme,
                pieceStyle = preferences.pieceStyle,
                showCoordinates = preferences.showCoordinates,
                isFlipped = false,
                onSquareClick = onSquareClick,
                modifier = Modifier.fillMaxWidth()
            )

            // Bottom action
            if (isCompleted) {
                Button(
                    onClick = onNextLesson,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    Text(
                        text = AppStrings.get(language, "next_lesson"),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Text(
                    text = "Hãy di chuyển quân theo thế cờ để tiếp tục!",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }

    if (showHanumanGuide) {
        HanumanMechanicsGuideModal(
            language = language,
            onDismiss = { showHanumanGuide = false }
        )
    }
}
