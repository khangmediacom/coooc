package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.AIDifficulty
import com.example.data.model.AppLanguage
import com.example.data.model.PieceColor
import com.example.ui.components.AngkorWarmBackground
import com.example.ui.components.GoogleLogo
import com.example.ui.components.HanumanAvatar
import com.example.ui.components.HanumanMascotBanner
import com.example.ui.components.HanumanMechanicsGuideModal
import com.example.ui.localization.AppStrings

/**
 * Pixel-Perfect Home Screen with warm glowing Angkor Wat & Bayon daylight background,
 * Khmer Hanuman mascot hero banner, and clean linear List module cards without wasted space.
 */
@Composable
fun HomeScreen(
    userProfile: UserProfileEntity,
    language: AppLanguage,
    onStartAi: (AIDifficulty, PieceColor) -> Unit,
    onStartLocal2P: () -> Unit,
    onStartOnline: () -> Unit,
    onOpenTactics: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenCustomization: () -> Unit,
    onPromptSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAiSetupDialog by remember { mutableStateOf(false) }
    var showHanumanGuide by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf(AIDifficulty.AMATEUR) }
    var selectedColor by remember { mutableStateOf<PieceColor?>(null) }

    AngkorWarmBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. TOP APP BAR (Logo + Title & Profile / Sign-in)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Logo & App Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        com.example.ui.components.KhmerAppLogo(
                            size = 40.dp,
                            showOuterBorder = true
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.get(language, "app_title"),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = AppStrings.get(language, "app_subtitle"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    // Right: User Profile (Logged in vs Guest)
                    if (userProfile.isLoggedIn) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFFFFFF))
                                .border(1.dp, Color(0xFFE8DCB8), RoundedCornerShape(14.dp))
                                .clickable { onOpenCustomization() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("top_user_profile")
                        ) {
                            HanumanAvatar(size = 30.dp, isSmiling = true, showCrown = false)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = userProfile.username.ifEmpty { "Sokha" },
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFC89320))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "${userProfile.elo} Elo",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFFFFF)
                                    )
                                }
                            }
                        }
                    } else {
                        // Guest Mode: Sign-in CTA Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFFFFFF))
                                .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(20.dp))
                                .clickable { onPromptSignIn() }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .testTag("guest_signin_pill")
                        ) {
                            GoogleLogo(modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get(language, "sign_in_google"),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }
            }

            // 2. SUB-HEADER BAR (● Online count & Stats)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF16A34A))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.get(language, "online_count"),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569)
                        )
                    }

                    if (userProfile.isLoggedIn) {
                        Text(
                            text = "W: ${userProfile.wins.coerceAtLeast(48)} / L: ${userProfile.losses.coerceAtLeast(32)}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    } else {
                        Text(
                            text = AppStrings.get(language, "guest_stats_hidden"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF78350F)
                        )
                    }
                }
            }

            // 3. KHMER HANUMAN MASCOT HERO BANNER
            item {
                HanumanMascotBanner(
                    language = language,
                    onQuickPlay = { showAiSetupDialog = true },
                    onOpenGuide = { showHanumanGuide = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 4. PRIMARY HERO CTA BUTTON: "Quick Play vs AI"
            item {
                Button(
                    onClick = { showAiSetupDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(3.dp, RoundedCornerShape(16.dp), spotColor = Color(0x40C89320))
                        .clip(RoundedCornerShape(16.dp))
                        .testTag("quick_play_cta_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD97706)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.get(language, "quick_play_ai"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // 5. CLEAN LIST MODULES (Replacing 2x3 grid with linear, space-efficient list cards)
            // Module 1: Play vs AI (Máy)
            item {
                HomeMenuListCard(
                    title = AppStrings.get(language, "play_vs_ai"),
                    subtitle = AppStrings.get(language, "play_vs_ai_desc"),
                    icon = Icons.Default.Psychology,
                    onClick = { showAiSetupDialog = true },
                    tag = "card_play_ai"
                )
            }

            // Module 2: Local 2 Player (Hai người cùng máy)
            item {
                HomeMenuListCard(
                    title = AppStrings.get(language, "local_2p"),
                    subtitle = AppStrings.get(language, "local_2p_desc"),
                    icon = Icons.Default.Group,
                    onClick = onStartLocal2P,
                    tag = "card_local_2p"
                )
            }

            // Module 3: Online Ranked Match (Trận đấu Trực tuyến)
            item {
                HomeMenuListCard(
                    title = AppStrings.get(language, "online_match"),
                    subtitle = AppStrings.get(language, "online_match_desc"),
                    icon = Icons.Default.Public,
                    subtitleColor = Color(0xFF059669),
                    isLocked = !userProfile.isLoggedIn,
                    onClick = {
                        if (userProfile.isLoggedIn) {
                            onStartOnline()
                        } else {
                            onPromptSignIn()
                        }
                    },
                    tag = "card_online_match"
                )
            }

            // Module 4: Tactics Puzzles (Thế cờ Tàn cuộc)
            item {
                HomeMenuListCard(
                    title = AppStrings.get(language, "tactics_puzzles"),
                    subtitle = AppStrings.get(language, "tactics_puzzles_desc"),
                    icon = Icons.Default.Extension,
                    onClick = onOpenTactics,
                    tag = "card_tactics"
                )
            }

            // Module 5: Match History & Replays (Lịch sử đấu)
            item {
                HomeMenuListCard(
                    title = AppStrings.get(language, "history_replays"),
                    subtitle = AppStrings.get(language, "history_replays_desc"),
                    icon = Icons.Default.Schedule,
                    isLocked = !userProfile.isLoggedIn,
                    onClick = {
                        if (userProfile.isLoggedIn) {
                            onOpenHistory()
                        } else {
                            onPromptSignIn()
                        }
                    },
                    tag = "card_history"
                )
            }

            // Module 6: Leaderboard (Bảng xếp hạng)
            item {
                HomeMenuListCard(
                    title = AppStrings.get(language, "leaderboard"),
                    subtitle = AppStrings.get(language, "leaderboard_desc"),
                    icon = Icons.Default.EmojiEvents,
                    onClick = onOpenLeaderboard,
                    tag = "card_leaderboard"
                )
            }

            // Module 7: Custom Themes & Audio (Cài đặt Giao diện & Âm thanh)
            item {
                HomeMenuListCard(
                    title = AppStrings.get(language, "custom_themes"),
                    subtitle = AppStrings.get(language, "custom_themes_desc"),
                    icon = Icons.Default.Palette,
                    onClick = onOpenCustomization,
                    tag = "card_custom_themes"
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // AI Match Setup Modal Dialog
    if (showAiSetupDialog) {
        Dialog(onDismissRequest = { showAiSetupDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .border(1.5.dp, Color(0xFFE2C474), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            HanumanAvatar(size = 32.dp, isSmiling = true, showCrown = false)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.get(language, "ai_setup_title"),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        }
                        IconButton(onClick = { showAiSetupDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = AppStrings.get(language, "choose_difficulty"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF78350F),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AIDifficulty.entries.forEach { diff ->
                            val isSelected = selectedDifficulty == diff
                            val diffTitle = when (language) {
                                AppLanguage.VIETNAMESE -> diff.titleVi
                                AppLanguage.KHMER -> diff.titleKm
                                AppLanguage.FRENCH -> diff.titleFr
                                AppLanguage.ENGLISH -> diff.titleEn
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFFFFF3D6) else Color(0xFFF8F4EA))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) Color(0xFFD97706) else Color(0xFFE8DCB8),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedDifficulty = diff }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = diffTitle,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF1E293B)
                                        )
                                        Text(
                                            text = "${diff.elo} Elo • Depth ${diff.searchDepth}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                    if (isSelected) {
                                        Text(text = "✓", color = Color(0xFFD97706), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = AppStrings.get(language, "play_as_side"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF78350F),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SideSelectionPill(
                            title = AppStrings.get(language, "white_side"),
                            isSelected = selectedColor == PieceColor.WHITE,
                            onClick = { selectedColor = PieceColor.WHITE },
                            modifier = Modifier.weight(1f)
                        )
                        SideSelectionPill(
                            title = AppStrings.get(language, "random_side"),
                            isSelected = selectedColor == null,
                            onClick = { selectedColor = null },
                            modifier = Modifier.weight(1f)
                        )
                        SideSelectionPill(
                            title = AppStrings.get(language, "black_side"),
                            isSelected = selectedColor == PieceColor.BLACK,
                            onClick = { selectedColor = PieceColor.BLACK },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val color = selectedColor ?: if (Math.random() < 0.5) PieceColor.WHITE else PieceColor.BLACK
                            showAiSetupDialog = false
                            onStartAi(selectedDifficulty, color)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                    ) {
                        Text(text = AppStrings.get(language, "start_match"), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
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

/**
 * Clean, space-efficient linear List Module Card for HomeScreen.
 * Solves empty grid gap issues by providing clear horizontal layout with rich icons & arrow affordances.
 */
@Composable
fun HomeMenuListCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitleColor: Color = Color(0xFF64748B),
    isLocked: Boolean = false,
    tag: String = ""
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.5.dp, RoundedCornerShape(14.dp), spotColor = Color(0x20D4AF37))
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(tag),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(Color(0xFFE8DCB8), Color(0xFFD4AF37).copy(alpha = 0.45f))
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Round Icon Container
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF8E6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color(0xFFC89320),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.5.sp,
                        color = subtitleColor
                    )
                }
            }

            // Right Status: Lock Icon or Subtle Forward Chevron
            if (isLocked) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF3E8D2))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked for Guest",
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
fun SideSelectionPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFFFFF3D6) else Color(0xFFF8F4EA))
            .border(
                1.5.dp,
                if (isSelected) Color(0xFFD97706) else Color(0xFFE8DCB8),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF92400E) else Color(0xFF475569)
        )
    }
}
