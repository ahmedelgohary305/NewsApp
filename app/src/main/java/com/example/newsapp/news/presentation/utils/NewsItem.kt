package com.example.newsapp.news.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.news.data.local.entities.NewsEntity

@Composable
fun NewsItem(
    news: NewsEntity,
    onNewsClick: () -> Unit,
    onBookmarkClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.primaryContainer
            )
            .padding(16.dp)
            .clickable { onNewsClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = news.author ?: "Unknown Author",
                style = MaterialTheme.typography.titleSmall,
                fontFamily = FontFamily(Font(R.font.poppins_bold_italic)),
                color = if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBookmarkClick() },
                painter = if (news.isBookmarked)
                    painterResource(R.drawable.baseline_bookmark_added_24)
                else
                    painterResource(R.drawable.baseline_bookmark_border_24),
                contentDescription = "Bookmark Icon",
                tint = if (isSystemInDarkTheme())
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            text = news.title,
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily(Font(R.font.poppins_black_italic)),
            color = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = news.description ?: "No Description",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily(Font(R.font.poppins_medium_italic)),
            color = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 8.dp)
        )

        AsyncImage(
            model = news.imageUrl,
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 16.dp)
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = news.source,
            style = MaterialTheme.typography.titleSmall,
            fontFamily = FontFamily(Font(R.font.poppins_light_italic)),
        )
    }
}