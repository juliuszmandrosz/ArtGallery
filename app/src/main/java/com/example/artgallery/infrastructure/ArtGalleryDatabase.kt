package com.example.artgallery.infrastructure

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.infrastructure.converters.UUIDConverter
import com.example.artgallery.infrastructure.paintings.PaintingDao

@Database(entities = [Painting::class], version = 2)
@TypeConverters(UUIDConverter::class)
abstract class ArtGalleryDatabase : RoomDatabase() {
    abstract fun paintingDao(): PaintingDao

    companion object {
        val DATABASE_NAME = "art_gallery_db"
    }
}