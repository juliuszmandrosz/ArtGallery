package com.example.artgallery.application.add_painting

data class AddPaintingState(
    val description: String? = null,
    val imageUrl: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)