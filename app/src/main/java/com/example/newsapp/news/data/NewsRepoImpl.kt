package com.example.newsapp.news.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.news.data.local.NewsDatabase
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.data.remote.NewsApi
import com.example.newsapp.news.data.remote.NewsRemoteMediator
import com.example.newsapp.news.domain.NewsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDb: NewsDatabase,
) : NewsRepo {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNews(query: String,feedType: String): Flow<PagingData<NewsEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NewsRemoteMediator(newsApi, newsDb,query,feedType),
            pagingSourceFactory = { newsDb.newsDao.pagingSource(feedType) }
        ).flow
    }

    override fun getBookmarkedNews(): Flow<List<NewsEntity>> {
        return newsDb.newsDao.getBookmarkedNews()
    }

    override suspend fun toggleBookmark(news: NewsEntity) {
        newsDb.newsDao.updateBookmarkStatus(news.id, !news.isBookmarked)
    }
}