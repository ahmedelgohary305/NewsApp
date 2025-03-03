package com.example.newsapp.news.data.remote



import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String = "6bd8e230879c4d7298ee015e05176b4f"
    ): NewsDto
}