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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AngkorGold
import com.example.ui.theme.JadeEmerald
import com.example.ui.theme.TerracottaRed
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 1. OFFICIAL APP LOGO (Row 2, Column 4 from Mascot Grid):
 * Smiling Hanuman leaning eagerly over an authentic wooden Ouk Chaktrang chessboard,
 * framed within a terracotta-red circular medallion with Angkor gold accents.
 */
@Composable
fun KhmerAppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    showOuterBorder: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_anim")
    val subtlePulse by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(
                if (showOuterBorder) {
                    Modifier
                        .shadow(4.dp, CircleShape, spotColor = TerracottaRed)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFFDF9),
                                    Color(0xFFFDF6E2),
                                    Color(0xFFF5E3BD)
                                )
                            )
                        )
                        .border(2.dp, TerracottaRed, CircleShape)
                } else Modifier
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.92f)) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f

            // Inner gold ring
            drawCircle(
                color = AngkorGold,
                radius = w * 0.46f,
                style = Stroke(width = w * 0.03f)
            )

            // Mukuta Crown (Gold Tiered Spire)
            val crownPath = Path().apply {
                moveTo(cx, h * 0.12f)
                lineTo(cx + w * 0.05f, h * 0.22f)
                lineTo(cx - w * 0.05f, h * 0.22f)
                close()
            }
            drawPath(crownPath, color = AngkorGold)

            val crownBase = Path().apply {
                moveTo(cx - w * 0.18f, h * 0.28f)
                cubicTo(cx - w * 0.1f, h * 0.24f, cx + w * 0.1f, h * 0.24f, cx + w * 0.18f, h * 0.28f)
                lineTo(cx + w * 0.14f, h * 0.36f)
                cubicTo(cx + w * 0.08f, h * 0.33f, cx - w * 0.08f, h * 0.33f, cx - w * 0.14f, h * 0.36f)
                close()
            }
            drawPath(crownBase, color = Color(0xFFFBBF24))
            drawPath(crownBase, color = Color(0xFF78350F), style = Stroke(w * 0.02f))

            // Ears + Earrings
            drawCircle(
                color = Color(0xFF16A34A),
                radius = w * 0.09f,
                center = Offset(cx - w * 0.25f, h * 0.40f)
            )
            drawCircle(
                color = Color(0xFFFBBF24),
                radius = w * 0.035f,
                center = Offset(cx - w * 0.29f, h * 0.45f)
            )

            drawCircle(
                color = Color(0xFF16A34A),
                radius = w * 0.09f,
                center = Offset(cx + w * 0.25f, h * 0.40f)
            )
            drawCircle(
                color = Color(0xFFFBBF24),
                radius = w * 0.035f,
                center = Offset(cx + w * 0.29f, h * 0.45f)
            )

            // Head & Face
            drawCircle(
                color = Color(0xFF15803D),
                radius = w * 0.20f,
                center = Offset(cx, h * 0.42f)
            )

            // Expressive Eyes (Big Almond Eyes with Yellow/Gold Pupils)
            drawOval(
                color = Color(0xFFFEF08A),
                topLeft = Offset(cx - w * 0.14f, h * 0.38f),
                size = Size(w * 0.10f, h * 0.07f)
            )
            drawOval(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx - w * 0.11f, h * 0.39f),
                size = Size(w * 0.05f, h * 0.05f)
            )

            drawOval(
                color = Color(0xFFFEF08A),
                topLeft = Offset(cx + w * 0.04f, h * 0.38f),
                size = Size(w * 0.10f, h * 0.07f)
            )
            drawOval(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx + w * 0.06f, h * 0.39f),
                size = Size(w * 0.05f, h * 0.05f)
            )

            // Smiling Mouth & Fangs
            val mouthPath = Path().apply {
                moveTo(cx - w * 0.12f, h * 0.48f)
                cubicTo(cx - w * 0.08f, h * 0.56f, cx + w * 0.08f, h * 0.56f, cx + w * 0.12f, h * 0.48f)
                cubicTo(cx + w * 0.06f, h * 0.49f, cx - w * 0.06f, h * 0.49f, cx - w * 0.12f, h * 0.48f)
                close()
            }
            drawPath(mouthPath, color = Color(0xFF991B1B))
            drawPath(mouthPath, color = Color(0xFF78350F), style = Stroke(w * 0.015f))

            // White Fangs
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.07f, h * 0.48f)
                    lineTo(cx - w * 0.04f, h * 0.52f)
                    lineTo(cx - w * 0.01f, h * 0.48f)
                    close()
                },
                color = Color.White
            )
            drawPath(
                Path().apply {
                    moveTo(cx + w * 0.01f, h * 0.48f)
                    lineTo(cx + w * 0.04f, h * 0.52f)
                    lineTo(cx + w * 0.07f, h * 0.48f)
                    close()
                },
                color = Color.White
            )

            // Two Arms Leaning In
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.22f, h * 0.52f)
                    cubicTo(cx - w * 0.26f, h * 0.62f, cx - w * 0.16f, h * 0.70f, cx - w * 0.08f, h * 0.68f)
                    lineTo(cx - w * 0.12f, h * 0.62f)
                    close()
                },
                color = Color(0xFF16A34A)
            )

            drawPath(
                Path().apply {
                    moveTo(cx + w * 0.22f, h * 0.52f)
                    cubicTo(cx + w * 0.26f, h * 0.62f, cx + w * 0.16f, h * 0.70f, cx + w * 0.08f, h * 0.68f)
                    lineTo(cx + w * 0.12f, h * 0.62f)
                    close()
                },
                color = Color(0xFF16A34A)
            )

            // Wooden Chessboard in front
            val boardPath = Path().apply {
                moveTo(cx - w * 0.36f, h * 0.68f)
                lineTo(cx + w * 0.36f, h * 0.68f)
                lineTo(cx + w * 0.44f, h * 0.86f)
                lineTo(cx - w * 0.44f, h * 0.86f)
                close()
            }
            drawPath(boardPath, color = Color(0xFFB45309))
            drawPath(boardPath, color = Color(0xFF78350F), style = Stroke(w * 0.02f))

            // Checkered Squares
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.18f, h * 0.68f)
                    lineTo(cx, h * 0.68f)
                    lineTo(cx, h * 0.86f)
                    lineTo(cx - w * 0.22f, h * 0.86f)
                    close()
                },
                color = Color(0xFF15803D)
            )
            drawPath(
                Path().apply {
                    moveTo(cx + w * 0.18f, h * 0.68f)
                    lineTo(cx + w * 0.36f, h * 0.68f)
                    lineTo(cx + w * 0.44f, h * 0.86f)
                    lineTo(cx + w * 0.22f, h * 0.86f)
                    close()
                },
                color = Color(0xFF15803D)
            )

            // Ouk Chaktrang Wooden Pieces on Board
            // 1. Center Red Piece
            drawOval(
                color = Color(0xFFDC2626),
                topLeft = Offset(cx - w * 0.06f, h * 0.74f),
                size = Size(w * 0.12f, h * 0.08f)
            )
            drawCircle(
                color = Color(0xFFFBBF24),
                radius = w * 0.022f,
                center = Offset(cx, h * 0.77f)
            )

            // 2. Left White Ivory Piece
            drawOval(
                color = Color(0xFFFFFBEB),
                topLeft = Offset(cx - w * 0.25f, h * 0.72f),
                size = Size(w * 0.10f, h * 0.07f)
            )
            drawOval(
                color = Color(0xFF78350F),
                topLeft = Offset(cx - w * 0.25f, h * 0.72f),
                size = Size(w * 0.10f, h * 0.07f),
                style = Stroke(w * 0.012f)
            )

            // 3. Right Green / Dark Wood Piece
            drawOval(
                color = Color(0xFF047857),
                topLeft = Offset(cx + w * 0.15f, h * 0.72f),
                size = Size(w * 0.10f, h * 0.07f)
            )
            drawOval(
                color = Color(0xFF78350F),
                topLeft = Offset(cx + w * 0.15f, h * 0.72f),
                size = Size(w * 0.10f, h * 0.07f),
                style = Stroke(w * 0.012f)
            )
        }
    }
}

