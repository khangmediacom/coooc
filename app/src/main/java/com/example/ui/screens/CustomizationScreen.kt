package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.VolumeUp
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
import com.example.data.model.UserPreferences
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*
import com.example.ui.components.AngkorWarmBackground
import com.example.ui.screens.KhmerLotusDivider

@Composable
fun CustomizationScreen(
    preferences: UserPreferences,
    onLanguageChange: (AppLanguage) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var darkMode by remember { mutableStateOf(false) }
    var animations by remember { mutableStateOf(true) }

    AngkorWarmBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Language Section
            item {
                SectionTitle(
                    icon = Icons.Default.Language,
                    title = AppStrings.get(preferences.language, "language_section").uppercase()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppLanguage.values().forEach { lang ->
                        val isSelected = preferences.language == lang
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) Gold else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(16.dp)
                                )
                                .background(if (isSelected) Gold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface)
                                .clickable { onLanguageChange(lang) }
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.KHMER) "\uD83C\uDDF0\uD83C\uDDED" else "\uD83C\uDDEC\uD83C\uDDE7", // Flag
                                    fontSize = 20.sp
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (lang == AppLanguage.KHMER) "ភាសាខ្មែរ" else "English",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = if (lang == AppLanguage.KHMER) "Khmer" else "English",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = RoyalGoldDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Divider
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KhmerLotusDivider(
                        color = Gold.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(0.8f).height(24.dp)
                    )
                }
            }

            // Preferences Section
            item {
                SectionTitle(
                    icon = Icons.Default.AutoAwesome,
                    title = AppStrings.get(preferences.language, "preferences").uppercase()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PreferenceToggle(
                        icon = Icons.Default.DarkMode,
                        title = AppStrings.get(preferences.language, "dark_mode"),
                        isChecked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )
                    PreferenceToggle(
                        icon = Icons.Default.VolumeUp,
                        title = AppStrings.get(preferences.language, "sound_effects"),
                        isChecked = preferences.soundEnabled,
                        onCheckedChange = onToggleSound
                    )
                    PreferenceToggle(
                        icon = Icons.Default.AutoAwesome,
                        title = AppStrings.get(preferences.language, "animations"),
                        isChecked = animations,
                        onCheckedChange = { animations = it }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RoyalGold,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.8.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PreferenceToggle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onCheckedChange(!isChecked) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RoyalGoldDark,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.surface,
                checkedTrackColor = Gold,
                checkedBorderColor = Gold,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}
