package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkLateriteColorScheme = darkColorScheme(
    primary = RoyalGold,
    onPrimary = LateriteDark,
    primaryContainer = LateriteSurface,
    onPrimaryContainer = RoyalGoldLight,
    secondary = RoyalGold,
    onSecondary = LateriteDark,
    tertiary = LiveGreen,
    background = LateriteDark,
    onBackground = TextPrimaryDark,
    surface = LateriteSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = AngkorWarmCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = AngkorWarmBorder,
    error = TerracottaRed
)

private val LightLateriteColorScheme = lightColorScheme(
    primary = RoyalGold,
    onPrimary = Color.White,
    primaryContainer = RoyalGoldLight,
    onPrimaryContainer = LateriteDark,
    secondary = LiveGreen,
    onSecondary = Color.White,
    tertiary = TerracottaRed,
    background = Color(0xFFFDFBF7),
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF5F0E6),
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFE5D9C5),
    error = TerracottaRed
)

@Composable
fun KhmerChessTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkLateriteColorScheme else LightLateriteColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
