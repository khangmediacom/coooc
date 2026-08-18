package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AIDifficulty
import com.example.data.model.AppLanguage
import com.example.data.model.PieceColor
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*
import com.example.ui.components.AppShellTopBar
import com.example.ui.components.AngkorWarmBackground

@Composable
fun PlayScreen(
    language: AppLanguage,
    onStartAi: (AIDifficulty, PieceColor) -> Unit,
    onStartLocal2P: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mode by remember { mutableStateOf("ai") }
    var depth by remember { mutableStateOf(2) }

    AngkorWarmBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            
            // Mode Selectors
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ModeCard(
                    title = AppStrings.get(language, "play_vs_ai"),
                    desc = AppStrings.get(language, "play_vs_ai_desc"),
                    icon = Icons.Default.SmartToy,
                    isSelected = mode == "ai",
                    onClick = { mode = "ai" }
                )
                
                ModeCard(
                    title = AppStrings.get(language, "local_2p"),
                    desc = AppStrings.get(language, "local_2p_desc"),
                    icon = Icons.Default.People,
                    isSelected = mode == "local",
                    onClick = { mode = "local" }
                )
            }

            if (mode == "ai") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = RoyalGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get(language, "choose_difficulty").uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.8.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Difficulty grid
                val levels = listOf(
                    Pair(1, "novice"),
                    Pair(2, "apprentice"),
                    Pair(3, "master"),
                    Pair(4, "grandmaster")
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DifficultyButton(
                            title = AppStrings.get(language, levels[0].second),
                            isSelected = depth == levels[0].first,
                            onClick = { depth = levels[0].first },
                            modifier = Modifier.weight(1f)
                        )
                        DifficultyButton(
                            title = AppStrings.get(language, levels[1].second),
                            isSelected = depth == levels[1].first,
                            onClick = { depth = levels[1].first },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DifficultyButton(
                            title = AppStrings.get(language, levels[2].second),
                            isSelected = depth == levels[2].first,
                            onClick = { depth = levels[2].first },
                            modifier = Modifier.weight(1f)
                        )
                        DifficultyButton(
                            title = AppStrings.get(language, levels[3].second),
                            isSelected = depth == levels[3].first,
                            onClick = { depth = levels[3].first },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Start Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = RoyalGold)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RoyalGold) // Simple blue in Lovable, but we use Gold to match theme
                    .clickable {
                        if (mode == "ai") {
                            val diff = when (depth) {
                                1 -> AIDifficulty.BEGINNER
                                2 -> AIDifficulty.AMATEUR
                                3 -> AIDifficulty.MASTER
                                4 -> AIDifficulty.GRANDMASTER
                                else -> AIDifficulty.AMATEUR
                            }
                            onStartAi(diff, PieceColor.WHITE)
                        } else {
                            onStartLocal2P()
                        }
                    }
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = AppStrings.get(language, "start_match"),
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ModeCard(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Gold else MaterialTheme.colorScheme.outline
    val bgColor = if (isSelected) Gold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
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
                    .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = RoyalGoldDark,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DifficultyButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) Gold else MaterialTheme.colorScheme.outline
    val bgColor = if (isSelected) Gold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
    val textColor = if (isSelected) RoyalGoldDark else MaterialTheme.colorScheme.onSurface
    val scale = if (isSelected) 1.02f else 1.0f

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = textColor
        )
    }
}
