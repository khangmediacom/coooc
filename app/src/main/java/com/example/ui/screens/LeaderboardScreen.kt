package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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

data class PlayerData(val name: String, val rating: Int, val wins: Int, val flag: String)

val PLAYERS = listOf(
    PlayerData("Sokha Chan", 2412, 318, "\uD83C\uDDF0\uD83C\uDDED"), // 🇰🇭
    PlayerData("Vibol Prak", 2288, 274, "\uD83C\uDDF0\uD83C\uDDED"), // 🇰🇭
    PlayerData("Minh Nguyen", 2201, 240, "\uD83C\uDDFB\uD83C\uDDF3"), // 🇻🇳
    PlayerData("Dara Meas", 2145, 231, "\uD83C\uDDF0\uD83C\uDDED"), // 🇰🇭
    PlayerData("Claire Rey", 2077, 198, "\uD83C\uDDEB\uD83C\uDDF7"), // 🇫🇷
    PlayerData("Rithy Sok", 2010, 187, "\uD83C\uDDF0\uD83C\uDDED"), // 🇰🇭
    PlayerData("Alex Grant", 1954, 172, "\uD83C\uDDEC\uD83C\uDDE7"), // 🇬🇧
    PlayerData("Bopha Ly", 1902, 160, "\uD83C\uDDF0\uD83C\uDDED")  // 🇰🇭
)

val TABS = listOf("tab_global", "tab_weekly", "tab_friends")

@Composable
fun LeaderboardScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(TABS[0]) }

    val currentList = when (selectedTab) {
        "tab_friends" -> PLAYERS.take(3)
        "tab_weekly" -> PLAYERS.drop(2)
        else -> PLAYERS
    }

    AngkorWarmBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TABS.forEach { tabKey ->
                    val isSelected = selectedTab == tabKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Gold.copy(alpha = 0.2f) else Color.Transparent)
                            .clickable { selectedTab = tabKey }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = AppStrings.get(language, tabKey),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) RoyalGoldDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Section Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = RoyalGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.get(language, "leaderboard").uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.8.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(currentList) { index, player ->
                    val medalColor = when (index) {
                        0 -> RoyalGold
                        1 -> Color.Gray
                        2 -> Color(0xFF8B5A2B)
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Rank Badge
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            if (index < 3) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = medalColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = (index + 1).toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Player Info
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${player.flag} ${player.name}",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                            Text(
                                text = "${player.wins} ${AppStrings.get(language, "win")}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Rating Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100))
                                .border(1.dp, JadeEmerald.copy(alpha = 0.4f), RoundedCornerShape(100))
                                .background(JadeEmerald.copy(alpha = 0.1f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = player.rating.toString(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = JadeEmerald
                            )
                        }
                    }
                }
            }
        }
    }
}
