package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.example.R
import com.example.data.model.AppLanguage
import com.example.engine.AudioHaptics
import com.example.ui.theme.LateriteDark
import com.example.ui.theme.LateriteSurface
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalGoldDark
import com.example.ui.theme.RoyalGoldLight
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.components.AngkorWatTempleCanvas
import com.example.ui.components.HanumanWarriorStanding
import com.example.ui.localization.AppStrings

private val GoldPrimary = RoyalGold
private val GoldLight = RoyalGoldLight
private val GoldAmber = RoyalGold
private val RoyalGoldDark = RoyalGoldDark
private val WarmBg = Color(0xFFF9F7F1)
private val CardBg = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF35312D)
private val TextMuted = Color(0xFF867E75)

@Composable
fun WelcomeScreen(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome_animations")

    // Slow rotation for background solar mandala wheel
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "mandala_rotation"
    )

    // Gentle float & tilt effect for Hanuman mascot
    val floatOffsetY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hanuman_float"
    )





    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBg)
    ) {
        // 1. Top Background: Angkor Wat Hero Silhouette + Solar Mandala Wheel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .align(Alignment.TopCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.angkor_bg),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Rotating Solar Mandala Wheel behind the spires
            SolarMandalaOrnament(
                color = GoldAmber.copy(alpha = 0.35f),
                modifier = Modifier
                    .size(230.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 35.dp, y = 15.dp)
                    .graphicsLayer { rotationZ = rotationAngle }
            )

            // Gradient overlay to smoothly blend image into the warm background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                WarmBg.copy(alpha = 0.35f),
                                WarmBg.copy(alpha = 0.85f),
                                WarmBg
                            ),
                            startY = 0f, endY = 1000f
                        )
                    )
            )
        }

        // 2. Main Scrollable Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // Top Hero Badge: "ANGKOR · 802 AD"
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CardBg.copy(alpha = 0.90f),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAmber.copy(alpha = 0.5f)),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = "ANGKOR · 802 AD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.5.sp,
                    color = RoyalGoldDark,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            // App Main Title
            Text(
                text = AppStrings.get(currentLanguage, "app_title"),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = RoyalGoldDark,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("title_app_welcome")
            )

            // App Subtitle
            Text(
                text = AppStrings.get(currentLanguage, "app_subtitle"),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Center Mascot with Floating Animation & Glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(280.dp)
                    .offset { androidx.compose.ui.unit.IntOffset(0, floatOffsetY.roundToInt()) }
                    
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEnter
                    )
            ) {
                // Gold Glow background
                Box(
                    modifier = Modifier
                        .size(110.dp, 55.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-8).dp)
                        .graphicsLayer { alpha = 0.50f + (floatOffsetY / 40f) }
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    GoldAmber,
                                    Color.Transparent
                                )
                            )
                        )
                )

                // High-resolution Hanuman Hero Mascot Vector
                Image(
                    painter = painterResource(id = R.drawable.mascot1),
                    contentDescription = "Hanuman Mascot",
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                    modifier = Modifier
                        .size(280.dp)
                        .offset(y = (-5).dp)
                        .graphicsLayer { translationY = floatOffsetY }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            // 4. Kbach-framed Welcome Card
            KbachFramedCard(
                currentLanguage = currentLanguage,
                onSelectLanguage = onSelectLanguage,
                onEnter = onEnter
            )

            }
    }
}

