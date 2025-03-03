package com.example.newsapp.news.presentation.utils

sealed class Routes(val route: String) {
    object NewsScreen : Routes("news_screen")
    object DetailScreen : Routes("detail_screen")
    object BookmarkScreen : Routes("bookmark_screen")
    object SearchScreen : Routes("search_screen")
}