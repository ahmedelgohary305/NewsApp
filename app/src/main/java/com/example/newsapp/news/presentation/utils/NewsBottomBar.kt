package com.example.newsapp.news.presentation.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.R

@Composable
fun NewsBottomBar(navController: NavController, currentScreen: String?) {

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        contentPadding = PaddingValues(top = 4.dp),
        contentColor = MaterialTheme.colorScheme.onBackground.copy(0.9f),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Routes.NewsScreen.route) {
                        popUpTo(Routes.NewsScreen.route) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (currentScreen == Routes.NewsScreen.route)
                            R.drawable.home_svgrepo_com__1_
                        else
                            R.drawable.home_svgrepo_com
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(34.dp)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate(Routes.SearchScreen.route) {
                        popUpTo(Routes.SearchScreen.route) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (currentScreen == Routes.SearchScreen.route) R.drawable.search_svgrepo_com
                        else R.drawable.search_svgrepo_com__1_
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(27.dp)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate(Routes.BookmarkScreen.route) {
                        popUpTo(Routes.BookmarkScreen.route) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (currentScreen == Routes.BookmarkScreen.route) R.drawable.bookmark_fill_svgrepo_com
                        else R.drawable.bookmark_svgrepo_com
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
