package com.example.newsapp.news.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeysEntity(
    @PrimaryKey
    val id: String,
    val nextKey: Int?
)
