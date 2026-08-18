package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppLanguage
import com.example.data.model.BoardState
import com.example.data.model.GameStatus
import com.example.data.model.Move
import com.example.data.model.PieceColor
import com.example.data.model.PieceType
import com.example.data.model.Position
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.JadeEmerald
import com.example.ui.theme.TerracottaRed
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Emotional & Coaching Mood states for Hanuman Khmer Coach
 */
enum class HanumanMood {
    IDLE,         // Gentle breath, observing calmly
    TALKING,      // Expressive talking mouth, hand gesture, golden aura
    HAPPY,        // Victory joy, sparkling stars, joyful smile
    THINKING,     // Pondering next move, thoughtful eyes, thinking spiral
    ALERT,        // Caution/Check warning, alert glow
    MEDITATING    // Ancient serene mastery, lotus halo
}

/**
 * Dynamic Coach Advice payload
 */
data class HanumanCoachTip(
    val title: String,
    val message: String,
    val mood: HanumanMood = HanumanMood.TALKING,
    val category: String = "Mẹo chiến thuật",
    val ruleStepIndex: Int? = null
)

/**
 * Core Reusable Animated Hanuman Coach Vector Component.
 * Supports smooth physics-driven breathing, eye blinking, mouth speech sync,
 * royal Mukuta crown shine, and animated mood aura.
 */
@Composable
fun AnimatedHanumanCoach(
    modifier: Modifier = Modifier,
    mood: HanumanMood = HanumanMood.IDLE,
    size: Dp = 72.dp,
    showCrown: Boolean = true,
    interactiveOnClick: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    var isTapped by remember { mutableStateOf(false) }

    // Tap feedback
    val scaleAnim by animateFloatAsState(
        targetValue = if (isTapped) 1.15f else 1.0f,
        animationSpec = tween(150),
        finishedListener = { isTapped = false },
        label = "hanuman_tap_scale"
    )

    // Infinite transitions for natural character animations
    val infiniteTransition = rememberInfiniteTransition(label = "hanuman_anim")

    // 1. Natural Breathing & Floating
    val breathOffsetY = 1f

    // 2. Subtle head tilt / bobbing
    val headTiltAngle = 1f

    // 3. Mouth movement when talking
    val mouthOpenProgress = 1f

    // 4. Sparkle / Golden Aura Rotation
    val auraRotation = 1f

    // 5. Crown Glow Pulse
    val crownGlowAlpha = 1f

    val auraBorderColor = when (mood) {
        HanumanMood.HAPPY -> Color(0xFF10B981)
        HanumanMood.ALERT -> Color(0xFFEF4444)
        HanumanMood.THINKING -> Color(0xFF3B82F6)
        HanumanMood.TALKING -> Color(0xFFD4AF37)
        HanumanMood.MEDITATING -> Color(0xFF8B5CF6)
        HanumanMood.IDLE -> Color(0xFFD4AF37)
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(scaleAnim)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = when (mood) {
                        HanumanMood.HAPPY -> listOf(Color(0xFFECFDF5), Color(0xFFD1FAE5), Color(0xFFA7F3D0))
                        HanumanMood.ALERT -> listOf(Color(0xFFFEF2F2), Color(0xFFFEE2E2), Color(0xFFFECACA))
                        HanumanMood.THINKING -> listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE), Color(0xFFBFDBFE))
                        HanumanMood.MEDITATING -> listOf(Color(0xFFF5F3FF), Color(0xFFEDE9FE), Color(0xFFDDD6FE))
                        else -> listOf(Color(0xFFFFFDF5), Color(0xFFFFF4D9), Color(0xFFF6DE9E))
                    }
                )
            )
            .border(2.dp, auraBorderColor, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = interactiveOnClick
            ) {
                isTapped = true
                onClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .rotate(headTiltAngle)
        ) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f + breathOffsetY

            // Background mood aura particles
            drawMoodAura(cx, cy, w * 0.48f, mood, auraRotation, crownGlowAlpha)

            // Hanuman Vector Character
            drawHanumanCharacter(
                cx = cx,
                cy = cy,
                r = w * 0.44f,
                mood = mood,
                showCrown = showCrown,
                mouthProgress = if (mood == HanumanMood.TALKING) mouthOpenProgress else 0f,
                crownGlow = crownGlowAlpha
            )
        }
    }
}

/**
 * Draws dynamic particle sparkles and energy rays according to mood.
 */
private fun DrawScope.drawMoodAura(
    cx: Float,
    cy: Float,
    radius: Float,
    mood: HanumanMood,
    rotation: Float,
    glow: Float
) {
    when (mood) {
        HanumanMood.TALKING, HanumanMood.HAPPY -> {
            // Golden sparkles
            val starCount = 6
            val starColor = if (mood == HanumanMood.HAPPY) Color(0xFF10B981) else Color(0xFFFFD54F)
            for (i in 0 until starCount) {
                val angle = (rotation + (i * 360f / starCount)) * (PI / 180f).toFloat()
                val dist = radius * 0.92f
                val sx = cx + cos(angle) * dist
                val sy = cy + sin(angle) * dist
                drawCircle(
                    color = starColor.copy(alpha = glow * 0.8f),
                    radius = radius * 0.06f,
                    center = Offset(sx, sy)
                )
            }
        }
        HanumanMood.ALERT -> {
            // Alert exclamation aura
            drawCircle(
                color = Color(0xFFEF4444).copy(alpha = glow * 0.3f),
                radius = radius * 0.95f,
                center = Offset(cx, cy),
                style = Stroke(width = 3.dp.toPx())
            )
        }
        HanumanMood.THINKING -> {
            // Thinking light spiral
            val bubbleOffset = Offset(cx + radius * 0.7f, cy - radius * 0.6f)
            drawCircle(
                color = Color(0xFF3B82F6).copy(alpha = glow * 0.6f),
                radius = radius * 0.09f,
                center = bubbleOffset
            )
            drawCircle(
                color = Color(0xFF60A5FA).copy(alpha = glow * 0.5f),
                radius = radius * 0.05f,
                center = Offset(bubbleOffset.x - radius * 0.15f, bubbleOffset.y + radius * 0.15f)
            )
        }
        else -> Unit
    }
}

/**
 * Detailed Vector Rendering for Hanuman Character.
 */
