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

/**
 * Modern Angkor Wat Flat Vector Silhouette Atmospheric Background.
 * Provides a brightened, elegant deep sapphire/slate canvas with subtle Angkor Wat silhouettes.
 */
@Composable
fun AngkorFlatBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Slightly brighter & warmer dark canvas: deep slate sapphire with warm amber aura
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F1B30), // Brighter top slate
            Color(0xFF162540), // Rich indigo slate center
            Color(0xFF0D172A)  // Deep navy bottom
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        // Flat Vector Silhouette of Angkor Wat (rendered in soft translucent gold & slate)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Subtle Horizon Warm Glow behind Angkor Wat
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE5A83B).copy(alpha = 0.12f),
                        Color(0xFFE5A83B).copy(alpha = 0.03f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.72f),
                    radius = w * 0.65f
                ),
                radius = w * 0.65f,
                center = Offset(w * 0.5f, h * 0.72f)
            )

            // 2. Angkor Wat Silhouette Path
            val silhouetteColor = Color(0xFFE5A83B).copy(alpha = 0.08f)
            val baseColor = Color(0xFF1E2E4A).copy(alpha = 0.35f)

            val templeBaseY = h * 0.88f
            val groundY = h * 0.94f

            // Ground base platform
            drawRect(
                color = baseColor,
                topLeft = Offset(0f, groundY),
                size = Size(w, h - groundY)
            )

            // Tier platform
            drawRect(
                color = baseColor,
                topLeft = Offset(w * 0.08f, templeBaseY),
                size = Size(w * 0.84f, groundY - templeBaseY)
            )

            // Function to draw an Angkor lotus-bud tower
            fun drawTower(centerX: Float, baseTopY: Float, towerWidth: Float, towerHeight: Float, color: Color) {
                val path = Path().apply {
                    val halfW = towerWidth / 2f
                    moveTo(centerX - halfW, baseTopY)
                    // Tier 1
                    lineTo(centerX - halfW * 0.85f, baseTopY - towerHeight * 0.3f)
                    lineTo(centerX - halfW * 0.95f, baseTopY - towerHeight * 0.32f)
                    // Tier 2
                    lineTo(centerX - halfW * 0.75f, baseTopY - towerHeight * 0.6f)
                    lineTo(centerX - halfW * 0.82f, baseTopY - towerHeight * 0.62f)
                    // Lotus dome curve to tip
                    cubicTo(
                        centerX - halfW * 0.6f, baseTopY - towerHeight * 0.85f,
                        centerX - halfW * 0.15f, baseTopY - towerHeight * 0.98f,
                        centerX, baseTopY - towerHeight
                    )
                    cubicTo(
                        centerX + halfW * 0.15f, baseTopY - towerHeight * 0.98f,
                        centerX + halfW * 0.6f, baseTopY - towerHeight * 0.85f,
                        centerX + halfW * 0.82f, baseTopY - towerHeight * 0.62f
                    )
                    lineTo(centerX + halfW * 0.75f, baseTopY - towerHeight * 0.6f)
                    lineTo(centerX + halfW * 0.95f, baseTopY - towerHeight * 0.32f)
                    lineTo(centerX + halfW * 0.85f, baseTopY - towerHeight * 0.3f)
                    lineTo(centerX + halfW, baseTopY)
                    close()
                }
                drawPath(path, color)
            }

            // Outer Lower Left Tower
            drawTower(
                centerX = w * 0.22f,
                baseTopY = templeBaseY,
                towerWidth = w * 0.12f,
                towerHeight = h * 0.15f,
                color = silhouetteColor
            )

            // Inner Mid Left Tower
            drawTower(
                centerX = w * 0.36f,
                baseTopY = templeBaseY,
                towerWidth = w * 0.14f,
                towerHeight = h * 0.22f,
                color = silhouetteColor
            )

            // Central Grand Sanctuary Tower (Tallest)
            drawTower(
                centerX = w * 0.50f,
                baseTopY = templeBaseY,
                towerWidth = w * 0.18f,
                towerHeight = h * 0.30f,
                color = Color(0xFFE5A83B).copy(alpha = 0.11f)
            )

            // Inner Mid Right Tower
            drawTower(
                centerX = w * 0.64f,
                baseTopY = templeBaseY,
                towerWidth = w * 0.14f,
                towerHeight = h * 0.22f,
                color = silhouetteColor
            )

            // Outer Lower Right Tower
            drawTower(
                centerX = w * 0.78f,
                baseTopY = templeBaseY,
                towerWidth = w * 0.12f,
                towerHeight = h * 0.15f,
                color = silhouetteColor
            )
        }

        // Screen Composable content
        content()
    }
}
