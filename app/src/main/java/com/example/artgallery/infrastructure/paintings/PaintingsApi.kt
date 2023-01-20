package com.example.artgallery.infrastructure.paintings

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaintingsApi {
    @POST("https://api.openai.com/v1/images/generations")
    suspend fun createImage(@Body prompt: CreateImageRequest): Response<CreateImageResponse>
}