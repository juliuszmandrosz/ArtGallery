package com.example.artgallery.infrastructure.paintings

data class CreateImageResponse(
    val data: List<ImageData>
)

data class ImageData(
    val url: String
)