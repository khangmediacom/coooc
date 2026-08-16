package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Luxury ZingPlay-style Dark Textured Gaming Table Background with Warm Golden Ambient Glow.
 * Features:
 * 1. Deep rich dark charcoal/espresso textured woven mat gradient (just like the ZingPlay game room).
 * 2. Warm amber-gold radial spotlight centered on the board area for maximum depth and contrast.
 * 3. Subtle fine woven texture grid overlay.
 * 4. Elegant Angkor & Bayon golden silhouettes and Khmer lotus filigrees at corner edges.
 */
@Composable
fun AngkorWarmBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF26201B), // Dark espresso / charcoal gaming linen top
            Color(0xFF1C1714), // Deep warm charcoal center
            Color(0xFF14110E), // Luxury dark ebony wood base
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

            // 1. Warm Golden Spotlight centered on the playing field (illuminates the chessboard)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFD4AF37).copy(alpha = 0.18f),
                        Color(0xFFB45309).copy(alpha = 0.08f),
                        Color(0xFF78350F).copy(alpha = 0.03f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.50f),
                    radius = w * 0.75f
                ),
                radius = w * 0.75f,
                center = Offset(w * 0.5f, h * 0.50f)
            )

            // 2. Subtle luxury woven fabric texture lines (horizontal & vertical grain)
            val fabricColor = Color(0xFFFFFFFF).copy(alpha = 0.015f)
            val step = 16f
            var y = 0f
            while (y < h) {
                drawLine(
                    color = fabricColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f
                )
                y += step
            }
            var x = 0f
            while (x < w) {
                drawLine(
                    color = fabricColor,
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1f
                )
                x += step
            }

            // 3. Angkor Wat Towers in Warm Golden Mist (Upper region)
            val templeBaseY = h * 0.35f
            val goldSilhouette = Color(0xFFE2C474).copy(alpha = 0.10f)
            val strokeColor = Color(0xFFD4AF37).copy(alpha = 0.14f)

            fun drawAngkorTower(
                centerX: Float,
                baseY: Float,
                towerW: Float,
                towerH: Float
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
                drawPath(path, goldSilhouette)
                drawPath(path, strokeColor, style = Stroke(1.0.dp.toPx()))
            }

            // 5 Iconic Angkor Wat Spire Towers
            drawAngkorTower(w * 0.18f, templeBaseY, w * 0.12f, h * 0.10f)
            drawAngkorTower(w * 0.33f, templeBaseY, w * 0.15f, h * 0.15f)
            drawAngkorTower(w * 0.50f, templeBaseY, w * 0.20f, h * 0.20f)
            drawAngkorTower(w * 0.67f, templeBaseY, w * 0.15f, h * 0.15f)
            drawAngkorTower(w * 0.82f, templeBaseY, w * 0.12f, h * 0.10f)

            // 4. Delicate Khmer Kbach Lotus Ornaments in the corners
            val filigreeColor = Color(0xFFE2C474).copy(alpha = 0.12f)
            fun drawCornerFlourish(originX: Float, originY: Float, flipX: Float, flipY: Float) {
                val fPath = Path().apply {
                    moveTo(originX, originY)
                    cubicTo(
                        originX + (36f * flipX), originY + (4f * flipY),
                        originX + (54f * flipX), originY + (24f * flipY),
                        originX + (36f * flipX), originY + (48f * flipY)
                    )
                    cubicTo(
                        originX + (20f * flipX), originY + (56f * flipY),
                        originX + (8f * flipX), originY + (32f * flipY),
                        originX, originY
                    )
                }
                drawPath(fPath, filigreeColor)
            }

            drawCornerFlourish(16f, 16f, 1f, 1f)
            drawCornerFlourish(w - 16f, 16f, -1f, 1f)
            drawCornerFlourish(16f, h - 16f, 1f, -1f)
            drawCornerFlourish(w - 16f, h - 16f, -1f, -1f)
        }

        // Inner Screen Content
        content()
    }
}
