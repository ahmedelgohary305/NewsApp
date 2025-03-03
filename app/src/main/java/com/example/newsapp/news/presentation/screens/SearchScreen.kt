package com.example.newsapp.news.presentation.screens

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.presentation.NewsViewModel
import com.example.newsapp.news.presentation.utils.NewsListScreen


@Composable
fun SearchScreen(
    newsViewModel: NewsViewModel,
    navController: NavController,
    news: LazyPagingItems<NewsEntity>,
    listState: LazyListState,
    context: Context,
) {
    NewsListScreen(
        newsViewModel = newsViewModel,
        navController = navController,
        news = news,
        listState = listState,
        context = context,
        isRefreshing = if (news.itemCount == 0) false else news.loadState.refresh == LoadState.Loading
    )
}