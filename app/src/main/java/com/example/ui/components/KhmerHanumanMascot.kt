package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.localization.AppStrings

/**
 * High-fidelity Vector Art of Hanuman - the revered Monkey Warrior King in Cambodian Khmer Reamker tradition.
 * Portrayed with authentic golden tiered Mukuta crown, white fur, royal jewelry, and friendly expressive eyes.
 */
@Composable
fun HanumanAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    isSmiling: Boolean = true,
    showCrown: Boolean = true
) {
    KhmerAppLogo(
        modifier = modifier,
        size = size,
        showOuterBorder = true
    )
}

/**
 * Image 1: Full-body Standing Hanuman Warrior Mascot holding the golden chess piece aloft in his right hand.
 * Complete with traditional martial posture, golden Mukuta crown, white fur with gold patterns,
 * royal red-and-green embroidered Khmer sampot attire, golden jewelry, and elegant arched tail.
 */
@Composable
fun HanumanWarriorStanding(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val cx = w * 0.48f

            val goldMain = Color(0xFFF59E0B)
            val goldBright = Color(0xFFFDE68A)
            val goldDark = Color(0xFFB45309)
            val furWhite = Color(0xFFFFFFFF)
            val furShadow = Color(0xFFE2E8F0)
            val greenMask = Color(0xFF15803D)
            val greenDark = Color(0xFF166534)
            val redPattern = Color(0xFF991B1B)
            val outline = Color(0xFF1E293B)
            val strokeW = w * 0.014f
            val thinW = w * 0.009f

            // 1. Elegantly Curled Monkey Tail with Golden Flame Tip (Behind)
            val tailPath = Path().apply {
                moveTo(cx + w * 0.12f, h * 0.55f)
                cubicTo(
                    cx + w * 0.38f, h * 0.56f,
                    cx + w * 0.42f, h * 0.36f,
                    cx + w * 0.30f, h * 0.28f
                )
                cubicTo(
                    cx + w * 0.22f, h * 0.22f,
                    cx + w * 0.32f, h * 0.18f,
                    cx + w * 0.34f, h * 0.24f
                )
                cubicTo(
                    cx + w * 0.38f, h * 0.34f,
                    cx + w * 0.30f, h * 0.48f,
                    cx + w * 0.14f, h * 0.58f
                )
                close()
            }
            drawPath(tailPath, furWhite)
            drawPath(tailPath, outline, style = Stroke(strokeW, join = StrokeJoin.Round))

            // Golden Flame Tail Tip
            val flamePath = Path().apply {
                moveTo(cx + w * 0.28f, h * 0.26f)
                cubicTo(cx + w * 0.34f, h * 0.20f, cx + w * 0.38f, h * 0.22f, cx + w * 0.33f, h * 0.28f)
                cubicTo(cx + w * 0.36f, h * 0.32f, cx + w * 0.30f, h * 0.36f, cx + w * 0.28f, h * 0.26f)
                close()
            }
            drawPath(flamePath, goldMain)
            drawPath(flamePath, outline, style = Stroke(thinW))

            // 2. Legs & Feet in Wide Martial Reamker Stance
            // Left Leg (Viewer's Left)
            val leftLeg = Path().apply {
                moveTo(cx - w * 0.22f, h * 0.58f)
                lineTo(cx - w * 0.32f, h * 0.70f)
                lineTo(cx - w * 0.24f, h * 0.82f)
                lineTo(cx - w * 0.14f, h * 0.68f)
                close()
            }
            drawPath(leftLeg, greenDark)
            drawPath(leftLeg, outline, style = Stroke(strokeW))

            // Left Leg Gold Calf Wrap & Ankle Bangle
            drawRoundRect(
                color = goldMain,
                topLeft = Offset(cx - w * 0.28f, h * 0.78f),
                size = Size(w * 0.12f, h * 0.05f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = outline,
                topLeft = Offset(cx - w * 0.28f, h * 0.78f),
                size = Size(w * 0.12f, h * 0.05f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f),
                style = Stroke(thinW)
            )

            // Left Foot (Spread Toes)
            val leftFoot = Path().apply {
                moveTo(cx - w * 0.32f, h * 0.88f)
                cubicTo(cx - w * 0.36f, h * 0.90f, cx - w * 0.34f, h * 0.93f, cx - w * 0.20f, h * 0.93f)
                cubicTo(cx - w * 0.16f, h * 0.93f, cx - w * 0.18f, h * 0.86f, cx - w * 0.24f, h * 0.84f)
                close()
            }
            drawPath(leftFoot, furWhite)
            drawPath(leftFoot, outline, style = Stroke(strokeW))

            // Right Leg (Viewer's Right)
            val rightLeg = Path().apply {
                moveTo(cx + w * 0.12f, h * 0.58f)
                lineTo(cx + w * 0.24f, h * 0.70f)
                lineTo(cx + w * 0.20f, h * 0.82f)
                lineTo(cx + w * 0.06f, h * 0.68f)
                close()
            }
            drawPath(rightLeg, greenDark)
            drawPath(rightLeg, outline, style = Stroke(strokeW))

            // Right Leg Gold Calf Wrap & Ankle Bangle
            drawRoundRect(
                color = goldMain,
                topLeft = Offset(cx + w * 0.14f, h * 0.78f),
                size = Size(w * 0.12f, h * 0.05f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
            drawRoundRect(
                color = outline,
                topLeft = Offset(cx + w * 0.14f, h * 0.78f),
                size = Size(w * 0.12f, h * 0.05f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f),
                style = Stroke(thinW)
            )

            // Right Foot
            val rightFoot = Path().apply {
                moveTo(cx + w * 0.18f, h * 0.84f)
                cubicTo(cx + w * 0.16f, h * 0.92f, cx + w * 0.22f, h * 0.93f, cx + w * 0.32f, h * 0.93f)
                cubicTo(cx + w * 0.36f, h * 0.91f, cx + w * 0.32f, h * 0.86f, cx + w * 0.24f, h * 0.84f)
                close()
            }
            drawPath(rightFoot, furWhite)
            drawPath(rightFoot, outline, style = Stroke(strokeW))

            // 3. Royal Khmer Sampot Center Apron (Red & RoyalGold)
            val sampotCenter = Path().apply {
                moveTo(cx - w * 0.14f, h * 0.50f)
                lineTo(cx + w * 0.14f, h * 0.50f)
                lineTo(cx + w * 0.12f, h * 0.68f)
                lineTo(cx, h * 0.76f)
                lineTo(cx - w * 0.12f, h * 0.68f)
                close()
            }
            drawPath(sampotCenter, redPattern)
            drawPath(sampotCenter, outline, style = Stroke(strokeW))

            // Gold floral pattern on red sampot
            drawCircle(color = goldBright, radius = w * 0.025f, center = Offset(cx, h * 0.60f))
            drawCircle(color = goldBright, radius = w * 0.018f, center = Offset(cx, h * 0.68f))

            // Golden Waist Belt & Center Gem
            drawRoundRect(
                color = goldMain,
                topLeft = Offset(cx - w * 0.18f, h * 0.46f),
                size = Size(w * 0.36f, h * 0.06f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
            drawCircle(color = goldDark, radius = w * 0.035f, center = Offset(cx, h * 0.49f))
            drawCircle(color = Color(0xFFDC2626), radius = w * 0.020f, center = Offset(cx, h * 0.49f))

            // 4. White Torso with Royal Gold Chest Sashes (Sangvar)
            val torsoPath = Path().apply {
                moveTo(cx - w * 0.18f, h * 0.28f)
                lineTo(cx + w * 0.18f, h * 0.28f)
                lineTo(cx + w * 0.16f, h * 0.48f)
                lineTo(cx - w * 0.16f, h * 0.48f)
                close()
            }
            drawPath(torsoPath, furWhite)
            drawPath(torsoPath, outline, style = Stroke(strokeW))

            // Crossed Golden Sashes with Rubies
            val sashLeft = Path().apply {
                moveTo(cx - w * 0.16f, h * 0.28f)
                lineTo(cx + w * 0.14f, h * 0.48f)
                lineTo(cx + w * 0.08f, h * 0.48f)
                lineTo(cx - w * 0.18f, h * 0.32f)
                close()
            }
            drawPath(sashLeft, goldMain)
            drawPath(sashLeft, outline, style = Stroke(thinW))

            val sashRight = Path().apply {
                moveTo(cx + w * 0.16f, h * 0.28f)
                lineTo(cx - w * 0.14f, h * 0.48f)
                lineTo(cx - w * 0.08f, h * 0.48f)
                lineTo(cx + w * 0.18f, h * 0.32f)
                close()
            }
            drawPath(sashRight, goldMain)
            drawPath(sashRight, outline, style = Stroke(thinW))

            // Chest Center Medallion
            drawCircle(color = goldBright, radius = w * 0.035f, center = Offset(cx, h * 0.36f))
            drawCircle(color = Color(0xFFDC2626), radius = w * 0.018f, center = Offset(cx, h * 0.36f))

            // 5. Left Arm (Viewer's Left - Resting with Martial Fist)
            val leftArm = Path().apply {
                moveTo(cx - w * 0.16f, h * 0.28f)
                cubicTo(cx - w * 0.32f, h * 0.28f, cx - w * 0.38f, h * 0.42f, cx - w * 0.22f, h * 0.50f)
                lineTo(cx - w * 0.18f, h * 0.45f)
                cubicTo(cx - w * 0.28f, h * 0.40f, cx - w * 0.26f, h * 0.32f, cx - w * 0.16f, h * 0.32f)
                close()
            }
            drawPath(leftArm, furWhite)
            drawPath(leftArm, outline, style = Stroke(strokeW))

            // Left Gold Armband & Wrist Bangles
            drawRoundRect(
                color = goldMain,
                topLeft = Offset(cx - w * 0.34f, h * 0.30f),
                size = Size(w * 0.08f, h * 0.04f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
            )

            // Left Hand Fist
            drawCircle(color = furWhite, radius = w * 0.045f, center = Offset(cx - w * 0.22f, h * 0.49f))
            drawCircle(color = outline, radius = w * 0.045f, center = Offset(cx - w * 0.22f, h * 0.49f), style = Stroke(strokeW))

            // 6. Right Arm (Viewer's Right - Raised High Holding Chess Piece)
            val rightArm = Path().apply {
                moveTo(cx + w * 0.16f, h * 0.28f)
                cubicTo(cx + w * 0.32f, h * 0.30f, cx + w * 0.36f, h * 0.44f, cx + w * 0.24f, h * 0.52f)
                lineTo(cx + w * 0.20f, h * 0.46f)
                cubicTo(cx + w * 0.28f, h * 0.42f, cx + w * 0.26f, h * 0.32f, cx + w * 0.16f, h * 0.32f)
                close()
            }
            drawPath(rightArm, furWhite)
            drawPath(rightArm, outline, style = Stroke(strokeW))

            // Right Raised Forearm holding Piece Aloft
            val rightForearm = Path().apply {
                moveTo(cx - w * 0.28f, h * 0.26f)
                cubicTo(cx - w * 0.36f, h * 0.22f, cx - w * 0.38f, h * 0.14f, cx - w * 0.30f, h * 0.12f)
                lineTo(cx - w * 0.24f, h * 0.16f)
                close()
            }
            drawPath(rightForearm, furWhite)

            // Right Hand Fingers Pinching Golden Chess Piece (Conch)
            drawCircle(color = furWhite, radius = w * 0.04f, center = Offset(cx - w * 0.28f, h * 0.15f))
            drawCircle(color = outline, radius = w * 0.04f, center = Offset(cx - w * 0.28f, h * 0.15f), style = Stroke(strokeW))

            // Golden Conch Tiered Chess Piece Held High
            val piecePath = Path().apply {
                moveTo(cx - w * 0.28f, h * 0.05f)
                cubicTo(cx - w * 0.25f, h * 0.08f, cx - w * 0.25f, h * 0.11f, cx - w * 0.28f, h * 0.14f)
                cubicTo(cx - w * 0.31f, h * 0.11f, cx - w * 0.31f, h * 0.08f, cx - w * 0.28f, h * 0.05f)
                close()
            }
            drawPath(piecePath, goldBright)
            drawPath(piecePath, outline, style = Stroke(thinW))

            // 3 Energy Glow Rays Above Piece (Image 1 Signature)
            val rayCenter = Offset(cx - w * 0.28f, h * 0.02f)
            val rayLeft = Offset(cx - w * 0.34f, h * 0.04f)
            val rayRight = Offset(cx - w * 0.22f, h * 0.04f)
            drawLine(goldMain, Offset(cx - w * 0.28f, h * 0.04f), rayCenter, strokeWidth = w * 0.025f, cap = StrokeCap.Round)
            drawLine(goldMain, Offset(cx - w * 0.31f, h * 0.06f), rayLeft, strokeWidth = w * 0.025f, cap = StrokeCap.Round)
            drawLine(goldMain, Offset(cx - w * 0.25f, h * 0.06f), rayRight, strokeWidth = w * 0.025f, cap = StrokeCap.Round)

            // 7. Hanuman Head with Green Mask, Roaring Smile & Mukuta Crown
            // Head Shape
            drawCircle(color = furWhite, radius = w * 0.15f, center = Offset(cx, h * 0.18f))
            drawCircle(color = outline, radius = w * 0.15f, center = Offset(cx, h * 0.18f), style = Stroke(strokeW))

            // Green Reamker Eye Mask
            val maskPath = Path().apply {
                moveTo(cx - w * 0.12f, h * 0.15f)
                cubicTo(cx - w * 0.06f, h * 0.10f, cx + w * 0.06f, h * 0.10f, cx + w * 0.12f, h * 0.15f)
                cubicTo(cx + w * 0.10f, h * 0.22f, cx - w * 0.10f, h * 0.22f, cx - w * 0.12f, h * 0.15f)
                close()
            }
            drawPath(maskPath, greenMask)
            drawPath(maskPath, outline, style = Stroke(thinW))

            // Big Almond Eyes with Dark Pupils
            drawOval(color = Color.White, topLeft = Offset(cx - w * 0.09f, h * 0.13f), size = Size(w * 0.06f, h * 0.05f))
            drawCircle(color = outline, radius = w * 0.018f, center = Offset(cx - w * 0.06f, h * 0.15f))
            drawOval(color = Color.White, topLeft = Offset(cx + w * 0.03f, h * 0.13f), size = Size(w * 0.06f, h * 0.05f))
            drawCircle(color = outline, radius = w * 0.018f, center = Offset(cx + w * 0.06f, h * 0.15f))

            // Open Roaring Smile with Teeth & Fangs
            val mouthPath = Path().apply {
                moveTo(cx - w * 0.08f, h * 0.19f)
                cubicTo(cx, h * 0.26f, cx + w * 0.08f, h * 0.19f, cx + w * 0.08f, h * 0.19f)
                cubicTo(cx, h * 0.22f, cx - w * 0.08f, h * 0.19f, cx - w * 0.08f, h * 0.19f)
                close()
            }
            drawPath(mouthPath, Color(0xFF7F1D1D))
            drawPath(mouthPath, outline, style = Stroke(thinW))

            // Golden Mukuta Spire Crown
            val crownPath = Path().apply {
                moveTo(cx - w * 0.14f, h * 0.12f)
                lineTo(cx - w * 0.08f, h * 0.05f)
                lineTo(cx, h * 0.02f)
                lineTo(cx + w * 0.08f, h * 0.05f)
                lineTo(cx + w * 0.14f, h * 0.12f)
                close()
            }
            drawPath(crownPath, goldMain)
            drawPath(crownPath, outline, style = Stroke(thinW))
            drawCircle(color = Color(0xFFDC2626), radius = w * 0.018f, center = Offset(cx, h * 0.07f))
        }
    }
}

/**
 * Full interactive Hanuman Mascot Hero Card with speech bubble dialog and Playing Mascot.
 */
@Composable
fun HanumanMascotBanner(
    language: AppLanguage,
    onQuickPlay: () -> Unit,
    onOpenGuide: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val greetingText = when (language) {
        AppLanguage.VIETNAMESE -> "Chào kỳ thủ! Ta là Hanuman. Hôm nay bạn muốn so tài cùng ai?"
        AppLanguage.KHMER -> "សូមស្វាគមន៍! ខ្ញុំគឺហនុមាន។ តើអ្នកចង់ប្រកួតជាមួយនរណានៅថ្ងៃនេះ?"
        AppLanguage.FRENCH -> "Salutations! Je suis Hanuman. Prêt pour une partie d'Ouk Chatrang?"
        AppLanguage.ENGLISH -> "Greetings, Master! I am Hanuman. Ready for an epic game of Khmer Chess?"
    }

    val subtitleTip = when (language) {
        AppLanguage.VIETNAMESE -> "Mẹo: Tốt Bia phong cấp thành Biangai ở hàng thứ 6 sẽ giúp lật ngược thế cờ! (Chạm để xem hướng dẫn luật cờ)"
        AppLanguage.KHMER -> "គន្លឹះ: ត្រីបំពងនៅជួរទី ៦ អាចជួយផ្លាស់ប្តូរស្ថានការណ៍ប្រកួត! (ចុចដើម្បីមើលច្បាប់)"
        AppLanguage.FRENCH -> "Astuce : Promouvez votre pion Bia au 6ème rang ! (Appuyez pour le guide)"
        AppLanguage.ENGLISH -> "Pro Tip: Promoting your Bia pawn on the 6th rank is key! (Tap to open Rules Guide)"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x33D4AF37))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFFDF5),
                        Color(0xFFFFF5DC),
                        Color(0xFFF9E7B9)
                    )
                )
            )
            .border(1.5.dp, Color(0xFFE2C474), RoundedCornerShape(20.dp))
            .clickable(enabled = onOpenGuide != null) { onOpenGuide?.invoke() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hanuman Mascot Playing at Chessboard (Row 2 Col 3 / Row 5 Col 1)
            KhmerMascotPlaying(
                size = 72.dp,
                modifier = Modifier.shadow(2.dp, CircleShape)
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Speech Bubble / Dialogue
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🐵 Hanuman Coach",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF8C5806)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFD4AF37))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Khmer Mascot",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C1E03)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = greetingText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3B2D19),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitleTip,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF7A6242),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

