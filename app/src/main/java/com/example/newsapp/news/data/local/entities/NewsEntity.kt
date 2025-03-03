package com.example.newsapp.news.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
    @PrimaryKey
    val id: String,
    val source: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String?,
    val isBookmarked: Boolean = false,
    val feedType: String
)
