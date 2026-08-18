package com.example.ui.components
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun AppShellTopBar(
    title: String,
    subtitle: String,
    onlineCountText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F1E2)) // Approximate bg-temple
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mascot
            Box(modifier = Modifier.size(40.dp)) {
                Image(
                painter = painterResource(id = R.drawable.mascot),
                contentDescription = null,
                    contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(
            ).clip(CircleShape).border(1.dp, RoyalGold.copy(alpha = 0.6f), CircleShape)
                )
            }
            
            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    color = TextPrimaryLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondaryLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Online count badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 50))
                    .background(Color(0xFFDDE6DD))
                    .border(1.dp, Color(0xFFB1C8B1), RoundedCornerShape(percent = 50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF388E3C))
                )
                Text(
                    text = onlineCountText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF388E3C)
                )
            }
        } // End of Mascot Row
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFD4C1A0)))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD4C1A0)))
            Spacer(modifier = Modifier.width(8.dp))
            // Diamond
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .graphicsLayer { rotationZ = 45f }
                    .border(1.5.dp, Color(0xFFD4C1A0))
                    .padding(2.dp)
                    .background(Color(0xFFD4C1A0))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD4C1A0)))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFD4C1A0)))
        }
    }
}