@Composable
private fun KbachFramedCard(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "kbach_animations")
    val buttonPulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_pulse"
    )
    val buttonGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_glow"
    )
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = CardBg.copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8DDCC)),
        shadowElevation = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Corner Traditional Khmer Kbach Ornaments
            KhmerCornerOrnament(
                color = GoldAmber.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.TopStart)
            )
            KhmerCornerOrnament(
                color = GoldAmber.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.TopEnd)
                    .scale(scaleX = -1f, scaleY = 1f)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Greeting
                Text(
                    text = AppStrings.get(currentLanguage, "welcome_greeting"),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = TextDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Tagline
                Text(
                    text = AppStrings.get(currentLanguage, "welcome_tagline"),
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Decorative Lotus Divider
                KhmerLotusDivider(
                    color = GoldAmber.copy(alpha = 0.55f),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Language Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.get(currentLanguage, "choose_language"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.6.sp,
                        color = RoyalGoldDark
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2x2 Language Selection Grid
                val languages = listOf(
                    LanguageOption(AppLanguage.KHMER, "ភាសាខ្មែរ", "Khmer", "🇰🇭"),
                    LanguageOption(AppLanguage.ENGLISH, "English", "English", "🇬🇧"),
                    LanguageOption(AppLanguage.VIETNAMESE, "Tiếng Việt", "Vietnamese", "🇻🇳"),
                    LanguageOption(AppLanguage.FRENCH, "Français", "French", "🇫🇷")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LanguageCard(
                            option = languages[0],
                            isSelected = currentLanguage == languages[0].language,
                            onClick = {
                                // AudioHaptics.playButtonClick(context, true)
                                onSelectLanguage(languages[0].language)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        LanguageCard(
                            option = languages[1],
                            isSelected = currentLanguage == languages[1].language,
                            onClick = {
                                // AudioHaptics.playButtonClick(context, true)
                                onSelectLanguage(languages[1].language)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LanguageCard(
                            option = languages[2],
                            isSelected = currentLanguage == languages[2].language,
                            onClick = {
                                // AudioHaptics.playButtonClick(context, true)
                                onSelectLanguage(languages[2].language)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        LanguageCard(
                            option = languages[3],
                            isSelected = currentLanguage == languages[3].language,
                            onClick = {
                                // AudioHaptics.playButtonClick(context, true)
                                onSelectLanguage(languages[3].language)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Enter Button ("Enter the Temple")
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .graphicsLayer {
                            scaleX = buttonPulseScale
                            scaleY = buttonPulseScale
                        }
                ) {
                    // Glow effect
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        GoldAmber.copy(alpha = buttonGlowAlpha),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                                        Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(com.example.ui.theme.RoyalGoldDark, com.example.ui.theme.RoyalGold.copy(alpha = 0.9f), com.example.ui.theme.GoldLight)
                                )
                            )
                            .clickable { onEnter() }
                            .testTag("btn_enter_temple"),
                        contentAlignment = Alignment.Center
                    ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = AppStrings.get(currentLanguage, "enter"),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Note
                Text(
                    text = AppStrings.get(currentLanguage, "welcome_note"),
                    fontSize = 10.5.sp,
                    color = TextMuted.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class LanguageOption(
    val language: AppLanguage,
    val nativeName: String,
    val subName: String,
    val flag: String
)

@Composable
fun LanguageCard(
    option: LanguageOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) GoldAmber else Color(0xFFE2D9CC)
    val bgColor = if (isSelected) Color(0xFFFEF3C7).copy(alpha = 0.75f) else Color(0xFFF8F4ED)
    val scale = if (isSelected) 1.02f else 1.0f

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(if (isSelected) 1.8.dp else 1.dp, borderColor),
        modifier = modifier
            .scale(scale)
            .testTag("lang_card_${option.language.code}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = option.flag,
                fontSize = 19.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = option.nativeName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = option.subName,
                    fontSize = 9.5.sp,
                    color = TextMuted
                )
            }
        }
    }
}

/**
 * Traditional Khmer Solar Mandala Ornament (12 Petals)
 */
@Composable
fun SolarMandalaOrnament(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val petalWidth = size.width * 0.13f
        val petalHeight = size.height * 0.42f

        // Draw 12 rotating petal ellipses
        for (i in 0 until 12) {
            rotate(degrees = i * 30f, pivot = center) {
                drawOval(
                    color = color,
                    topLeft = Offset(center.x - petalWidth / 2f, center.y - petalHeight),
                    size = Size(petalWidth, petalHeight),
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }
        }

        // Concentric inner rings
        drawCircle(
            color = color,
            radius = size.width * 0.10f,
            center = center,
            style = Stroke(width = 1.5.dp.toPx())
        )
        drawCircle(
            color = color,
            radius = size.width * 0.04f,
            center = center,
            style = Stroke(width = 1.2.dp.toPx())
        )
    }
}

/**
 * Traditional Khmer Kbach Corner Vector Ornament
 */
@Composable
fun KhmerCornerOrnament(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val path1 = Path().apply {
            moveTo(w * 0.06f, h * 0.94f)
            lineTo(w * 0.06f, h * 0.35f)
            cubicTo(w * 0.06f, h * 0.19f, w * 0.19f, h * 0.06f, w * 0.35f, h * 0.06f)
            lineTo(w * 0.94f, h * 0.06f)
        }
        drawPath(path1, color = color.copy(alpha = 0.4f), style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round))

        val path2 = Path().apply {
            moveTo(w * 0.22f, h * 0.94f)
            lineTo(w * 0.22f, h * 0.42f)
            cubicTo(w * 0.22f, h * 0.30f, w * 0.30f, h * 0.22f, w * 0.42f, h * 0.22f)
            lineTo(w * 0.88f, h * 0.22f)
        }
        drawPath(path2, color = color.copy(alpha = 0.7f), style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round))

        // Center floral accent
        val path3 = Path().apply {
            moveTo(w * 0.41f, h * 0.66f)
            cubicTo(w * 0.41f, h * 0.52f, w * 0.52f, h * 0.41f, w * 0.66f, h * 0.41f)
            cubicTo(w * 0.61f, h * 0.55f, w * 0.55f, h * 0.61f, w * 0.41f, h * 0.66f)
            close()
        }
        drawPath(path3, color = color, style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round))

        drawCircle(
            color = color,
            radius = 1.8.dp.toPx(),
            center = Offset(w * 0.38f, h * 0.72f)
        )
    }
}

/**
 * Traditional Khmer Lotus Decorative Divider
 */
@Composable
fun KhmerLotusDivider(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cy = h / 2f
        val cx = w / 2f

        // Left & Right horizontal stroke lines
        drawLine(
            color = color.copy(alpha = 0.45f),
            start = Offset(w * 0.04f, cy),
            end = Offset(cx - 30.dp.toPx(), cy),
            strokeWidth = 1.2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color.copy(alpha = 0.45f),
            start = Offset(cx + 30.dp.toPx(), cy),
            end = Offset(w * 0.96f, cy),
            strokeWidth = 1.2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Center Lotus Bud
        val lotusPath = Path().apply {
            moveTo(cx, cy - 6.dp.toPx())
            cubicTo(cx + 5.dp.toPx(), cy - 3.dp.toPx(), cx + 6.dp.toPx(), cy, cx + 6.dp.toPx(), cy + 2.dp.toPx())
            cubicTo(cx + 6.dp.toPx(), cy + 5.dp.toPx(), cx + 3.dp.toPx(), cy + 6.dp.toPx(), cx, cy + 6.dp.toPx())
            cubicTo(cx - 3.dp.toPx(), cy + 6.dp.toPx(), cx - 6.dp.toPx(), cy + 5.dp.toPx(), cx - 6.dp.toPx(), cy + 2.dp.toPx())
            cubicTo(cx - 6.dp.toPx(), cy, cx - 5.dp.toPx(), cy - 3.dp.toPx(), cx, cy - 6.dp.toPx())
            close()
        }
        drawPath(lotusPath, color = color, style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round))

        // Side petals
        val leftPetal = Path().apply {
            moveTo(cx - 18.dp.toPx(), cy)
            cubicTo(cx - 14.dp.toPx(), cy - 5.dp.toPx(), cx - 9.dp.toPx(), cy - 5.dp.toPx(), cx - 6.dp.toPx(), cy)
            cubicTo(cx - 9.dp.toPx(), cy + 5.dp.toPx(), cx - 14.dp.toPx(), cy + 5.dp.toPx(), cx - 18.dp.toPx(), cy)
            close()
        }
        drawPath(leftPetal, color = color, style = Stroke(width = 1.1.dp.toPx(), cap = StrokeCap.Round))

        val rightPetal = Path().apply {
            moveTo(cx + 18.dp.toPx(), cy)
            cubicTo(cx + 14.dp.toPx(), cy - 5.dp.toPx(), cx + 9.dp.toPx(), cy - 5.dp.toPx(), cx + 6.dp.toPx(), cy)
            cubicTo(cx + 9.dp.toPx(), cy + 5.dp.toPx(), cx + 14.dp.toPx(), cy + 5.dp.toPx(), cx + 18.dp.toPx(), cy)
            close()
        }
        drawPath(rightPetal, color = color, style = Stroke(width = 1.1.dp.toPx(), cap = StrokeCap.Round))

        // Center dot
        drawCircle(
            color = color,
            radius = 1.6.dp.toPx(),
            center = Offset(cx, cy)
        )
    }
}

