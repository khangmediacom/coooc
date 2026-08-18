package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import com.example.data.model.PieceType
import com.example.data.model.Piece
import com.example.data.model.PieceStyle
import com.example.ui.components.PieceRenderer
import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AIDifficulty
import com.example.data.model.AppLanguage
import com.example.data.model.PieceColor
import com.example.data.local.entity.UserProfileEntity
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*

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
    com.example.ui.components.AngkorWarmBackground { LazyColumn(
        modifier = modifier
            .fillMaxSize()
            
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, RoyalGold.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                // Blur spot
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 24.dp, y = (-24).dp)
                        .size(96.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(RoyalGold.copy(alpha = 0.2f), Color.Transparent)
                            )
                        )
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, RoyalGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = com.example.R.drawable.mascot),
                                contentDescription = "User Avatar",
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.get(language, "guest"),
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = AppStrings.get(language, "guest_mode"),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = RoyalGoldDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "3",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalGoldDark
                                )
                            }
                            Text(
                                text = "STREAK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = RoyalGold)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(RoyalGoldDark, RoyalGold.copy(alpha = 0.9f), GoldLight)
                                )
                            )
                            .clickable { onStartAi(AIDifficulty.AMATEUR, PieceColor.WHITE) }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = AppStrings.get(language, "quick_play_ai"),
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            KbachDividerLine()
        }

        item {
            SectionTitle(title = AppStrings.get(language, "game_modes"), icon = Icons.Default.AutoAwesome)
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Game Modes
        val modes = listOf(
            ModeItem(AppStrings.get(language, "local_2p"), AppStrings.get(language, "local_2p_desc"), Icons.Default.People, "teak", onStartLocal2P),
            ModeItem(AppStrings.get(language, "online_match"), AppStrings.get(language, "online_match_desc"), Icons.Default.Public, "jade", onStartOnline),
        )

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(modes) { mode ->
            ModeCard(mode)
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            KbachDividerLine()
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(title = AppStrings.get(language, "pieces_guide"), icon = Icons.Filled.MenuBook)
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp)
            ) {
                val pieces = listOf(
                    Triple(com.example.data.model.PieceType.KING, "Ang (Vua)", "Tướng"),
                    Triple(com.example.data.model.PieceType.QUEEN, "Neang (Hậu)", "Sĩ"),
                    Triple(com.example.data.model.PieceType.BISHOP, "Koul (Tượng)", "Tượng"),
                    Triple(com.example.data.model.PieceType.KNIGHT, "Ses (Mã)", "Mã"),
                    Triple(com.example.data.model.PieceType.ROOK, "Tuuk (Xe)", "Xe"),
                    Triple(com.example.data.model.PieceType.PAWN, "Trey (Tốt)", "Tốt")
                )
                items(pieces.size) { index ->
                    PieceGuideCard(
                        pieceType = pieces[index].first,
                        kmName = pieces[index].second,
                        enName = pieces[index].third,
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

}


@Composable
fun KbachDividerLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1f).height(1.dp).background(RoyalGold.copy(alpha = 0.55f)))
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(RoyalGold))
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.weight(1f).height(1.dp).background(RoyalGold.copy(alpha = 0.55f)))
        }
    }
}

@Composable
fun SectionTitle(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RoyalGold,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.8.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

data class ModeItem(val title: String, val desc: String, val icon: ImageVector, val tone: String, val action: () -> Unit)

@Composable
fun ModeCard(mode: ModeItem) {
    val (bgAlpha, borderAlpha, iconTint) = when (mode.tone) {
        "gold" -> Triple(0.15f, 0.40f, RoyalGoldDark)
        "teak" -> Triple(0.10f, 0.30f, RoyalTeakMedium)
        "jade" -> Triple(0.12f, 0.35f, JadeEmerald)
        else -> Triple(0.15f, 0.40f, RoyalGoldDark)
    }
    
    val toneColor = when(mode.tone) {
        "gold" -> Gold
        "teak" -> RoyalTeakMedium
        "jade" -> JadeEmerald
        else -> Gold
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { mode.action() }
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
                    .border(1.dp, toneColor.copy(alpha = borderAlpha), RoundedCornerShape(12.dp))
                    .background(toneColor.copy(alpha = bgAlpha)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = mode.icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mode.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = mode.desc,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun PieceGuideCard(pieceType: PieceType, kmName: String, enName: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        PieceRenderer(
            piece = Piece(pieceType, PieceColor.WHITE),
            style = PieceStyle.SCULPTED,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = kmName,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = enName,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


