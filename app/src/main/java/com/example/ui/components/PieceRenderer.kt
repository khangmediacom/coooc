package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.data.model.Piece
import com.example.data.model.PieceColor
import com.example.data.model.PieceStyle
import com.example.data.model.PieceType

@Composable
fun PieceRenderer(
    piece: Piece,
    style: PieceStyle,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val context = LocalContext.current
    val imageLoader = coil.ImageLoader.Builder(context)
        .components {
            add(coil.decode.SvgDecoder.Factory())
        }
        .build()

    val colorPrefix = if (piece.color == PieceColor.WHITE) "w" else "b"
    val typeSuffix = when (piece.type) {
        PieceType.KING -> "K"
        PieceType.QUEEN -> "Q"
        PieceType.ROOK -> "R"
        PieceType.BISHOP -> "B"
        PieceType.KNIGHT -> "N"
        PieceType.PAWN -> "P"
        PieceType.PROMOTED_PAWN -> "F"
    }
    
    val folderName = style.id
    val assetPath = "file:///android_asset/pieces/$folderName/$colorPrefix$typeSuffix.svg"

    AsyncImage(
        model = coil.request.ImageRequest.Builder(context)
            .data(assetPath)
            .decoderFactory(coil.decode.SvgDecoder.Factory())
            .build(),
        contentDescription = "${piece.color} ${piece.type}",
        imageLoader = imageLoader,
        modifier = modifier.size(size)
    )
}