/**
 * Draws the vector paths for Khmer Hanuman character.
 */
private fun DrawScope.drawHanumanVector(
    cx: Float,
    cy: Float,
    r: Float,
    isSmiling: Boolean,
    showCrown: Boolean
) {
    val goldCrownColor = Color(0xFFFFD54F)
    val goldDarkColor = Color(0xFFC79200)
    val redRubyColor = Color(0xFFE53935)
    val furWhite = Color(0xFFFFFFFF)
    val furShade = Color(0xFFE2DCD2)
    val facePeach = Color(0xFFFFE0B2)
    val outlineBlack = Color(0xFF261C14)
    val strokeWidth = 2.4.dp.toPx()
    val thinStroke = 1.4.dp.toPx()

    // 1. Monkey Ears (Left & Right)
    val earRadius = r * 0.28f
    val earOffsetY = cy - r * 0.05f

    // Left Ear
    drawCircle(
        color = furWhite,
        radius = earRadius,
        center = Offset(cx - r * 0.68f, earOffsetY)
    )
    drawCircle(
        color = Color(0xFFFFCDD2),
        radius = earRadius * 0.6f,
        center = Offset(cx - r * 0.68f, earOffsetY)
    )
    drawCircle(
        color = outlineBlack,
        radius = earRadius,
        center = Offset(cx - r * 0.68f, earOffsetY),
        style = Stroke(strokeWidth)
    )
    // Left Golden Earring
    drawCircle(
        color = goldCrownColor,
        radius = earRadius * 0.35f,
        center = Offset(cx - r * 0.85f, earOffsetY + earRadius * 0.7f)
    )
    drawCircle(
        color = outlineBlack,
        radius = earRadius * 0.35f,
        center = Offset(cx - r * 0.85f, earOffsetY + earRadius * 0.7f),
        style = Stroke(thinStroke)
    )

    // Right Ear
    drawCircle(
        color = furWhite,
        radius = earRadius,
        center = Offset(cx + r * 0.68f, earOffsetY)
    )
    drawCircle(
        color = Color(0xFFFFCDD2),
        radius = earRadius * 0.6f,
        center = Offset(cx + r * 0.68f, earOffsetY)
    )
    drawCircle(
        color = outlineBlack,
        radius = earRadius,
        center = Offset(cx + r * 0.68f, earOffsetY),
        style = Stroke(strokeWidth)
    )
    // Right Golden Earring
    drawCircle(
        color = goldCrownColor,
        radius = earRadius * 0.35f,
        center = Offset(cx + r * 0.85f, earOffsetY + earRadius * 0.7f)
    )
    drawCircle(
        color = outlineBlack,
        radius = earRadius * 0.35f,
        center = Offset(cx + r * 0.85f, earOffsetY + earRadius * 0.7f),
        style = Stroke(thinStroke)
    )

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
        // Left cheek up
        cubicTo(
            cx - r * 0.45f, cy + r * 0.5f,
            cx - r * 0.52f, cy + r * 0.05f,
            cx - r * 0.35f, cy - r * 0.15f
        )
        // Left brow
        cubicTo(
            cx - r * 0.2f, cy - r * 0.32f,
            cx - r * 0.05f, cy - r * 0.15f,
            cx, cy - r * 0.05f
        )
        // Right brow
        cubicTo(
            cx + r * 0.05f, cy - r * 0.15f,
            cx + r * 0.2f, cy - r * 0.32f,
            cx + r * 0.35f, cy - r * 0.15f
        )
        // Right cheek down
        cubicTo(
            cx + r * 0.52f, cy + r * 0.05f,
            cx + r * 0.45f, cy + r * 0.5f,
            cx, cy + r * 0.55f
        )
        close()
    }
    drawPath(faceMaskPath, facePeach)
    drawPath(faceMaskPath, outlineBlack, style = Stroke(thinStroke))

    // 4. Expressive Eyes
    val eyeW = r * 0.13f
    val eyeH = r * 0.17f
    val eyeY = cy + r * 0.04f

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
    drawCircle(
        color = Color(0xFF3E2723),
        radius = eyeW * 0.38f,
        center = Offset(cx - r * 0.25f, eyeY)
    )
    drawCircle(
        color = Color.White,
        radius = eyeW * 0.15f,
        center = Offset(cx - r * 0.27f, eyeY - eyeH * 0.2f)
    )

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
    drawCircle(
        color = Color(0xFF3E2723),
        radius = eyeW * 0.38f,
        center = Offset(cx + r * 0.25f, eyeY)
    )
    drawCircle(
        color = Color.White,
        radius = eyeW * 0.15f,
        center = Offset(cx + r * 0.23f, eyeY - eyeH * 0.2f)
    )

    // Brows
    val leftBrow = Path().apply {
        moveTo(cx - r * 0.36f, eyeY - r * 0.15f)
        quadraticTo(cx - r * 0.25f, eyeY - r * 0.25f, cx - r * 0.12f, eyeY - r * 0.13f)
    }
    val rightBrow = Path().apply {
        moveTo(cx + r * 0.12f, eyeY - r * 0.13f)
        quadraticTo(cx + r * 0.25f, eyeY - r * 0.25f, cx + r * 0.36f, eyeY - r * 0.15f)
    }
    drawPath(leftBrow, outlineBlack, style = Stroke(strokeWidth, cap = StrokeCap.Round))
    drawPath(rightBrow, outlineBlack, style = Stroke(strokeWidth, cap = StrokeCap.Round))

    // 5. Snout & Smile
    // Snout
    val snoutPath = Path().apply {
        moveTo(cx - r * 0.1f, cy + r * 0.22f)
        quadraticTo(cx, cy + r * 0.18f, cx + r * 0.1f, cy + r * 0.22f)
        quadraticTo(cx, cy + r * 0.28f, cx - r * 0.1f, cy + r * 0.22f)
    }
    drawPath(snoutPath, Color(0xFF8D6E63))

    // Smile
    val smilePath = Path().apply {
        moveTo(cx - r * 0.24f, cy + r * 0.36f)
        quadraticTo(cx, cy + if (isSmiling) r * 0.52f else r * 0.38f, cx + r * 0.24f, cy + r * 0.36f)
    }
    drawPath(smilePath, outlineBlack, style = Stroke(strokeWidth, cap = StrokeCap.Round))

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
            // Left crown tier
            lineTo(cx - r * 0.38f, crownBaseY - r * 0.22f)
            lineTo(cx - r * 0.42f, crownBaseY - r * 0.23f)
            lineTo(cx - r * 0.25f, crownBaseY - r * 0.45f)
            lineTo(cx - r * 0.28f, crownBaseY - r * 0.46f)
            // Main Spire to Top
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

        // Fill Crown Gold
        drawPath(crownPath, goldCrownColor)
        drawPath(crownPath, outlineBlack, style = Stroke(strokeWidth, join = StrokeJoin.Round))

        // Crown Central Red Ruby Gem
        drawCircle(
            color = redRubyColor,
            radius = r * 0.09f,
            center = Offset(cx, crownBaseY - r * 0.16f)
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
