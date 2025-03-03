package com.example.newsapp.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.news.data.local.daos.NewsDao
import com.example.newsapp.news.data.local.daos.RemoteKeysDao
import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.data.local.entities.RemoteKeysEntity

@Database(
    entities = [NewsEntity::class, RemoteKeysEntity::class],
    version = 7,
    exportSchema = false
)
abstract class NewsDatabase: RoomDatabase() {
    abstract val newsDao: NewsDao
    abstract val remoteKeysDao: RemoteKeysDao
}