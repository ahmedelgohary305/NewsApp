package com.example.newsapp.news.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.news.data.local.NewsDatabase
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.data.local.entities.RemoteKeysEntity
import com.example.newsapp.news.data.mappers.toNewsEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsApi: NewsApi,
    private val newsDb: NewsDatabase,
    private val query: String,
    private val feedType: String
): RemoteMediator<Int, NewsEntity>() {
    private val newsDao = newsDb.newsDao
    private val remoteKeysDao = newsDb.remoteKeysDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextKey
                }
            }

            val response = newsApi.getNews(
                query = query,
                page = page,
                pageSize = state.config.pageSize
            )
            val articles = response.articles
            val endOfPaginationReached = articles.isEmpty()

            newsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDao.clearAll(feedType)
                    remoteKeysDao.clearAll()
                }

                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = articles.map { article ->
                    RemoteKeysEntity(
                        id = article.url,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.upsertAll(keys)
                newsDao.upsertAll(articles.map { it.toNewsEntity(feedType) })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, NewsEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article -> remoteKeysDao.getRemoteKeys(article.id) }
    }
}