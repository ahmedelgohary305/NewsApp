package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.news.data.NewsRepoImpl
import com.example.newsapp.news.data.local.NewsDatabase
import com.example.newsapp.news.data.remote.NewsApi
import com.example.newsapp.news.domain.NewsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi{
        return Retrofit.Builder().baseUrl("https://newsapi.org/v2/").addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(NewsApi::class.java)
    }


    @Provides
    @Singleton
    fun provideNewsDataBase(@ApplicationContext context:Context): NewsDatabase{
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news.db"
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideNewsRepo(newsApi: NewsApi, newsDb: NewsDatabase): NewsRepo{
        return NewsRepoImpl(newsApi, newsDb)
    }

}