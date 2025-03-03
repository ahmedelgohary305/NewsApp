package com.example.newsapp.news.domain

import androidx.paging.PagingData
import com.example.newsapp.news.data.local.entities.NewsEntity
import kotlinx.coroutines.flow.Flow

interface NewsRepo {
    fun getNews(query: String,feedType: String): Flow<PagingData<NewsEntity>>
    fun getBookmarkedNews(): Flow<List<NewsEntity>>
    suspend fun toggleBookmark(news: NewsEntity)
}