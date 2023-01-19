package com.example.artgallery.infrastructure.paintings

import androidx.room.*
import com.example.artgallery.domain.paintings.Painting
import kotlinx.coroutines.flow.Flow

@Dao
interface PaintingDao {
    @Query("SELECT * FROM paintings")
    fun getAll(): Flow<List<Painting>>

    @Insert
    suspend fun insert(painting: Painting)

    @Update
    suspend fun update(painting: Painting)

    @Delete
    suspend fun delete(painting: Painting)
}