package com.example.newsapp.news.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.news.data.local.entities.NewsEntity

@Composable
fun DetailScreen(news: NewsEntity, onBackClick: () -> Unit, onLinkClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                onClick = { onLinkClick(news.url) },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_link_24),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = news.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .size(400.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .blur(
                        radius = 2.dp
                    )
            )
            Box(
                modifier = Modifier.padding(bottom = 16.dp).align(Alignment.BottomStart).background(
                    color = Color.Black.copy(alpha = 0.4f)
                )
            ){
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = news.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily(Font(R.font.poppins_black_italic)),
                    color = Color.White
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Start)
        ) {
            Text(
                text = news.source,
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = FontFamily(Font(R.font.poppins_black)),
                color = if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            )
            Icon(
                painter = painterResource(R.drawable.baseline_verified_24),
                contentDescription = null,
                tint = if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        HorizontalDivider(
            color = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Text(
            text = news.description ?: "No Description",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily(Font(R.font.poppins_medium_italic)),
            color = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = news.publishedAt ?: "No Publish Date",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily(Font(R.font.poppins_medium_italic)),
            color = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            modifier = Modifier
                .padding(start = 8.dp, bottom = 16.dp)
                .align(Alignment.Start)
        )
    }
}