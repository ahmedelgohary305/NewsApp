package com.example.newsapp.news.data.local.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.news.data.local.entities.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Upsert
    suspend fun upsertAll(news: List<NewsEntity>)

    @Query("SELECT * FROM newsentity WHERE feedType = :feedType AND isBookmarked = 0")
    fun pagingSource(feedType: String): PagingSource<Int, NewsEntity>

    @Query("DELETE FROM newsentity WHERE isBookmarked = 0 AND feedType = :feedType")
    suspend fun clearAll(feedType: String)

    @Query("SELECT * FROM newsentity WHERE isBookmarked = 1")
    fun getBookmarkedNews(): Flow<List<NewsEntity>>

    @Query("UPDATE newsentity SET isBookmarked = :isBookmarked WHERE id = :newsId")
    suspend fun updateBookmarkStatus(newsId: String, isBookmarked: Boolean)
}