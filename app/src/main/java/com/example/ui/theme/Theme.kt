package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WarmAngkorColorScheme = lightColorScheme(
    primary = AngkorGold,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = AngkorWarmSurface,
    onPrimaryContainer = AngkorGoldDark,
    secondary = KhmerAmber,
    onSecondary = Color(0xFFFFFFFF),
    tertiary = LiveGreen,
    background = AngkorWarmCanvas,
    onBackground = TextPrimaryDark,
    surface = AngkorWarmSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = AngkorWarmCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = AngkorWarmBorder,
    error = TerracottaRed
)

@Composable
fun KhmerChessTheme(
    darkTheme: Boolean = false, // Always bright, warm Angkor daylight aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WarmAngkorColorScheme,
        typography = Typography,
        content = content
    )
}
