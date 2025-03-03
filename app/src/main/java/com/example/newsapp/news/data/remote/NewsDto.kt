package com.example.newsapp.news.data.remote

data class NewsDto(
    val articles: List<ArticleDto>
)

data class ArticleDto(
    val source: SourceDto,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
)

data class SourceDto(
    val name: String
)
