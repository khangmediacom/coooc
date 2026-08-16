package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Luminous Angkor Wat & Bayon Serene Faces Background with subtle soft opacity (mờ nhẹ).
 * Features:
 * 1. Warm ivory-to-sandstone daylight vertical gradient canvas.
 * 2. Golden radiant morning aura in the upper-mid region.
 * 3. Ancient Angkor Wat towers in soft atmospheric mist.
 * 4. Serene Bayon smiling stone face monument and delicate Khmer flame/lotus filigrees at low opacity (10-18%).
 */
@Composable
fun AngkorWarmBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFDF7), // Pure radiant warm ivory
            Color(0xFFF8F3E6), // Soft sandstone cream
            Color(0xFFEFE4CC), // Warm golden amber lotus moat
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Radiant Morning Sun Aura behind Angkor Wat
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFDF80).copy(alpha = 0.30f),
                        Color(0xFFFFD54F).copy(alpha = 0.14f),
                        Color(0xFFFFF8E1).copy(alpha = 0.04f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.45f),
                    radius = w * 0.70f
                ),
                radius = w * 0.70f,
                center = Offset(w * 0.5f, h * 0.45f)
            )

            // 2. Angkor Wat Silhouette in Background Mist (Low Opacity)
            val templeBaseY = h * 0.48f
            val sandstoneSilhouette = Color(0xFFC79E5C).copy(alpha = 0.16f)
            val strokeColor = Color(0xFFA57C36).copy(alpha = 0.20f)

            fun drawAngkorTower(
                centerX: Float,
                baseY: Float,
                towerW: Float,
                towerH: Float,
                fillColor: Color
            ) {
                val path = Path().apply {
                    val halfW = towerW / 2f
                    moveTo(centerX - halfW, baseY)
                    lineTo(centerX - halfW * 0.88f, baseY - towerH * 0.22f)
                    lineTo(centerX - halfW * 0.94f, baseY - towerH * 0.25f)
                    lineTo(centerX - halfW * 0.78f, baseY - towerH * 0.48f)
                    lineTo(centerX - halfW * 0.84f, baseY - towerH * 0.52f)
                    lineTo(centerX - halfW * 0.66f, baseY - towerH * 0.70f)
                    cubicTo(
                        centerX - halfW * 0.45f, baseY - towerH * 0.88f,
                        centerX - halfW * 0.12f, baseY - towerH * 0.98f,
                        centerX, baseY - towerH
                    )
                    cubicTo(
                        centerX + halfW * 0.12f, baseY - towerH * 0.98f,
                        centerX + halfW * 0.45f, baseY - towerH * 0.88f,
                        centerX + halfW * 0.66f, baseY - towerH * 0.70f
                    )
                    lineTo(centerX + halfW * 0.84f, baseY - towerH * 0.52f)
                    lineTo(centerX + halfW * 0.78f, baseY - towerH * 0.48f)
                    lineTo(centerX + halfW * 0.94f, baseY - towerH * 0.25f)
                    lineTo(centerX + halfW * 0.88f, baseY - towerH * 0.22f)
                    lineTo(centerX + halfW, baseY)
                    close()
                }
                drawPath(path, fillColor)
                drawPath(path, strokeColor, style = Stroke(1.0.dp.toPx()))
            }

            // 5 Iconic Angkor Wat Spire Towers
            drawAngkorTower(w * 0.18f, templeBaseY, w * 0.12f, h * 0.12f, sandstoneSilhouette)
            drawAngkorTower(w * 0.33f, templeBaseY, w * 0.15f, h * 0.18f, sandstoneSilhouette)
            drawAngkorTower(w * 0.50f, templeBaseY, w * 0.20f, h * 0.25f, Color(0xFFD4AF37).copy(alpha = 0.22f))
            drawAngkorTower(w * 0.67f, templeBaseY, w * 0.15f, h * 0.18f, sandstoneSilhouette)
            drawAngkorTower(w * 0.82f, templeBaseY, w * 0.12f, h * 0.12f, sandstoneSilhouette)

            // 3. Central Bayon Serene Stone Faces & Lotus Crown (Translucent Warm Gold ~ 12-16% opacity)
            val bayonCenterX = w * 0.5f
            val bayonCenterY = h * 0.68f
            val bayonWidth = w * 0.65f
            val bayonHeight = h * 0.32f
            val bayonGold = Color(0xFFB48528).copy(alpha = 0.13f)
            val bayonLine = Color(0xFF8C5E1A).copy(alpha = 0.18f)

            // Bayon Lotus Crown Top
            val crownPath = Path().apply {
                moveTo(bayonCenterX - bayonWidth * 0.32f, bayonCenterY - bayonHeight * 0.38f)
                cubicTo(
                    bayonCenterX - bayonWidth * 0.20f, bayonCenterY - bayonHeight * 0.52f,
                    bayonCenterX + bayonWidth * 0.20f, bayonCenterY - bayonHeight * 0.52f,
                    bayonCenterX + bayonWidth * 0.32f, bayonCenterY - bayonHeight * 0.38f
                )
                lineTo(bayonCenterX + bayonWidth * 0.22f, bayonCenterY - bayonHeight * 0.28f)
                lineTo(bayonCenterX - bayonWidth * 0.22f, bayonCenterY - bayonHeight * 0.28f)
                close()
            }
            drawPath(crownPath, bayonGold)
            drawPath(crownPath, bayonLine, style = Stroke(1.2.dp.toPx()))

            // Bayon Stone Tower Pillar
            val pillarPath = Path().apply {
                moveTo(bayonCenterX - bayonWidth * 0.36f, bayonCenterY - bayonHeight * 0.28f)
                lineTo(bayonCenterX + bayonWidth * 0.36f, bayonCenterY - bayonHeight * 0.28f)
                lineTo(bayonCenterX + bayonWidth * 0.32f, bayonCenterY + bayonHeight * 0.45f)
                lineTo(bayonCenterX - bayonWidth * 0.32f, bayonCenterY + bayonHeight * 0.45f)
                close()
            }
            drawPath(pillarPath, bayonGold)
            drawPath(pillarPath, bayonLine, style = Stroke(1.2.dp.toPx()))

            // Serene Smiling Face - Left & Right Contours
            fun drawSereneFace(faceCenterX: Float, faceY: Float, scale: Float) {
                val fw = bayonWidth * 0.32f * scale
                val fh = bayonHeight * 0.42f * scale

                // Eyebrow curves
                val browPath = Path().apply {
                    moveTo(faceCenterX - fw * 0.35f, faceY - fh * 0.12f)
                    cubicTo(
                        faceCenterX - fw * 0.15f, faceY - fh * 0.22f,
                        faceCenterX + fw * 0.15f, faceY - fh * 0.22f,
                        faceCenterX + fw * 0.35f, faceY - fh * 0.12f
                    )
                }
                drawPath(browPath, bayonLine, style = Stroke(1.5.dp.toPx()))

                // Serene Closed Eyes (Meditation)
                val leftEye = Path().apply {
                    moveTo(faceCenterX - fw * 0.32f, faceY - fh * 0.04f)
                    cubicTo(
                        faceCenterX - fw * 0.22f, faceY + fh * 0.02f,
                        faceCenterX - fw * 0.12f, faceY + fh * 0.02f,
                        faceCenterX - fw * 0.04f, faceY - fh * 0.04f
                    )
                }
                drawPath(leftEye, bayonLine, style = Stroke(1.5.dp.toPx()))

                val rightEye = Path().apply {
                    moveTo(faceCenterX + fw * 0.04f, faceY - fh * 0.04f)
                    cubicTo(
                        faceCenterX + fw * 0.12f, faceY + fh * 0.02f,
                        faceCenterX + fw * 0.22f, faceY + fh * 0.02f,
                        faceCenterX + fw * 0.32f, faceY - fh * 0.04f
                    )
                }
                drawPath(rightEye, bayonLine, style = Stroke(1.5.dp.toPx()))

                // Gentle Broad Nose
                val nosePath = Path().apply {
                    moveTo(faceCenterX, faceY - fh * 0.12f)
                    lineTo(faceCenterX, faceY + fh * 0.14f)
                    cubicTo(
                        faceCenterX - fw * 0.10f, faceY + fh * 0.17f,
                        faceCenterX + fw * 0.10f, faceY + fh * 0.17f,
                        faceCenterX, faceY + fh * 0.14f
                    )
                }
                drawPath(nosePath, bayonLine, style = Stroke(1.2.dp.toPx()))

                // Iconic Bayon Serene Compassionate Smile
                val smilePath = Path().apply {
                    moveTo(faceCenterX - fw * 0.28f, faceY + fh * 0.25f)
                    cubicTo(
                        faceCenterX - fw * 0.14f, faceY + fh * 0.33f,
                        faceCenterX + fw * 0.14f, faceY + fh * 0.33f,
                        faceCenterX + fw * 0.28f, faceY + fh * 0.25f
                    )
                }
                drawPath(smilePath, bayonLine, style = Stroke(2.0.dp.toPx()))
            }

            // Left Face (Profile Angle)
            drawSereneFace(bayonCenterX - bayonWidth * 0.18f, bayonCenterY + bayonHeight * 0.05f, 0.95f)

            // Right Face (Profile Angle)
            drawSereneFace(bayonCenterX + bayonWidth * 0.18f, bayonCenterY + bayonHeight * 0.05f, 0.95f)

            // 4. Delicate Khmer Kbach Flame / Lotus Flourishes (Corner Ornaments)
            val filigreeColor = Color(0xFFC59A38).copy(alpha = 0.14f)
            fun drawKhmerFlourish(originX: Float, originY: Float, flipX: Float) {
                val fPath = Path().apply {
                    moveTo(originX, originY)
                    cubicTo(
                        originX + (30f * flipX), originY - 40f,
                        originX + (60f * flipX), originY - 20f,
                        originX + (45f * flipX), originY + 20f
                    )
                    cubicTo(
                        originX + (30f * flipX), originY + 50f,
                        originX + (10f * flipX), originY + 30f,
                        originX, originY
                    )
                }
                drawPath(fPath, filigreeColor)
            }

            drawKhmerFlourish(bayonCenterX - bayonWidth * 0.38f, bayonCenterY + bayonHeight * 0.30f, -1f)
            drawKhmerFlourish(bayonCenterX + bayonWidth * 0.38f, bayonCenterY + bayonHeight * 0.30f, 1f)
        }

        // Inner Screen Content
        content()
    }
}
