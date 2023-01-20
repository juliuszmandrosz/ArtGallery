package com.example.artgallery.application.add_painting

data class AddPaintingState(
    val description: String? = null,
    val imageUrl: String? = null,
    val isPaintingDrawing: Boolean = false,
    val isPaintingAdding: Boolean = false,
    val isSuccess: Boolean = false
)