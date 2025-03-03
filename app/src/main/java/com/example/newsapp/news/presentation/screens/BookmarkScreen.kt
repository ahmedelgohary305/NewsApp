package com.example.newsapp.news.presentation.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.presentation.NewsViewModel
import com.example.newsapp.news.presentation.utils.NewsItem
import com.example.newsapp.news.presentation.utils.Routes
import com.google.gson.Gson

@Composable
fun BookmarkedNewsScreen(
    newsViewModel: NewsViewModel,
    navController: NavController,
    bookmarkedNews: List<NewsEntity>,
    context: Context
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(bookmarkedNews) { newsItem ->
            NewsItem(news = newsItem, onNewsClick = {
                val newsJson = Uri.encode(Gson().toJson(newsItem))
                navController.navigate("${Routes.DetailScreen.route}/$newsJson")
            }, onBookmarkClick = {
                newsViewModel.toggleBookmark(newsItem)
                Toast.makeText(
                    context,
                    if (newsItem.isBookmarked) "Removed from bookmarks" else "Added to bookmarks" ,
                    Toast.LENGTH_SHORT
                ).show()
            }
            )
        }
    }

}