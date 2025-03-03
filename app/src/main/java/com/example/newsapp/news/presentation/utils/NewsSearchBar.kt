package com.example.newsapp.news.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsapp.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSearchBar(
    expanded: Boolean,
    navController: NavController,
    currentScreen: String?,
    context: Context,
    onSearch: (String) -> Unit
) {
    val searchQuery = remember { mutableStateOf("") }
    val height = animateDpAsState(if (expanded) 83.dp else 0.dp, label = "AppBarHeight")
    val hasInternetConnection = remember { mutableStateOf(false) }

    if (currentScreen == Routes.NewsScreen.route) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.value)
                .animateContentSize()
                .padding(bottom = 8.dp, end = 16.dp),
            windowInsets = WindowInsets(top = 18.dp),

            title = {
                NewsSearch(
                    navController = navController
                )
            }
        )
    } else {
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(Unit) {
            delay(300)
            focusRequester.requestFocus()
            keyboardController?.show()
        }

        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .height(height.value)
                .animateContentSize()
                .padding(bottom = 8.dp, end = 16.dp),
            title = {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery.value,

                    onValueChange = {
                        searchQuery.value = it
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontFamily = FontFamily(Font(R.font.poppins_medium_italic))
                    ),
                    placeholder = {
                        Text(
                            if (searchQuery.value.isEmpty()) "Search News..." else searchQuery.value,
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = FontFamily(Font(R.font.poppins_medium_italic))
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions {
                        hasInternetConnection.value = showNoInternetToast(context)
                        if (searchQuery.value.isNotEmpty() && hasInternetConnection.value) {
                            onSearch(searchQuery.value)
                            keyboardController?.hide()
                        }
                    },
                    maxLines = 1,
                    shape = CircleShape,
                )
            }
        )

    }
}

@Composable
fun NewsSearch(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
            .clickable { navController.navigate(Routes.SearchScreen.route) },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search News...",
                color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily(
                    Font(R.font.poppins_medium_italic)
                )
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@SuppressLint("ServiceCast")
fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
}

fun showNoInternetToast(context: Context): Boolean {
    if (!context.isInternetAvailable()) {
        Toast.makeText(context, "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show()
        return false
    }else{
        return true
    }
}