/**
 * 2. MASCOT THINKING / TACTICAL COACH (Row 4, Column 1):
 * Green Yaksha resting chin on hand in deep contemplation over a piece.
 */
@Composable
fun KhmerMascotThinking(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "think_anim")
    val tiltAnim by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tilt"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFFFEF3C7))
            .border(1.5.dp, Color(0xFFD97706), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.9f)) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f

            // Profile Head tilted with chin on hand
            drawCircle(
                color = Color(0xFF15803D),
                radius = w * 0.32f,
                center = Offset(cx - w * 0.04f, cy - h * 0.05f)
            )

            // Golden Mukuta Crest
            val crown = Path().apply {
                moveTo(cx - w * 0.08f, h * 0.10f)
                lineTo(cx + w * 0.04f, h * 0.22f)
                lineTo(cx - w * 0.20f, h * 0.22f)
                close()
            }
            drawPath(crown, color = AngkorGold)

            // Thinking eye looking down
            drawOval(
                color = Color(0xFFFEF08A),
                topLeft = Offset(cx - w * 0.16f, h * 0.36f),
                size = Size(w * 0.16f, h * 0.10f)
            )
            drawCircle(
                color = Color(0xFF1E293B),
                radius = w * 0.04f,
                center = Offset(cx - w * 0.06f, h * 0.42f)
            )

            // Hand supporting chin
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.12f, h * 0.62f)
                    cubicTo(cx - w * 0.20f, h * 0.56f, cx - w * 0.26f, h * 0.68f, cx - w * 0.18f, h * 0.74f)
                    lineTo(cx + w * 0.02f, h * 0.76f)
                    close()
                },
                color = Color(0xFF16A34A)
            )
            drawCircle(
                color = Color(0xFFFBBF24),
                radius = w * 0.04f,
                center = Offset(cx - w * 0.02f, h * 0.74f)
            )

            // Ouk Chaktrang Piece in front
            drawOval(
                color = Color(0xFFDC2626),
                topLeft = Offset(cx + w * 0.08f, h * 0.64f),
                size = Size(w * 0.24f, h * 0.16f)
            )
            drawCircle(
                color = Color(0xFFFBBF24),
                radius = w * 0.04f,
                center = Offset(cx + w * 0.20f, h * 0.70f)
            )
        }
    }
}

