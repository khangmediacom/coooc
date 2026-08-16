package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.data.model.BoardTheme
import com.example.data.model.Piece
import com.example.data.model.PieceColor
import com.example.data.model.PieceStyle
import com.example.data.model.PieceType
import com.example.data.model.UserPreferences
import com.example.ui.localization.AppStrings
import com.example.ui.theme.AngkorGold

/**
 * In-Game Visual Theme Settings Dialog allowing users to switch board and piece visual styles
 * on the fly without exiting their current game.
 */
@Composable
fun InGameThemeSettingsDialog(
    preferences: UserPreferences,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSelectTheme: (BoardTheme) -> Unit,
    onSelectPieceStyle: (PieceStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .border(1.5.dp, Color(0xFFE2C474), RoundedCornerShape(24.dp))
                .testTag("in_game_theme_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFF3D6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.get(language, "custom_themes"),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = AppStrings.get(language, "board_theme"),
                                fontSize = 11.sp,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: 0: Board Theme, 1: Piece Style
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFF5EFE0),
                    contentColor = Color(0xFF1E293B),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = AngkorGold,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = AppStrings.get(language, "board_theme"),
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.5.sp,
                                color = if (selectedTab == 0) Color(0xFFB45309) else Color(0xFF64748B)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = AppStrings.get(language, "piece_style"),
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.5.sp,
                                color = if (selectedTab == 1) Color(0xFFB45309) else Color(0xFF64748B)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Content for selected tab
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (selectedTab == 0) {
                        // Board Themes List
                        items(BoardTheme.entries.size) { index ->
                            val theme = BoardTheme.entries[index]
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
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) AngkorGold else Color(0xFFE8DCB8),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onSelectTheme(theme) }
                                    .testTag("in_game_theme_${theme.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFFFFBEB) else Color(0xFFFFFFFF)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // 2x2 Board Preview Swatch
                                        Column(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .border(1.dp, Color(theme.borderHex), RoundedCornerShape(6.dp))
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

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column {
                                            Text(
                                                text = themeTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.5.sp,
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
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color(0xFFD97706),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Piece Styles List
                        items(PieceStyle.entries.size) { index ->
                            val style = PieceStyle.entries[index]
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
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) AngkorGold else Color(0xFFE8DCB8),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onSelectPieceStyle(style) }
                                    .testTag("in_game_piece_style_${style.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFFFFFBEB) else Color(0xFFFFFFFF)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
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
                                                fontSize = 13.5.sp,
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
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = Color(0xFFD97706),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Mini piece preview strip
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFF8FAFC))
                                            .padding(horizontal = 6.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        listOf(
                                            Piece(PieceType.KING, PieceColor.WHITE),
                                            Piece(PieceType.QUEEN, PieceColor.BLACK),
                                            Piece(PieceType.BISHOP, PieceColor.WHITE),
                                            Piece(PieceType.KNIGHT, PieceColor.BLACK),
                                            Piece(PieceType.ROOK, PieceColor.WHITE),
                                            Piece(PieceType.PAWN, PieceColor.BLACK)
                                        ).forEach { piece ->
                                            PieceView(
                                                piece = piece,
                                                style = style,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Done Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = AppStrings.get(language, "close"),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
