package com.example.newsapp.news.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.presentation.screens.BookmarkedNewsScreen
import com.example.newsapp.news.presentation.screens.DetailScreen
import com.example.newsapp.news.presentation.screens.NewsScreen
import com.example.newsapp.news.presentation.screens.SearchScreen
import com.example.newsapp.news.presentation.utils.NewsBottomBar
import com.example.newsapp.news.presentation.utils.NewsSearchBar
import com.example.newsapp.news.presentation.utils.Routes
import com.example.newsapp.news.presentation.utils.TopBar
import com.google.gson.Gson
import kotlin.collections.contains

@Composable
fun NewsNavGraph(
    context: Context
) {
    val viewModel = hiltViewModel<NewsViewModel>()
    val bookmarkedNews = viewModel.bookmarkedNews.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val listState = rememberLazyListState()
    val previousIndex = remember { mutableIntStateOf(0) }
    val previousScrollOffset = remember { mutableIntStateOf(0) }
    val expanded = remember { mutableStateOf(true) }
    val scrollThreshold = 5

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val isScrollingUp = index < previousIndex.intValue ||
                        (index == previousIndex.intValue && offset < previousScrollOffset.intValue)

                val isSignificantScroll =
                    kotlin.math.abs(offset - previousScrollOffset.intValue) > scrollThreshold

                if (isSignificantScroll) {
                    expanded.value = isScrollingUp
                    previousIndex.intValue = index
                    previousScrollOffset.intValue = offset
                }
            }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            when (currentRoute) {
                Routes.NewsScreen.route -> Column(

                ) {
                    TopBar("For You")
                    NewsSearchBar(
                        expanded.value,
                        navController,
                        currentRoute,
                        context
                    ) { query ->
                        viewModel.updateSearchQuery(query)
                    }
                }

                Routes.SearchScreen.route -> Column(

                ) {
                    TopBar("Search")
                    NewsSearchBar(
                        expanded.value,
                        navController,
                        currentRoute,
                        context
                    ) { query ->
                        viewModel.updateSearchQuery(query)
                    }
                }

                Routes.BookmarkScreen.route -> TopBar("Bookmarked News")
            }
        },
        bottomBar = {
            if (currentRoute in listOf(
                    Routes.NewsScreen.route,
                    Routes.BookmarkScreen.route,
                    Routes.SearchScreen.route
                )
            ) {
                NewsBottomBar(navController, currentRoute)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.NewsScreen.route,
            modifier = Modifier.padding(it),
        ) {
            composable(Routes.NewsScreen.route) {
                val newsFeed = viewModel.newsFeed.collectAsLazyPagingItems()
                NewsScreen(
                    newsViewModel = viewModel,
                    navController = navController,
                    news = newsFeed,
                    listState = listState,
                    context = context
                )
            }

            composable(
                route = "${Routes.DetailScreen.route}/{newsItem}",
                arguments = listOf(navArgument("newsItem") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val json = backStackEntry.arguments?.getString("newsItem")
                val newsItem = Gson().fromJson(json, NewsEntity::class.java)
                DetailScreen(
                    news = newsItem,
                    onBackClick = { navController.popBackStack() }
                ) { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(context,intent,null)
                }
            }

            composable(Routes.BookmarkScreen.route) {
                BookmarkedNewsScreen(
                    newsViewModel = viewModel,
                    navController = navController,
                    bookmarkedNews = bookmarkedNews.value,
                    context = context
                )
            }

            composable(Routes.SearchScreen.route) {
                val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
                SearchScreen(
                    newsViewModel = viewModel,
                    navController = navController,
                    news = searchResults,
                    listState = listState,
                    context = context
                )
            }
        }
    }

}