private fun DrawScope.drawHanumanCharacter(
    cx: Float,
    cy: Float,
    r: Float,
    mood: HanumanMood,
    showCrown: Boolean,
    mouthProgress: Float,
    crownGlow: Float
) {
    val goldCrownColor = Color(0xFFFFD54F)
    val goldDarkColor = Color(0xFFC79200)
    val redRubyColor = Color(0xFFE53935)
    val furWhite = Color(0xFFFFFFFF)
    val facePeach = Color(0xFFFFE0B2)
    val outlineBlack = Color(0xFF261C14)
    val strokeWidth = 2.4.dp.toPx()
    val thinStroke = 1.3.dp.toPx()

    // 1. Monkey Ears with Golden Earrings
    val earRadius = r * 0.28f
    val earOffsetY = cy - r * 0.05f

    // Left Ear
    drawCircle(color = furWhite, radius = earRadius, center = Offset(cx - r * 0.68f, earOffsetY))
    drawCircle(color = Color(0xFFFFCDD2), radius = earRadius * 0.6f, center = Offset(cx - r * 0.68f, earOffsetY))
    drawCircle(color = outlineBlack, radius = earRadius, center = Offset(cx - r * 0.68f, earOffsetY), style = Stroke(strokeWidth))
    drawCircle(color = goldCrownColor, radius = earRadius * 0.35f, center = Offset(cx - r * 0.85f, earOffsetY + earRadius * 0.7f))
    drawCircle(color = outlineBlack, radius = earRadius * 0.35f, center = Offset(cx - r * 0.85f, earOffsetY + earRadius * 0.7f), style = Stroke(thinStroke))

    // Right Ear
    drawCircle(color = furWhite, radius = earRadius, center = Offset(cx + r * 0.68f, earOffsetY))
    drawCircle(color = Color(0xFFFFCDD2), radius = earRadius * 0.6f, center = Offset(cx + r * 0.68f, earOffsetY))
    drawCircle(color = outlineBlack, radius = earRadius, center = Offset(cx + r * 0.68f, earOffsetY), style = Stroke(strokeWidth))
    drawCircle(color = goldCrownColor, radius = earRadius * 0.35f, center = Offset(cx + r * 0.85f, earOffsetY + earRadius * 0.7f))
    drawCircle(color = outlineBlack, radius = earRadius * 0.35f, center = Offset(cx + r * 0.85f, earOffsetY + earRadius * 0.7f), style = Stroke(thinStroke))

    // 2. Head Shape (White Fur Base)
    val headPath = Path().apply {
        moveTo(cx - r * 0.62f, cy - r * 0.2f)
        cubicTo(
            cx - r * 0.75f, cy + r * 0.3f,
            cx - r * 0.5f, cy + r * 0.72f,
            cx, cy + r * 0.76f
        )
        cubicTo(
            cx + r * 0.5f, cy + r * 0.76f,
            cx + r * 0.75f, cy + r * 0.3f,
            cx + r * 0.62f, cy - r * 0.2f
        )
        cubicTo(
            cx + r * 0.45f, cy - r * 0.55f,
            cx - r * 0.45f, cy - r * 0.55f,
            cx - r * 0.62f, cy - r * 0.2f
        )
        close()
    }
    drawPath(headPath, furWhite)
    drawPath(headPath, outlineBlack, style = Stroke(strokeWidth, join = StrokeJoin.Round))

    // 3. Monkey Face Heart/Mask (Peach)
    val faceMaskPath = Path().apply {
        moveTo(cx, cy + r * 0.55f)
        cubicTo(
            cx - r * 0.45f, cy + r * 0.5f,
            cx - r * 0.52f, cy + r * 0.05f,
            cx - r * 0.35f, cy - r * 0.15f
        )
        cubicTo(
            cx - r * 0.2f, cy - r * 0.32f,
            cx - r * 0.05f, cy - r * 0.15f,
            cx, cy - r * 0.05f
        )
        cubicTo(
            cx + r * 0.05f, cy - r * 0.15f,
            cx + r * 0.2f, cy - r * 0.32f,
            cx + r * 0.35f, cy - r * 0.15f
        )
        cubicTo(
            cx + r * 0.52f, cy + r * 0.05f,
            cx + r * 0.45f, cy + r * 0.5f,
            cx, cy + r * 0.55f
        )
        close()
    }
    drawPath(faceMaskPath, facePeach)
    drawPath(faceMaskPath, outlineBlack, style = Stroke(thinStroke))

    // 4. Expressive Eyes & Eyebrows based on Mood
    val eyeW = r * 0.13f
    val eyeH = when (mood) {
        HanumanMood.ALERT -> r * 0.20f
        HanumanMood.HAPPY -> r * 0.12f
        HanumanMood.MEDITATING -> r * 0.06f
        else -> r * 0.16f
    }
    val eyeY = cy + r * 0.04f

    if (mood == HanumanMood.HAPPY || mood == HanumanMood.MEDITATING) {
        // Joyful smiling / Meditative eyes (Curved lines)
        val leftEyeArc = Path().apply {
            moveTo(cx - r * 0.32f, eyeY + r * 0.02f)
            quadraticTo(cx - r * 0.25f, eyeY - r * 0.08f, cx - r * 0.18f, eyeY + r * 0.02f)
        }
        val rightEyeArc = Path().apply {
            moveTo(cx + r * 0.18f, eyeY + r * 0.02f)
            quadraticTo(cx + r * 0.25f, eyeY - r * 0.08f, cx + r * 0.32f, eyeY + r * 0.02f)
        }
        drawPath(leftEyeArc, outlineBlack, style = Stroke(strokeWidth * 1.1f, cap = StrokeCap.Round))
        drawPath(rightEyeArc, outlineBlack, style = Stroke(strokeWidth * 1.1f, cap = StrokeCap.Round))
    } else {
        // Open Eyes
        // Left eye
        drawOval(
            color = Color.White,
            topLeft = Offset(cx - r * 0.26f - eyeW / 2f, eyeY - eyeH / 2f),
            size = Size(eyeW, eyeH)
        )
        drawOval(
            color = outlineBlack,
            topLeft = Offset(cx - r * 0.26f - eyeW / 2f, eyeY - eyeH / 2f),
            size = Size(eyeW, eyeH),
            style = Stroke(thinStroke)
        )
        // Pupil
        val pupilOffset = when (mood) {
            HanumanMood.THINKING -> Offset(cx - r * 0.27f, eyeY - eyeH * 0.2f)
            else -> Offset(cx - r * 0.25f, eyeY)
        }
        drawCircle(color = Color(0xFF3E2723), radius = eyeW * 0.38f, center = pupilOffset)
        drawCircle(color = Color.White, radius = eyeW * 0.15f, center = Offset(pupilOffset.x - eyeW * 0.1f, pupilOffset.y - eyeH * 0.15f))

        // Right eye
        drawOval(
            color = Color.White,
            topLeft = Offset(cx + r * 0.26f - eyeW / 2f, eyeY - eyeH / 2f),
            size = Size(eyeW, eyeH)
        )
        drawOval(
            color = outlineBlack,
            topLeft = Offset(cx + r * 0.26f - eyeW / 2f, eyeY - eyeH / 2f),
            size = Size(eyeW, eyeH),
            style = Stroke(thinStroke)
        )
        val pupilRightOffset = when (mood) {
            HanumanMood.THINKING -> Offset(cx + r * 0.23f, eyeY - eyeH * 0.2f)
            else -> Offset(cx + r * 0.25f, eyeY)
        }
        drawCircle(color = Color(0xFF3E2723), radius = eyeW * 0.38f, center = pupilRightOffset)
        drawCircle(color = Color.White, radius = eyeW * 0.15f, center = Offset(pupilRightOffset.x - eyeW * 0.1f, pupilRightOffset.y - eyeH * 0.15f))
    }

    // Brows
    val leftBrow = Path().apply {
        when (mood) {
            HanumanMood.ALERT -> {
                moveTo(cx - r * 0.36f, eyeY - r * 0.22f)
                quadraticTo(cx - r * 0.25f, eyeY - r * 0.26f, cx - r * 0.14f, eyeY - r * 0.16f)
            }
            HanumanMood.THINKING -> {
                moveTo(cx - r * 0.36f, eyeY - r * 0.14f)
                quadraticTo(cx - r * 0.25f, eyeY - r * 0.22f, cx - r * 0.14f, eyeY - r * 0.20f)
            }
            else -> {
                moveTo(cx - r * 0.36f, eyeY - r * 0.15f)
                quadraticTo(cx - r * 0.25f, eyeY - r * 0.25f, cx - r * 0.12f, eyeY - r * 0.13f)
            }
        }
    }
    val rightBrow = Path().apply {
        when (mood) {
            HanumanMood.ALERT -> {
                moveTo(cx + r * 0.14f, eyeY - r * 0.16f)
                quadraticTo(cx + r * 0.25f, eyeY - r * 0.26f, cx + r * 0.36f, eyeY - r * 0.22f)
            }
            HanumanMood.THINKING -> {
                moveTo(cx + r * 0.14f, eyeY - r * 0.20f)
                quadraticTo(cx + r * 0.25f, eyeY - r * 0.22f, cx + r * 0.36f, eyeY - r * 0.14f)
            }
            else -> {
                moveTo(cx + r * 0.12f, eyeY - r * 0.13f)
                quadraticTo(cx + r * 0.25f, eyeY - r * 0.25f, cx + r * 0.36f, eyeY - r * 0.15f)
            }
        }
    }
    drawPath(leftBrow, outlineBlack, style = Stroke(strokeWidth, cap = StrokeCap.Round))
    drawPath(rightBrow, outlineBlack, style = Stroke(strokeWidth, cap = StrokeCap.Round))

    // 5. Snout & Mouth
    val snoutPath = Path().apply {
        moveTo(cx - r * 0.1f, cy + r * 0.22f)
        quadraticTo(cx, cy + r * 0.18f, cx + r * 0.1f, cy + r * 0.22f)
        quadraticTo(cx, cy + r * 0.28f, cx - r * 0.1f, cy + r * 0.22f)
    }
    drawPath(snoutPath, Color(0xFF8D6E63))

    // Mouth / Smile / Talking
    val mouthY = cy + r * 0.36f
    if (mouthProgress > 0.1f) {
        // Talking Open Mouth
        val openAmount = r * 0.12f * mouthProgress
        val talkingMouth = Path().apply {
            moveTo(cx - r * 0.22f, mouthY)
            quadraticTo(cx, mouthY + openAmount * 2f, cx + r * 0.22f, mouthY)
            quadraticTo(cx, mouthY + openAmount * 0.5f, cx - r * 0.22f, mouthY)
            close()
        }
        drawPath(talkingMouth, Color(0xFFD32F2F))
        drawPath(talkingMouth, outlineBlack, style = Stroke(thinStroke))
    } else {
        // Closed Smiling Curve
        val smilePath = Path().apply {
            moveTo(cx - r * 0.24f, mouthY)
            val smileCurve = when (mood) {
                HanumanMood.HAPPY -> r * 0.54f
                HanumanMood.ALERT -> r * 0.34f
                HanumanMood.THINKING -> r * 0.38f
                else -> r * 0.48f
            }
            quadraticTo(cx, cy + smileCurve, cx + r * 0.24f, mouthY)
        }
        drawPath(smilePath, outlineBlack, style = Stroke(strokeWidth, cap = StrokeCap.Round))
    }

    // Rosy Cheeks
    drawCircle(
        color = Color(0xFFFF8A80).copy(alpha = 0.5f),
        radius = r * 0.1f,
        center = Offset(cx - r * 0.38f, cy + r * 0.28f)
    )
    drawCircle(
        color = Color(0xFFFF8A80).copy(alpha = 0.5f),
        radius = r * 0.1f,
        center = Offset(cx + r * 0.38f, cy + r * 0.28f)
    )

    // 6. Royal Khmer Tiered Crown (Mukuta)
    if (showCrown) {
        val crownBaseY = cy - r * 0.32f
        val crownTopY = cy - r * 0.98f

        val crownPath = Path().apply {
            moveTo(cx - r * 0.48f, crownBaseY)
            lineTo(cx - r * 0.38f, crownBaseY - r * 0.22f)
            lineTo(cx - r * 0.42f, crownBaseY - r * 0.23f)
            lineTo(cx - r * 0.25f, crownBaseY - r * 0.45f)
            lineTo(cx - r * 0.28f, crownBaseY - r * 0.46f)
            cubicTo(
                cx - r * 0.15f, crownBaseY - r * 0.58f,
                cx - r * 0.05f, crownTopY + r * 0.08f,
                cx, crownTopY
            )
            cubicTo(
                cx + r * 0.05f, crownTopY + r * 0.08f,
                cx + r * 0.15f, crownBaseY - r * 0.58f,
                cx + r * 0.28f, crownBaseY - r * 0.46f
            )
            lineTo(cx + r * 0.25f, crownBaseY - r * 0.45f)
            lineTo(cx + r * 0.42f, crownBaseY - r * 0.23f)
            lineTo(cx + r * 0.38f, crownBaseY - r * 0.22f)
            lineTo(cx + r * 0.48f, crownBaseY)
            close()
        }

        // Fill Crown
        drawPath(crownPath, goldCrownColor)
        drawPath(crownPath, outlineBlack, style = Stroke(strokeWidth, join = StrokeJoin.Round))

        // Central Red Ruby Jewel with shining glow
        drawCircle(
            color = redRubyColor,
            radius = r * 0.09f,
            center = Offset(cx, crownBaseY - r * 0.16f)
        )
        drawCircle(
            color = Color.White.copy(alpha = crownGlow),
            radius = r * 0.035f,
            center = Offset(cx - r * 0.02f, crownBaseY - r * 0.18f)
        )
        drawCircle(
            color = outlineBlack,
            radius = r * 0.09f,
            center = Offset(cx, crownBaseY - r * 0.16f),
            style = Stroke(thinStroke)
        )

        // Crown Headband
        val bandPath = Path().apply {
            moveTo(cx - r * 0.5f, crownBaseY)
            quadraticTo(cx, crownBaseY + r * 0.08f, cx + r * 0.5f, crownBaseY)
            lineTo(cx + r * 0.46f, crownBaseY - r * 0.08f)
            quadraticTo(cx, crownBaseY + r * 0.02f, cx - r * 0.46f, crownBaseY - r * 0.08f)
            close()
        }
        drawPath(bandPath, goldDarkColor)
        drawPath(bandPath, outlineBlack, style = Stroke(thinStroke))
    }
}

