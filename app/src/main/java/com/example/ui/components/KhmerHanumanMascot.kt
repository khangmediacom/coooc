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
