package com.example.ui.components

import android.content.Context
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.data.model.Piece
import com.example.data.model.PieceColor
import com.example.data.model.PieceStyle
import com.example.data.model.PieceType

/**
 * Global singleton SVG ImageLoader for Coil
 */
private var globalSvgImageLoader: ImageLoader? = null

fun getSvgImageLoader(context: Context): ImageLoader {
    return globalSvgImageLoader ?: synchronized(context) {
        globalSvgImageLoader ?: ImageLoader.Builder(context.applicationContext)
            .components {
                add(SvgDecoder.Factory())
            }
            .memoryCache {
                coil.memory.MemoryCache.Builder(context.applicationContext)
                    .maxSizePercent(0.25)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
            .also { globalSvgImageLoader = it }
    }
}

/**
 * Maps game Piece to exact SVG asset file from hanuman-chess-arena
 * Sets:
 *  - "ada-red": Default authentic Crimson & Ivory (bK.svg, wK.svg, etc.)
 *  - "ada": Ivory style
 *  - "cambodian": Cambodian Ouk style
 */
fun getPieceSvgAssetPath(piece: Piece, style: PieceStyle = PieceStyle.CLASSIC): String {
    val folder = when (style) {
        PieceStyle.CLASSIC -> "ada-red"
        PieceStyle.SCULPTED -> "ada"
        PieceStyle.MINIMALIST -> "cambodian"
    }

    val sidePrefix = if (piece.color == PieceColor.WHITE) "w" else "b"
    val typeCode = when (piece.type) {
        PieceType.KING -> "K"
        PieceType.QUEEN -> "Q"
        PieceType.BISHOP -> "B"
        PieceType.KNIGHT -> "N"
        PieceType.ROOK -> "R"
        PieceType.PAWN -> "P"
        PieceType.PROMOTED_PAWN -> "F"
    }

    return "file:///android_asset/pieces/$folder/$sidePrefix$typeCode.svg"
}

@Composable
fun PieceView(
    piece: Piece,
    isSelected: Boolean = false,
    style: PieceStyle = PieceStyle.CLASSIC,
    modifier: Modifier = Modifier
) {
    PieceRenderer(piece, isSelected, style, modifier)
}

/**
 * Authentic 100% Exact SVG Piece Renderer rendering the SVG vector artwork from hanuman-chess-arena
 */
@Composable
fun PieceRenderer(
    piece: Piece,
    isSelected: Boolean = false,
    style: PieceStyle = PieceStyle.CLASSIC,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = remember(context) { getSvgImageLoader(context) }
    val assetUri = remember(piece.color, piece.type, style) { getPieceSvgAssetPath(piece, style) }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "piece_scale"
    )

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(assetUri)
            .crossfade(true)
            .build(),
        imageLoader = imageLoader
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .scale(scale)
            .clip(CircleShape)
            .testTag("piece_${piece.color.name.lowercase()}_${piece.type.name.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "${piece.color.name} ${piece.type.name}",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
        )
    }
}
