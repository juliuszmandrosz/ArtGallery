package com.example.artgallery.application.paintings

import com.example.artgallery.domain.paintings.Painting

data class PaintingsState(
    val paintings: List<Painting> = emptyList(),
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)