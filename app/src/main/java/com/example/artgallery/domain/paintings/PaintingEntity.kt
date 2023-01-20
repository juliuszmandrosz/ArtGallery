package com.example.artgallery.domain.paintings

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "paintings")
data class Painting(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String,
    val imageUrl: String,
)