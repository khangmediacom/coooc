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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.AppLanguage
import com.example.data.model.BoardTheme
import com.example.data.model.PieceStyle
import com.example.data.model.UserPreferences
import com.example.ui.components.AngkorWarmBackground
import com.example.ui.components.GoogleLogo
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.JadeEmerald

/**
 * Customization / Settings Screen with complete localization, warm daylight Angkor theme, and Google Account integration.
 */
@Composable
fun CustomizationScreen(
    preferences: UserPreferences,
    userProfile: UserProfileEntity,
    onSelectTheme: (BoardTheme) -> Unit,
    onSelectPieceStyle: (PieceStyle) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onUpdateSoundVolume: (Float) -> Unit = {},
    onToggleMusic: (Boolean) -> Unit = {},
    onUpdateMusicVolume: (Float) -> Unit = {},
    onTestSound: (String) -> Unit = {},
    onUpdateUsername: (String) -> Unit,
    onPromptSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = preferences.language
    var showEditNameDialog by remember { mutableStateOf(false) }
    var editNameText by remember { mutableStateOf(userProfile.username) }

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
                Text(
                    text = AppStrings.get(language, "settings"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. GOOGLE ACCOUNT & PROFILE CARD
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(18.dp))
                            .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = AppStrings.get(language, "account").uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )

                                if (userProfile.isLoggedIn) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFDCFCE7))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = AppStrings.get(language, "sync_status"),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = JadeEmerald
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (userProfile.isLoggedIn) {
                                // Logged-in Google Account View
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFEF3C7))
                                                .border(1.5.dp, AngkorGold, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "👑", fontSize = 22.sp)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = userProfile.username,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1E293B)
                                            )
                                            Text(
                                                text = userProfile.email.ifEmpty { "user@makruk.kh" },
                                                fontSize = 12.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Row {
                                        IconButton(
                                            onClick = {
                                                editNameText = userProfile.username
                                                showEditNameDialog = true
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color(0xFFFFFBEB))
                                                .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit name", tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        IconButton(
                                            onClick = onSignOut,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color(0xFFFEF2F2))
                                                .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(10.dp))
                                        ) {
                                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Sign Out", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Detailed Stats Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Rating", fontSize = 11.sp, color = Color(0xFF64748B))
                                        Text(text = "${userProfile.elo}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Level", fontSize = 11.sp, color = Color(0xFF64748B))
                                        Text(text = "Lv. ${userProfile.level}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Matches", fontSize = 11.sp, color = Color(0xFF64748B))
                                        Text(text = "${userProfile.totalMatches}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Win Rate", fontSize = 11.sp, color = Color(0xFF64748B))
                                        val winRate = if (userProfile.totalMatches > 0) (userProfile.wins * 100 / userProfile.totalMatches) else 60
                                        Text(text = "$winRate%", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = JadeEmerald)
                                    }
                                }
                            } else {
                                // Guest Profile View
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFF1E6D0))
                                            .border(1.dp, Color(0xFFD4C3A3), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(24.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = AppStrings.get(language, "guest_mode"),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                        Text(
                                            text = AppStrings.get(language, "guest_stats_hidden"),
                                            fontSize = 12.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = onPromptSignIn,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .testTag("settings_google_signin_btn"),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFBEB)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        GoogleLogo(modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = AppStrings.get(language, "sign_in_google"),
                                            color = Color(0xFF1E293B),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. BOARD THEMES
                item {
                    Text(
                        text = AppStrings.get(language, "board_theme"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BoardTheme.entries.forEach { theme ->
                            val isSelected = preferences.boardTheme == theme
                            val themeTitle = when (language) {
                                AppLanguage.VIETNAMESE -> theme.titleVi
                                AppLanguage.KHMER -> theme.titleKm
                                AppLanguage.FRENCH -> theme.titleFr
                                AppLanguage.ENGLISH -> theme.titleEn
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(2.dp, RoundedCornerShape(14.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) AngkorGold else Color(0xFFE8DCB8),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { onSelectTheme(theme) }
                                    .testTag("theme_item_${theme.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFFFFBEB) else Color(0xFFFFFFFF)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // 2x2 preview
                                        Column(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, Color(theme.borderHex), RoundedCornerShape(8.dp))
                                        ) {
                                            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                                                Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(theme.lightSquareColorHex)))
                                                Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(theme.darkSquareColorHex)))
                                            }
                                            Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                                                Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(theme.darkSquareColorHex)))
                                                Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color(theme.lightSquareColorHex)))
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = themeTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = Color(0xFF1E293B)
                                            )
                                            Text(
                                                text = theme.description,
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color(0xFFD97706),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. PIECE STYLES
                item {
                    Text(
                        text = AppStrings.get(language, "piece_style"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PieceStyle.entries.forEach { style ->
                            val isSelected = preferences.pieceStyle == style
                            val styleTitle = when (language) {
                                AppLanguage.VIETNAMESE -> style.titleVi
                                AppLanguage.KHMER -> style.titleKm
                                AppLanguage.FRENCH -> style.titleFr
                                AppLanguage.ENGLISH -> style.titleEn
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(2.dp, RoundedCornerShape(14.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) AngkorGold else Color(0xFFE8DCB8),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { onSelectPieceStyle(style) }
                                    .testTag("piece_style_${style.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFFFFBEB) else Color(0xFFFFFFFF)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                    ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = styleTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = Color(0xFF1E293B)
                                            )
                                            Text(
                                                text = style.description,
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }

                                        if (isSelected) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = Color(0xFFD97706),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Live Piece Preview Row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF8FAFC))
                                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        listOf(
                                            com.example.data.model.Piece(com.example.data.model.PieceType.KING, com.example.data.model.PieceColor.WHITE),
                                            com.example.data.model.Piece(com.example.data.model.PieceType.QUEEN, com.example.data.model.PieceColor.BLACK),
                                            com.example.data.model.Piece(com.example.data.model.PieceType.BISHOP, com.example.data.model.PieceColor.WHITE),
                                            com.example.data.model.Piece(com.example.data.model.PieceType.KNIGHT, com.example.data.model.PieceColor.BLACK),
                                            com.example.data.model.Piece(com.example.data.model.PieceType.ROOK, com.example.data.model.PieceColor.WHITE),
                                            com.example.data.model.Piece(com.example.data.model.PieceType.PAWN, com.example.data.model.PieceColor.BLACK),
                                            com.example.data.model.Piece(com.example.data.model.PieceType.PROMOTED_PAWN, com.example.data.model.PieceColor.WHITE)
                                        ).forEach { previewPiece ->
                                            com.example.ui.components.PieceView(
                                                piece = previewPiece,
                                                style = style,
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. LANGUAGE SELECTOR
                item {
                    Text(
                        text = AppStrings.get(language, "language_section"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            val isSelected = preferences.language == lang
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(2.dp, RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) AngkorGold else Color(0xFFE8DCB8),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onSelectLanguage(lang) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFFFFBEB) else Color(0xFFFFFFFF)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = lang.flag, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = lang.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color(0xFFD97706) else Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. AUDIO & VISUAL PREFERENCES
                item {
                    Text(
                        text = AppStrings.get(language, "preferences"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // SFX Sound Toggle & Volume
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = AngkorGold)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = AppStrings.get(language, "sound_effects"), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                        Text(text = "${(preferences.soundVolume * 100).toInt()}%", fontSize = 11.sp, color = Color(0xFF64748B))
                                    }
                                }
                                Switch(
                                    checked = preferences.soundEnabled,
                                    onCheckedChange = {
                                        onToggleSound(it)
                                        if (it) {
                                            onTestSound("move")
                                        }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AngkorGold
                                    ),
                                    modifier = Modifier.testTag("customization_sound_switch")
                                )
                            }

                            if (preferences.soundEnabled) {
                                Slider(
                                    value = preferences.soundVolume,
                                    onValueChange = onUpdateSoundVolume,
                                    onValueChangeFinished = {
                                        onTestSound("move")
                                    },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = AngkorGold,
                                        activeTrackColor = AngkorGold,
                                        inactiveTrackColor = Color(0xFFE2E8F0)
                                    ),
                                    modifier = Modifier.fillMaxWidth().testTag("customization_sound_slider")
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Traditional Khmer BGM Music Toggle & Volume
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = JadeEmerald)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = AppStrings.get(language, "bgm_music"), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                        Text(text = "${(preferences.musicVolume * 100).toInt()}%", fontSize = 11.sp, color = Color(0xFF64748B))
                                    }
                                }
                                Switch(
                                    checked = preferences.musicEnabled,
                                    onCheckedChange = onToggleMusic,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = JadeEmerald
                                    ),
                                    modifier = Modifier.testTag("customization_music_switch")
                                )
                            }

                            if (preferences.musicEnabled) {
                                Slider(
                                    value = preferences.musicVolume,
                                    onValueChange = onUpdateMusicVolume,
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = JadeEmerald,
                                        activeTrackColor = JadeEmerald,
                                        inactiveTrackColor = Color(0xFFE2E8F0)
                                    ),
                                    modifier = Modifier.fillMaxWidth().testTag("customization_music_slider")
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Sound Effects Preview / Test Khmer Instruments
                            Text(
                                text = "Thử âm thanh nhạc cụ Khmer:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onTestSound("move") },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Nước đi", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { onTestSound("capture") },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Ăn quân", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { onTestSound("check") },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Chiếu", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { onTestSound("victory") },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Thắng", fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Dark Mode Toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DarkMode, contentDescription = null, tint = Color(0xFFD97706))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = AppStrings.get(language, "dark_mode"), fontSize = 14.sp, color = Color(0xFF1E293B))
                                }
                                Switch(
                                    checked = preferences.isDarkMode,
                                    onCheckedChange = onToggleDarkMode,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFFD97706)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Name Dialog
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text(text = AppStrings.get(language, "display_name"), fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = editNameText,
                    onValueChange = { editNameText = it },
                    label = { Text(AppStrings.get(language, "display_name")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateUsername(editNameText.trim())
                        showEditNameDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    Text(text = AppStrings.get(language, "save"), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditNameDialog = false }) {
                    Text(text = AppStrings.get(language, "cancel"))
                }
            }
        )
    }
}
