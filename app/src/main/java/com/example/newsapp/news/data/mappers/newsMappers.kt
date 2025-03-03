package com.example.newsapp.news.data.mappers

import com.example.newsapp.news.data.local.entities.NewsEntity
import com.example.newsapp.news.data.remote.ArticleDto


fun ArticleDto.toNewsEntity(feedType: String): NewsEntity {
    return NewsEntity(
        id = url,
        source = source.name,
        author = author,
        title = title,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedAt = publishedAt,
        feedType = feedType
    )
}
