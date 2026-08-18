package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.animateFloat
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

import com.example.ui.theme.LateriteDark
import com.example.ui.theme.LateriteSurface
import com.example.ui.theme.RoyalGoldDark

private val SkyGoldTop = LateriteDark
private val SkyGoldWarm = LateriteSurface
private val TempleSilhouetteColor = RoyalGoldDark
private val TempleShadowColor = Color(0xFF15100E)

/**
 * Warm Golden Sky & Angkor Temple Background for Game & Match screens.
 * Seamlessly connects the warm golden sunrise sky on top with the majestic Angkor Wat silhouette at the bottom.
 */
@Composable
fun AngkorWarmBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            androidx.compose.material3.MaterialTheme.colorScheme.background,
            androidx.compose.material3.MaterialTheme.colorScheme.surface,
            androidx.compose.material3.MaterialTheme.colorScheme.surface
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        // Bottom Angkor Wat Temple Silhouette Canvas aligned against the footer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
        ) {
            val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "bg_rotation")
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                    animation = androidx.compose.animation.core.tween(20000, easing = androidx.compose.animation.core.LinearEasing),
                    repeatMode = androidx.compose.animation.core.RepeatMode.Restart
                ),
                label = "rotation"
            )

            Image(
                painter = painterResource(id = R.drawable.angkor_bg),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            )
            
            // Rotating Solar Mandala Wheel
            com.example.ui.screens.SolarMandalaOrnament(
                color = RoyalGoldDark.copy(alpha = 0.35f),
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 60.dp, y = (-40).dp)
                    .graphicsLayer { rotationZ = rotation }
            )

            // Seamless gradient overlay to blend smoothly into the sky color above
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                androidx.compose.material3.MaterialTheme.colorScheme.surface,
                                androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.40f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = 220f
                        )
                    )
            )
        }

        // Screen Content Layer
        content()
    }
}

/**
 * Vector Angkor Wat Temple Silhouette with 5 Sacred Lotus Towers (Prasat).
 */
@Composable
fun AngkorWatTempleCanvas(
    modifier: Modifier = Modifier,
    silhouetteColor: Color = Color(0xFFB45309).copy(alpha = 0.30f),
    baseColor: Color = Color(0xFF78350F).copy(alpha = 0.20f)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val baseY = h * 0.95f

        // Base foundation terrace
        drawRect(
            color = baseColor,
            topLeft = Offset(0f, baseY),
            size = Size(w, h - baseY)
        )

        // Draw 5 Iconic Prasat Towers
        val cx = w * 0.5f

        // Helper to draw a Khmer tiered lotus tower
        fun drawPrasatTower(towerCx: Float, towerWidth: Float, towerHeight: Float, color: Color) {
            val topY = baseY - towerHeight
            val towerPath = Path().apply {
                // Lotus Bud Finial (Kalasa)
                moveTo(towerCx, topY)
                cubicTo(towerCx + towerWidth * 0.08f, topY + towerHeight * 0.06f, towerCx + towerWidth * 0.12f, topY + towerHeight * 0.12f, towerCx + towerWidth * 0.06f, topY + towerHeight * 0.18f)
                cubicTo(towerCx + towerWidth * 0.22f, topY + towerHeight * 0.28f, towerCx + towerWidth * 0.28f, topY + towerHeight * 0.42f, towerCx + towerWidth * 0.25f, topY + towerHeight * 0.55f)
                // Tiered steps down to base
                lineTo(towerCx + towerWidth * 0.36f, topY + towerHeight * 0.65f)
                lineTo(towerCx + towerWidth * 0.34f, topY + towerHeight * 0.72f)
                lineTo(towerCx + towerWidth * 0.46f, topY + towerHeight * 0.82f)
                lineTo(towerCx + towerWidth * 0.44f, topY + towerHeight * 0.88f)
                lineTo(towerCx + towerWidth * 0.50f, baseY)
                // Left side mirror
                lineTo(towerCx - towerWidth * 0.50f, baseY)
                lineTo(towerCx - towerWidth * 0.44f, topY + towerHeight * 0.88f)
                lineTo(towerCx - towerWidth * 0.46f, topY + towerHeight * 0.82f)
                lineTo(towerCx - towerWidth * 0.34f, topY + towerHeight * 0.72f)
                lineTo(towerCx - towerWidth * 0.36f, topY + towerHeight * 0.65f)
                cubicTo(towerCx - towerWidth * 0.25f, topY + towerHeight * 0.55f, towerCx - towerWidth * 0.28f, topY + towerHeight * 0.42f, towerCx - towerWidth * 0.22f, topY + towerHeight * 0.28f)
                cubicTo(towerCx - towerWidth * 0.12f, topY + towerHeight * 0.18f, towerCx - towerWidth * 0.08f, topY + towerHeight * 0.06f, towerCx, topY)
                close()
            }
            drawPath(towerPath, color = color)
        }

        // Connecting Gallery Galleries
        val galleryPath = Path().apply {
            moveTo(w * 0.08f, baseY)
            lineTo(w * 0.08f, baseY - h * 0.22f)
            lineTo(w * 0.92f, baseY - h * 0.22f)
            lineTo(w * 0.92f, baseY)
            close()
        }
        drawPath(galleryPath, color = baseColor)

        // 1. Far Left Tower
        drawPrasatTower(cx - w * 0.34f, w * 0.14f, h * 0.45f, silhouetteColor.copy(alpha = silhouetteColor.alpha * 0.75f))

        // 2. Far Right Tower
        drawPrasatTower(cx + w * 0.34f, w * 0.14f, h * 0.45f, silhouetteColor.copy(alpha = silhouetteColor.alpha * 0.75f))

        // 3. Middle Left Tower
        drawPrasatTower(cx - w * 0.18f, w * 0.18f, h * 0.65f, silhouetteColor.copy(alpha = silhouetteColor.alpha * 0.9f))

        // 4. Middle Right Tower
        drawPrasatTower(cx + w * 0.18f, w * 0.18f, h * 0.65f, silhouetteColor.copy(alpha = silhouetteColor.alpha * 0.9f))

        // 5. Central Grand Prasat Sanctuary (Tallest)
        drawPrasatTower(cx, w * 0.24f, h * 0.88f, silhouetteColor)
    }
}

