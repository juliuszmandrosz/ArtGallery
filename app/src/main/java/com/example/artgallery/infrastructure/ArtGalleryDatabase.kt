package com.example.artgallery.infrastructure

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.infrastructure.paintings.PaintingDao

@Database(entities = [Painting::class], version = 1)
abstract class ArtGalleryDatabase : RoomDatabase() {
    abstract fun paintingDao(): PaintingDao

    companion object {
        val DATABASE_NAME = "art_gallery_db"
    }
}