/**
 * Reusable In-Game Hanuman Coach Bar.
 * Listens to the current board state and generates instant contextual tips:
 * - Check warnings
 * - Fish promotion tips at row 6
 * - Special 1st move leaps for King/Queen
 * - Counting rule explanations
 */
@Composable
fun HanumanCoachInGameBar(
    boardState: BoardState,
    selectedPos: Position?,
    playerColor: PieceColor,
    language: AppLanguage,
    onOpenGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Generate context-aware coaching advice
    val coachTip = remember(boardState, selectedPos, language) {
        generateInGameTip(boardState, selectedPos, playerColor, language)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x33D4AF37))
            .border(1.2.dp, Color(0xFFE2C474), RoundedCornerShape(18.dp))
            .clickable { onOpenGuide() }
            .testTag("hanuman_coach_ingame_bar"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFDF8)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Avatar with matching mood
            AnimatedHanumanCoach(
                mood = coachTip.mood,
                size = 46.dp,
                showCrown = true,
                interactiveOnClick = false
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🐵 Hanuman Coach",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF8C5806)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (coachTip.mood) {
                                    HanumanMood.ALERT -> Color(0xFFFEE2E2)
                                    HanumanMood.HAPPY -> Color(0xFFDCFCE7)
                                    HanumanMood.THINKING -> Color(0xFFDBEAFE)
                                    else -> Color(0xFFFEF3C7)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = coachTip.category,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (coachTip.mood) {
                                HanumanMood.ALERT -> Color(0xFFB91C1C)
                                HanumanMood.HAPPY -> Color(0xFF15803D)
                                HanumanMood.THINKING -> Color(0xFF1D4ED8)
                                else -> Color(0xFFB45309)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = coachTip.message,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C1E03),
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = "Xem luật cờ",
                tint = Color(0xFFC89320),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * Generates smart context-driven tips based on real board conditions.
 */
private fun generateInGameTip(
    board: BoardState,
    selectedPos: Position?,
    playerColor: PieceColor,
    language: AppLanguage
): HanumanCoachTip {
    // 1. Game Status specific tips
    if (board.status == GameStatus.CHECK) {
        val isPlayerInCheck = board.currentTurn == playerColor
        return if (isPlayerInCheck) {
            HanumanCoachTip(
                title = "Vua đang bị chiếu!",
                message = when (language) {
                    AppLanguage.VIETNAMESE -> "Cẩn trọng! Vua Khun của bạn đang bị đe dọa. Hãy di chuyển Vua hoặc chặn đường chiếu!"
                    AppLanguage.KHMER -> "ប្រុងប្រយ័ត្ន! ស្ដេចរបស់អ្នកកំពុងរងការគំរាមកំហែង។ ផ្លាស់ទីស្ដេចជាបន្ទាន់!"
                    AppLanguage.FRENCH -> "Attention ! Votre Roi Khun est en échec. Déplacez-le ou parez la menace !"
                    AppLanguage.ENGLISH -> "Warning! Your King is in check. Escape or block the attack immediately!"
                },
                mood = HanumanMood.ALERT,
                category = "Cảnh báo Chiếu Vua"
            )
        } else {
            HanumanCoachTip(
                title = "Đang chiếu tướng!",
                message = when (language) {
                    AppLanguage.VIETNAMESE -> "Đòn tấn công xuất sắc! Đối thủ đang bị chiếu, hãy dồn ép bắt tướng!"
                    AppLanguage.KHMER -> "ការវាយប្រហារដ៏អស្ចារ្យ! បន្តគាបសង្កត់ដើម្បីឈ្នះ!"
                    AppLanguage.FRENCH -> "Excellente attaque ! Maintenez la pression pour mater le Roi !"
                    AppLanguage.ENGLISH -> "Great attacking move! Keep pressing the opponent King!"
                },
                mood = HanumanMood.HAPPY,
                category = "Tấn công dồn dập"
            )
        }
    }

    // 2. Selected Piece Mechanics Tip
    if (selectedPos != null) {
        val piece = board.board[selectedPos.row][selectedPos.col]
        if (piece != null) {
            return when (piece.type) {
                PieceType.PAWN -> {
                    val isNearPromotion = if (piece.color == PieceColor.WHITE) selectedPos.row == 3 else selectedPos.row == 4
                    if (isNearPromotion) {
                        HanumanCoachTip(
                            title = "Phong Cấp Biangai",
                            message = when (language) {
                                AppLanguage.VIETNAMESE -> "Tốt Bia bước sang hàng ${if (piece.color == PieceColor.WHITE) "6" else "3"} sẽ lập tức biến thành Sĩ Biangai!"
                                AppLanguage.KHMER -> "ត្រីបំពងនៅជួរទី ៦ នឹងក្លាយជានាងភ្លាមៗ!"
                                AppLanguage.FRENCH -> "Votre pion Bia sera promu en Reine Biangai dès le rang 6 !"
                                AppLanguage.ENGLISH -> "Your Bia pawn promotes into a Queen/Biangai on rank 6!"
                            },
                            mood = HanumanMood.HAPPY,
                            category = "Phong Cấp Tốt Bia"
                        )
                    } else {
                        HanumanCoachTip(
                            title = "Quân Tốt Bia",
                            message = when (language) {
                                AppLanguage.VIETNAMESE -> "Tốt Bia đi thẳng 1 ô và ăn chéo 1 ô về phía trước."
                                AppLanguage.KHMER -> "ត្រីដើរត្រង់មួយក្រឡា និងស៊ីអង្កត់ទ្រូងទៅមុខ។"
                                AppLanguage.FRENCH -> "Le pion Bia avance de 1 case et capture en diagonale avant."
                                AppLanguage.ENGLISH -> "Bia moves 1 step forward and captures 1 step diagonally forward."
                            },
                            mood = HanumanMood.TALKING,
                            category = "Quy tắc Quân Cờ"
                        )
                    }
                }
                PieceType.PROMOTED_PAWN -> HanumanCoachTip(
                    title = "Sĩ Phong Cấp (Biangai)",
                    message = when (language) {
                        AppLanguage.VIETNAMESE -> "Biangai di chuyển 1 ô theo bất kỳ đường chéo nào (4 hướng)."
                        AppLanguage.KHMER -> "នាងត្រីអាចដើរបាន ៤ ទិសអង្កត់ទ្រូង។"
                        AppLanguage.FRENCH -> "La Reine promue (Biangai) se déplace de 1 case en diagonale (4 directions)."
                        AppLanguage.ENGLISH -> "Promoted Biangai moves 1 step diagonally in all 4 directions."
                    },
                    mood = HanumanMood.TALKING,
                    category = "Quy tắc Quân Cờ"
                )
                PieceType.BISHOP -> HanumanCoachTip(
                    title = "Tượng Koul (Tướng)",
                    message = when (language) {
                        AppLanguage.VIETNAMESE -> "Koul đi được 5 hướng: 1 ô thẳng tới trước và 4 ô theo đường chéo."
                        AppLanguage.KHMER -> "គល់អាចដើរបាន ៥ ទិស: ១ ត្រង់ទៅមុខ និង ៤ អង្កត់ទ្រូង។"
                        AppLanguage.FRENCH -> "Le Khon (Noble) bouge de 1 case vers l'avant ou dans les 4 diagonales (5 directions)."
                        AppLanguage.ENGLISH -> "Koul (Bishop) moves 1 step straight forward or 4 diagonal steps (5 directions)."
                    },
                    mood = HanumanMood.TALKING,
                    category = "Quy tắc Quân Cờ"
                )
                PieceType.QUEEN -> HanumanCoachTip(
                    title = "Sĩ Neang (Hoàng Hậu)",
                    message = when (language) {
                        AppLanguage.VIETNAMESE -> "Neang đi 1 ô chéo. Ở nước đi đầu tiên có thể nhảy 2 ô thẳng về phía trước nếu trống!"
                        AppLanguage.KHMER -> "នាងដើរ ១ ក្រឡាអង្កត់ទ្រូង។ អាចលោត ២ ក្រឡាទៅមុខនៅទឹកដំបូង!"
                        AppLanguage.FRENCH -> "Neang avance de 1 case en diagonale. Premier coup spécial : saut de 2 cases en avant !"
                        AppLanguage.ENGLISH -> "Neang moves 1 step diagonally. Special 1st move: jump 2 steps straight ahead!"
                    },
                    mood = HanumanMood.TALKING,
                    category = "Nước Đi Đặc Biệt"
                )
                PieceType.KING -> HanumanCoachTip(
                    title = "Vua Khun (Ang)",
                    message = when (language) {
                        AppLanguage.VIETNAMESE -> "Khun đi 1 ô mọi hướng. Ở nước đầu tiên chưa bị chiếu, Vua có thể nhảy như Mã!"
                        AppLanguage.KHMER -> "ស្ដេចដើរ ១ ក្រឡាគ្រប់ទិស។ ទឹកដំបូងអាចលោតដូចសេះបាន!"
                        AppLanguage.FRENCH -> "Le Roi se déplace de 1 case. Premier coup spécial : saut de Cavalier si pas en échec !"
                        AppLanguage.ENGLISH -> "The King moves 1 step anywhere. Special 1st move: can jump like a Knight!"
                    },
                    mood = HanumanMood.TALKING,
                    category = "Tuyệt Kỹ Vua"
                )
                PieceType.KNIGHT -> HanumanCoachTip(
                    title = "Mã Ses",
                    message = when (language) {
                        AppLanguage.VIETNAMESE -> "Mã Ses đi hình chữ L (2x1) và có thể nhảy qua mọi quân cờ."
                        AppLanguage.KHMER -> "សេះដើរជើងអក្សរ L និងអាចលោតរំលងកូនផ្សេងទៀតបាន។"
                        AppLanguage.FRENCH -> "Le Cavalier (Ses) se déplace en L et saute par-dessus les pièces."
                        AppLanguage.ENGLISH -> "The Knight (Ses) moves in an L-shape (2x1) jumping over obstacles."
                    },
                    mood = HanumanMood.TALKING,
                    category = "Quy tắc Quân Cờ"
                )
                PieceType.ROOK -> HanumanCoachTip(
                    title = "Thuyền Touk (Xe)",
                    message = when (language) {
                        AppLanguage.VIETNAMESE -> "Thuyền Touk là quân mạnh nhất, đi ngang và dọc không giới hạn khoảng cách."
                        AppLanguage.KHMER -> "ទូកជាកូនខ្លាំងបំផុត ដើរត្រង់ទទឹងនិងបណ្ដោយគ្មានដែនកំណត់។"
                        AppLanguage.FRENCH -> "Le Bateau Touk (Tour) est la pièce la plus puissante, glissant en lignes droites."
                        AppLanguage.ENGLISH -> "The Boat Touk (Rook) is the strongest piece, controlling full rows and columns."
                    },
                    mood = HanumanMood.TALKING,
                    category = "Quy tắc Quân Cờ"
                )
            }
        }
    }

    // 3. Counting Rule Tip
    if (board.countingRule.isCountingActive) {
        return HanumanCoachTip(
            title = "Luật Đếm Hòa Đang Kích Hoạt",
            message = when (language) {
                AppLanguage.VIETNAMESE -> "Còn ${board.countingRule.currentMovesLeft} nước đếm! Hãy bắt Vua đối thủ trước khi hòa!"
                AppLanguage.KHMER -> "នៅសល់ ${board.countingRule.currentMovesLeft} ទឹកទៀត! ស្វែងរកការឈ្នះមុនពេលស្មើ!"
                AppLanguage.FRENCH -> "Plus que ${board.countingRule.currentMovesLeft} coups ! Matez le Roi avant le nul !"
                AppLanguage.ENGLISH -> "${board.countingRule.currentMovesLeft} counting moves left! Checkmate before the draw!"
            },
            mood = HanumanMood.ALERT,
            category = "Luật Đếm Hòa"
        )
    }

    // 4. Default Turn Advice
    val isPlayerTurn = board.currentTurn == playerColor
    return if (isPlayerTurn) {
        HanumanCoachTip(
            title = "Đến Lượt Của Bạn",
            message = when (language) {
                AppLanguage.VIETNAMESE -> "Hãy kiểm soát trung tâm bàn cờ và đẩy các quân Tốt Bia lên hàng 6 để phong cấp!"
                AppLanguage.KHMER -> "គ្រប់គ្រងកណ្តាលក្តារ និងរុញត្រីបំពងឡើងដើម្បីលើកកម្ពស់!"
                AppLanguage.FRENCH -> "Contrôlez le centre et avancez vos pions Bia pour les promouvoir au rang 6 !"
                AppLanguage.ENGLISH -> "Control the center and push your Bia pawns forward for early promotion!"
            },
            mood = HanumanMood.TALKING,
            category = "Chiến lược Mở quân"
        )
    } else {
        HanumanCoachTip(
            title = "Đối thủ đang suy nghĩ",
            message = when (language) {
                AppLanguage.VIETNAMESE -> "Quan sát kỹ các đường tấn công của Xe và Mã của đối phương!"
                AppLanguage.KHMER -> "សង្កេតមើលចលនារបស់ទូកនិងសេះគូប្រកួតដោយប្រុងប្រយ័ត្ន!"
                AppLanguage.FRENCH -> "Observez bien les lignes de Bateaux et Cavaliers adverses !"
                AppLanguage.ENGLISH -> "Carefully watch your opponent's Boat and Knight trajectories!"
            },
            mood = HanumanMood.THINKING,
            category = "Phòng thủ Chiến thuật"
        )
    }
}

/**
 * Interactive Full-Screen Dialog with Coach Hanuman guiding users through all Ouk Chaktrang mechanics.
 */
@Composable
fun HanumanMechanicsGuideModal(
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    var currentChapter by remember { mutableIntStateOf(0) }

    val chapters = remember(language) {
        listOf(
            MechanicsChapter(
                title = when (language) {
                    AppLanguage.VIETNAMESE -> "1. Bàn Cờ & Vị Trí Ban Đầu"
                    AppLanguage.KHMER -> "១. ក្ដារអុក & ការរៀបចំដំបូង"
                    AppLanguage.FRENCH -> "1. L'Échiquier & Placement Initial"
                    AppLanguage.ENGLISH -> "1. Board & Initial Setup"
                },
                subtitle = "8x8 Sandstone Board & Row 3 Pawns",
                description = when (language) {
                    AppLanguage.VIETNAMESE -> "Cờ Ốc Khmer (Ouk Chaktrang) chơi trên bàn cờ 8x8 ô. Điểm đặc biệt nhất: hàng Tốt (Bia) được xếp ở hàng 3 và hàng 6 (thay vì hàng 2/7 như cờ vua phương Tây), tạo ra giao tranh giáp lá cà cực kỳ nhanh và kịch tính ngay từ đầu trận."
                    AppLanguage.KHMER -> "អុកចត្រង្គលេងនៅលើក្ដារ ៨x៨។ ត្រីត្រូវបានរៀបចំនៅជួរទី ៣ និង ៦ ដែលធ្វើឱ្យការប្រកួតចាប់ផ្តើមយ៉ាងរហ័សនិងជក់ចិត្ត។"
                    AppLanguage.FRENCH -> "L'Ouk Chatrang se joue sur un échiquier 8x8. Particularité majeure : les pions (Bia) sont placés sur les rangs 3 et 6, créant un contact rapide et tactique dès l'ouverture."
                    AppLanguage.ENGLISH -> "Ouk Chaktrang is played on an 8x8 board. Uniquely, Bia pawns start directly on ranks 3 and 6, triggering immediate tactical skirmishes from the very start."
                },
                hanumanMood = HanumanMood.TALKING,
                hanumanQuote = when (language) {
                    AppLanguage.VIETNAMESE -> "Chào kỳ thủ! Hãy nhớ: Tốt xếp ở hàng 3 nên thế cờ mở ra rất nhanh!"
                    AppLanguage.KHMER -> "សូមស្វាគមន៍! ត្រីនៅជួរទី ៣ ធ្វើឱ្យការប្រយុទ្ធកើតឡើងភ្លាមៗ!"
                    AppLanguage.FRENCH -> "Bienvenue ! Les pions au rang 3 accélèrent considérablement le jeu !"
                    AppLanguage.ENGLISH -> "Welcome Master! Starting on rank 3 makes the early game fast and thrilling!"
                },
                badgeText = "Quy tắc #1"
            ),
            MechanicsChapter(
                title = when (language) {
                    AppLanguage.VIETNAMESE -> "2. Tốt Bia & Phong Cấp Biangai"
                    AppLanguage.KHMER -> "២. ត្រី & ការបំពងត្រី"
                    AppLanguage.FRENCH -> "2. Pion Bia & Promotion Biangai"
                    AppLanguage.ENGLISH -> "2. Bia Pawn & Biangai Promotion"
                },
                subtitle = "Promotes at Rank 6 (Opponent's Pawn Line)",
                description = when (language) {
                    AppLanguage.VIETNAMESE -> "Tốt Bia đi thẳng 1 ô và ăn chéo 1 ô về phía trước. Ngay khi bước sang hàng của Tốt đối phương (Hàng 6 đối với Trắng, Hàng 3 đối với Đen), Tốt Bia LẬP TỨC được phong cấp thành Biangai (lật úp quân cờ) và có quyền đi 1 ô theo 4 đường chéo."
                    AppLanguage.KHMER -> "ត្រីដើរត្រង់ ១ ក្រឡា និងស៊ីអង្កត់ទ្រូង។ ពេលទៅដល់ជួរទី ៦ វានឹងក្លាយជានាងភ្លាមៗ ហើយអាចដើរ ៤ ទិសអង្កត់ទ្រូង។"
                    AppLanguage.FRENCH -> "Le Bia avance de 1 case et capture en diagonale avant. Dès qu'il atteint le rang 6 (rang 3 pour Noir), il est IMMÉDIATEMENT promu en Biangai (Reine diagonale)."
                    AppLanguage.ENGLISH -> "Bia pawns move 1 step forward and capture diagonally forward. When reaching rank 6 (rank 3 for Black), it IMMEDIATELY promotes into a Biangai with 4 diagonal moves."
                },
                hanumanMood = HanumanMood.HAPPY,
                hanumanQuote = when (language) {
                    AppLanguage.VIETNAMESE -> "Tốt phong cấp thành Biangai là vũ khí lật kèo lợi hại nhất của ta đấy!"
                    AppLanguage.KHMER -> "ការបំពងត្រីជាកូនសោរដ៏សំខាន់ក្នុងការយកឈ្នះ!"
                    AppLanguage.FRENCH -> "Promouvoir vos Bia en Biangai est la clé de la victoire !"
                    AppLanguage.ENGLISH -> "Promoting your Bia into a Biangai is your most lethal endgame weapon!"
                },
                badgeText = "Phong cấp"
            ),
            MechanicsChapter(
                title = when (language) {
                    AppLanguage.VIETNAMESE -> "3. Vua Khun (Ang) & Nước Nhảy Đầu Tiên"
                    AppLanguage.KHMER -> "៣. ស្ដេច & ទឹកលោតដំបូង"
                    AppLanguage.FRENCH -> "3. Le Roi Khun & Saut Initial"
                    AppLanguage.ENGLISH -> "3. King Khun & The First Leap"
                },
                subtitle = "Can jump like a Knight on Move 1",
                description = when (language) {
                    AppLanguage.VIETNAMESE -> "Vua (Khun/Ang) di chuyển 1 ô theo mọi hướng. Đặc biệt trong cờ Ốc truyền thống: nếu Vua chưa từng di chuyển và chưa từng bị chiếu, ở nước đi đầu tiên Vua có thể NHẢY NHƯ MÃ để thoát hiểm hoặc nhập cuộc chiến lược!"
                    AppLanguage.KHMER -> "ស្ដេចដើរ ១ ក្រឡាគ្រប់ទិស។ នៅទឹកដំបូង ប្រសិនបើមិនទាន់រងការគំរាម ស្ដេចអាចលោតដូចសេះបាន!"
                    AppLanguage.FRENCH -> "Le Roi (Khun) bouge de 1 case dans toutes les directions. Règle spéciale : s'il n'a pas bougé ni subi d'échec, son premier coup peut être un saut de Cavalier !"
                    AppLanguage.ENGLISH -> "The King (Khun) moves 1 step anywhere. Special rule: On its very first move (if never moved and not in check), the King can jump like a Knight to safety!"
                },
                hanumanMood = HanumanMood.TALKING,
                hanumanQuote = when (language) {
                    AppLanguage.VIETNAMESE -> "Vua nhảy như Mã sẽ giúp bạn thoát khỏi những đòn chiếu bất ngờ!"
                    AppLanguage.KHMER -> "ការលោតរបស់ស្ដេចជួយឱ្យគេចផុតពីគ្រោះថ្នាក់បានយ៉ាងលឿន!"
                    AppLanguage.FRENCH -> "Le saut initial du Roi vous sauve souvent de situations périlleuses !"
                    AppLanguage.ENGLISH -> "The King's knight leap can quickly position you out of danger!"
                },
                badgeText = "Tuyệt kỹ Vua"
            ),
            MechanicsChapter(
                title = when (language) {
                    AppLanguage.VIETNAMESE -> "4. Tượng Koul (Tướng) - 5 Hướng Vàng"
                    AppLanguage.KHMER -> "៤. គល់ - ចលនា ៥ ទិស"
                    AppLanguage.FRENCH -> "4. Le Khon (Tượng) - 5 Directions"
                    AppLanguage.ENGLISH -> "4. Koul (Bishop) - 5 Noble Steps"
                },
                subtitle = "1 forward + 4 diagonals (Silver General equivalent)",
                description = when (language) {
                    AppLanguage.VIETNAMESE -> "Quân Tượng Koul không đi chéo dài vô tận như cờ vua phương Tây. Koul chỉ đi 1 ô: 1 ô thẳng về phía trước hoặc 4 ô theo đường chéo (tổng cộng 5 hướng). Đây là quân cờ phòng thủ bảo vệ Vua cực kỳ vững chắc."
                    AppLanguage.KHMER -> "គល់ដើរបាន ៥ ទិស (១ ត្រង់ទៅមុខ និង ៤ អង្កត់ទ្រូង)។ ជាកូនការពារស្ដេចយ៉ាងរឹងមាំ។"
                    AppLanguage.FRENCH -> "Le Khon avance d'une case vers l'avant ou dans les 4 diagonales (5 directions au total). C'est le protecteur naturel du Roi."
                    AppLanguage.ENGLISH -> "Koul moves 1 step: 1 step straight forward or 4 diagonal steps (5 directions total). It functions as the premier shield for your King."
                },
                hanumanMood = HanumanMood.MEDITATING,
                hanumanQuote = when (language) {
                    AppLanguage.VIETNAMESE -> "Tượng Koul bảo vệ Vua rất chắc, hãy kết hợp Koul với Mã Ses nhé!"
                    AppLanguage.KHMER -> "គល់ការពារស្ដេចបានយ៉ាងល្អ ប្រើវាការពារតំបន់សំខាន់!"
                    AppLanguage.FRENCH -> "Placez le Khon devant le Roi pour bâtir une forteresse imprenable !"
                    AppLanguage.ENGLISH -> "Station your Koul in front of your King to create an iron stronghold!"
                },
                badgeText = "Quân Tượng"
            ),
            MechanicsChapter(
                title = when (language) {
                    AppLanguage.VIETNAMESE -> "5. Sĩ Neang & Nước Nhảy 2 Ô"
                    AppLanguage.KHMER -> "៥. នាង & ទឹកលោត ២ ក្រឡា"
                    AppLanguage.FRENCH -> "5. Neang (Reine) & Saut de 2 Cases"
                    AppLanguage.ENGLISH -> "5. Queen Neang & 2-Step Leap"
                },
                subtitle = "1 diagonal step, or 2 steps forward on Move 1",
                description = when (language) {
                    AppLanguage.VIETNAMESE -> "Hoàng hậu Neang di chuyển 1 ô theo đường chéo. Ở nước đi đầu tiên của mình, Neang có thể NHẢY VỌT 2 Ô THẲNG VỀ PHÍA TRƯỚC nếu ô ở giữa và ô đích không bị cản!"
                    AppLanguage.KHMER -> "នាងដើរ ១ ក្រឡាអង្កត់ទ្រូង។ នៅទឹកដំបូង អាចលោត ២ ក្រឡាត្រង់ទៅមុខបាន!"
                    AppLanguage.FRENCH -> "Neang se déplace de 1 case en diagonale. À son tout premier coup, elle peut sauter de 2 cases en ligne droite vers l'avant !"
                    AppLanguage.ENGLISH -> "Neang moves 1 step diagonally. On its very first move, Neang can leap 2 squares straight forward if unobstructed!"
                },
                hanumanMood = HanumanMood.TALKING,
                hanumanQuote = when (language) {
                    AppLanguage.VIETNAMESE -> "Nước nhảy 2 ô của Neang giúp nhanh chóng kiểm soát các ô chiến lược!"
                    AppLanguage.KHMER -> "ការលោត ២ ក្រឡារបស់នាងជួយគ្រប់គ្រងកណ្តាលក្តារបានលឿន!"
                    AppLanguage.FRENCH -> "Le saut de 2 cases de Neang surprend souvent l'adversaire !"
                    AppLanguage.ENGLISH -> "Neang's opening 2-step leap seizes vital midfield diagonals rapidly!"
                },
                badgeText = "Quân Sĩ"
            ),
            MechanicsChapter(
                title = when (language) {
                    AppLanguage.VIETNAMESE -> "6. Luật Đếm Hòa Truyền Thống"
                    AppLanguage.KHMER -> "៦. ច្បាប់រាប់ស្មើបុរាណ"
                    AppLanguage.FRENCH -> "6. Règles de Comptage Traditionnel"
                    AppLanguage.ENGLISH -> "6. Traditional Counting Rules"
                },
                subtitle = "64 Board Count & Piece Counts (16/22/32/44/64)",
                description = when (language) {
                    AppLanguage.VIETNAMESE -> "Khi một bên chỉ còn lại Vua (hoặc không còn quân Tốt chưa phong cấp), hệ thống đếm hòa bắt đầu. Bên tấn công phải chiếu hết trong số nước quy định (vd: 2 Xe = 8 nước, 1 Xe = 16 nước, 2 Tượng = 22 nước, 2 Mã = 32 nước, v.v.). Nếu vượt quá, ván cờ xử HÒA!"
                    AppLanguage.KHMER -> "នៅពេលភាគីម្ខាងនៅសល់តែស្ដេច ការរាប់ស្មើចាប់ផ្តើម (ឧ. ២ ទូក = ៨ ទឹក, ១ ទូក = ១៦ ទឹក, ២ សេះ = ៣២ ទឹក)។ បើហួសកំណត់ ស្មើគ្នា!"
                    AppLanguage.FRENCH -> "Quand un joueur n'a plus que son Roi isolé, le décompte s'enclenche (2 Bateaux = 8 coups, 1 Bateau = 16 coups, etc.). Si le temps est écoulé : Nulle !"
                    AppLanguage.ENGLISH -> "When a player has only a lone King remaining, the counting countdown activates (2 Rooks = 8 moves, 1 Rook = 16 moves, etc.). If exceeded, the match is a DRAW!"
                },
                hanumanMood = HanumanMood.ALERT,
                hanumanQuote = when (language) {
                    AppLanguage.VIETNAMESE -> "Đừng để bị hết nước đếm! Hãy phối hợp Xe và Tượng để khóa Vua thật nhanh!"
                    AppLanguage.KHMER -> "ប្រយ័ត្នអស់ទឹកដេញ! ត្រូវប្រើទូកនិងគល់រួមគ្នាដើម្បីបញ្ចប់ការប្រកួត!"
                    AppLanguage.FRENCH -> "Ne laissez pas le décompte expirer ! Coordonnez Bateau et Tour pour mater vite !"
                    AppLanguage.ENGLISH -> "Keep an eye on the move counter! Coordinate Boat and Knight swiftly!"
                },
                badgeText = "Đếm Hòa"
            )
        )
    }

    val current = chapters[currentChapter]

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color(0x66D4AF37))
                .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFFFFDF8)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${currentChapter + 1}/${chapters.size}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2C1E03)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = current.badgeText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8C5806)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1E7CF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF3B2D19),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Animated Hanuman Mascot with dynamic mood
                AnimatedHanumanCoach(
                    mood = current.hanumanMood,
                    size = 80.dp,
                    showCrown = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Hanuman Quote Bubble
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFFF8E6), Color(0xFFFEEFC3))
                            )
                        )
                        .border(1.dp, Color(0xFFE2C474), RoundedCornerShape(14.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🐵",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "\"${current.hanumanQuote}\"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF78350F),
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Chapter Title
                Text(
                    text = current.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Chapter Detailed Description
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8EE))
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        item {
                            Text(
                                text = current.description,
                                fontSize = 12.5.sp,
                                color = Color(0xFF334155),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation Controls (Back / Next / Finish)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentChapter > 0) {
                        OutlinedButton(
                            onClick = { currentChapter-- },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF78350F)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFD4AF37))
                        ) {
                            Text("← Trước", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    if (currentChapter < chapters.size - 1) {
                        Button(
                            onClick = { currentChapter++ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFC89320)
                            )
                        ) {
                            Text("Tiếp theo →", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = JadeEmerald
                            )
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Đã hiểu luật!", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Reusable Hanuman Coach Card specifically designed for Tactics & Lesson mode.
 */
@Composable
fun HanumanTacticsCoachCard(
    lessonTitle: String,
    hintText: String,
    isSolved: Boolean,
    onShowFullGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x33D4AF37))
            .border(1.2.dp, Color(0xFFE2C474), RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFDF8)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedHanumanCoach(
                mood = if (isSolved) HanumanMood.HAPPY else HanumanMood.THINKING,
                size = 54.dp,
                showCrown = true,
                onClick = onShowFullGuide
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isSolved) "🐵 Tuyệt vời! Bạn đã tìm đúng nước đi!" else "🐵 Huấn luyện viên Hanuman",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSolved) Color(0xFF15803D) else Color(0xFF8C5806)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = if (isSolved) "Bạn đã giải quyết xuất sắc bài tập này!" else hintText,
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onShowFullGuide,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7))
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Mẹo hướng dẫn",
                    tint = Color(0xFFC89320),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Data structure for interactive rules guide chapter
 */
private data class MechanicsChapter(
    val title: String,
    val subtitle: String,
    val description: String,
    val hanumanMood: HanumanMood,
    val hanumanQuote: String,
    val badgeText: String
)
