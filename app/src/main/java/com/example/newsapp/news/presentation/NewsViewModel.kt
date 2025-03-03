package com.example.newsapp.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.domain.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepo: NewsRepo
) : ViewModel() {

    private val userPreferences = MutableStateFlow("top headlines")
    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    var newsFeed = userPreferences
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { preferences ->
            newsRepo.getNews(preferences, "general")
        }.cachedIn(viewModelScope)
        private set

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    var searchResults = searchQuery
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            newsRepo.getNews(query, "search")
        }.cachedIn(viewModelScope)
        private set

    var bookmarkedNews = newsRepo.getBookmarkedNews()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        private set

    fun toggleBookmark(news: NewsEntity) {
        viewModelScope.launch {
            newsRepo.toggleBookmark(news)
        }
    }

    fun updateSearchQuery(newQuery: String) {
        searchQuery.value = newQuery
    }
}