/**
 * 3. MASCOT PLAYING / DUEL (Row 2, Column 3 & Row 5, Column 1):
 * Sitting cross-legged at the chessboard with hands actively moving pieces.
 */
@Composable
fun KhmerMascotPlaying(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "play_anim")
    val handBob by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hand"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFFFFFBEB))
            .border(1.5.dp, AngkorGold, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.92f)) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f

            // Body & Head
            drawCircle(
                color = Color(0xFF15803D),
                radius = w * 0.24f,
                center = Offset(cx, h * 0.35f)
            )

            // Crown
            drawPath(
                Path().apply {
                    moveTo(cx, h * 0.06f)
                    lineTo(cx + w * 0.08f, h * 0.20f)
                    lineTo(cx - w * 0.08f, h * 0.20f)
                    close()
                },
                color = AngkorGold
            )

            // Smile
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.12f, h * 0.40f)
                    cubicTo(cx - w * 0.06f, h * 0.48f, cx + w * 0.06f, h * 0.48f, cx + w * 0.12f, h * 0.40f)
                    close()
                },
                color = Color(0xFF991B1B)
            )

            // Cross-legged seated robes
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.36f, h * 0.72f)
                    cubicTo(cx - w * 0.20f, h * 0.56f, cx + w * 0.20f, h * 0.56f, cx + w * 0.36f, h * 0.72f)
                    lineTo(cx + w * 0.28f, h * 0.82f)
                    lineTo(cx - w * 0.28f, h * 0.82f)
                    close()
                },
                color = Color(0xFFC2410C)
            )

            // Chessboard & 4 Pieces
            val board = Path().apply {
                moveTo(cx - w * 0.32f, h * 0.74f)
                lineTo(cx + w * 0.32f, h * 0.74f)
                lineTo(cx + w * 0.40f, h * 0.92f)
                lineTo(cx - w * 0.40f, h * 0.92f)
                close()
            }
            drawPath(board, color = Color(0xFFB45309))
            drawPath(board, color = Color(0xFF78350F), style = Stroke(w * 0.015f))

            // Pieces
            drawCircle(Color(0xFFFFFFFF), radius = w * 0.04f, center = Offset(cx - w * 0.18f, h * 0.83f))
            drawCircle(Color(0xFFDC2626), radius = w * 0.04f, center = Offset(cx - w * 0.06f, h * 0.83f))
            drawCircle(Color(0xFF047857), radius = w * 0.04f, center = Offset(cx + w * 0.06f, h * 0.83f))
            drawCircle(Color(0xFFFBBF24), radius = w * 0.04f, center = Offset(cx + w * 0.18f, h * 0.83f))
        }
    }
}

