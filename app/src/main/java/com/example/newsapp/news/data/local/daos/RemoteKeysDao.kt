package com.example.newsapp.news.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.news.data.local.entities.RemoteKeysEntity


@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM remotekeysentity WHERE id = :id")
    suspend fun getRemoteKeys(id: String): RemoteKeysEntity?

    @Upsert
    suspend fun upsertAll(remoteKeys: List<RemoteKeysEntity>)

    @Query("DELETE FROM remotekeysentity")
    suspend fun clearAll()
}