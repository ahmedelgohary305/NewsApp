package com.example.newsapp.news.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newsapp.R
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.presentation.NewsViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    newsViewModel: NewsViewModel,
    navController: NavController,
    news: LazyPagingItems<NewsEntity>,
    listState: LazyListState,
    context: Context,
    isRefreshing: Boolean,
) {
    val hasDataLoadedOnce = remember { mutableStateOf(false) }


    LaunchedEffect(news.itemCount) {
        if (news.itemCount > 0) {
            hasDataLoadedOnce.value = true
        }
    }

    PullToRefreshBox(
        state = rememberPullToRefreshState(),
        isRefreshing = isRefreshing,
        onRefresh = { news.refresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            news.itemCount == 0 && hasDataLoadedOnce.value -> {
                EmptyStateMessage()
            }

            news.loadState.refresh is LoadState.Loading && news.itemCount != 0 -> {
                LoadingIndicator()
            }

            else -> {
                NewsList(
                    news = news,
                    listState = listState,
                    navController = navController,
                    onBookmarkClick = { newsItem ->
                        newsViewModel.toggleBookmark(newsItem)
                        Toast.makeText(
                            context,
                            if (newsItem.isBookmarked) "Removed from bookmarks" else "Added to bookmarks",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyStateMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No news found",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily(Font(R.font.poppins_black_italic))
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NewsList(
    news: LazyPagingItems<NewsEntity>,
    listState: LazyListState,
    navController: NavController,
    onBookmarkClick: (NewsEntity) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(news.itemSnapshotList.items) { newsItem ->
            newsItem.let {
                NewsItem(
                    news = it,
                    onNewsClick = {
                        val newsJson = Uri.encode(Gson().toJson(it))
                        navController.navigate("${Routes.DetailScreen.route}/$newsJson")
                    },
                    onBookmarkClick = { onBookmarkClick(it) }
                )
            }
        }
        item {
            if (news.loadState.append is LoadState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}