/**
 * 4. MASCOT VICTORY / TRIUMPH (Row 1, Column 2 & Row 1, Column 3):
 * Smiling Hanuman holding up a captured gold/ivory chess piece in celebration!
 */
@Composable
fun KhmerMascotVictory(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vic_anim")
    val sparkleAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFBEB),
                        Color(0xFFFEF3C7),
                        Color(0xFFFDE68A)
                    )
                )
            )
            .border(2.dp, AngkorGold, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.92f)) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f

            // Golden Sun Rays / Halo
            for (i in 0 until 8) {
                val angle = (i * 45f + sparkleAngle) * (PI / 180f)
                val rayStart = Offset(cx + cos(angle).toFloat() * w * 0.38f, cy + sin(angle).toFloat() * h * 0.38f)
                val rayEnd = Offset(cx + cos(angle).toFloat() * w * 0.46f, cy + sin(angle).toFloat() * h * 0.46f)
                drawLine(
                    color = AngkorGold,
                    start = rayStart,
                    end = rayEnd,
                    strokeWidth = w * 0.03f,
                    cap = StrokeCap.Round
                )
            }

            // Head & Crown
            drawCircle(
                color = Color(0xFF15803D),
                radius = w * 0.24f,
                center = Offset(cx + w * 0.04f, h * 0.44f)
            )

            // Crown
            drawPath(
                Path().apply {
                    moveTo(cx + w * 0.04f, h * 0.12f)
                    lineTo(cx + w * 0.12f, h * 0.26f)
                    lineTo(cx - w * 0.04f, h * 0.26f)
                    close()
                },
                color = AngkorGold
            )

            // Joyful Wide Smiling Mouth
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.08f, h * 0.48f)
                    cubicTo(cx - w * 0.02f, h * 0.58f, cx + w * 0.14f, h * 0.58f, cx + w * 0.18f, h * 0.48f)
                    close()
                },
                color = Color(0xFF991B1B)
            )
            // Fangs
            drawCircle(Color.White, radius = w * 0.025f, center = Offset(cx - w * 0.02f, h * 0.50f))
            drawCircle(Color.White, radius = w * 0.025f, center = Offset(cx + w * 0.12f, h * 0.50f))

            // Raised Hand holding Victory Piece High (Left Side)
            drawPath(
                Path().apply {
                    moveTo(cx - w * 0.14f, h * 0.52f)
                    cubicTo(cx - w * 0.28f, h * 0.46f, cx - w * 0.32f, h * 0.32f, cx - w * 0.24f, h * 0.26f)
                    lineTo(cx - w * 0.18f, h * 0.32f)
                    close()
                },
                color = Color(0xFF16A34A)
            )

            // Golden Victory Piece Held in Hand!
            drawOval(
                color = Color(0xFFFBBF24),
                topLeft = Offset(cx - w * 0.36f, h * 0.18f),
                size = Size(w * 0.18f, h * 0.14f)
            )
            drawCircle(
                color = Color(0xFFDC2626),
                radius = w * 0.035f,
                center = Offset(cx - w * 0.27f, h * 0.25f)
            )
        }
    }
}
