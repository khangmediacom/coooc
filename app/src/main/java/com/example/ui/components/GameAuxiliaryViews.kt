package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Piece
import com.example.data.model.PieceColor
import com.example.data.model.PieceStyle

@Composable
fun CapturedRow(
    pieces: List<Piece>,
    color: PieceColor,
    style: PieceStyle,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        pieces.forEach { piece ->
            PieceRenderer(piece = piece, style = style, size = 24.dp)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun PieceView(
    piece: Piece,
    style: PieceStyle,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp
) {
    PieceRenderer(piece, style, modifier, size)
}
