package com.example.artgallery.domain.paintings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paintings")
data class Painting(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val imageUrl: String,
)