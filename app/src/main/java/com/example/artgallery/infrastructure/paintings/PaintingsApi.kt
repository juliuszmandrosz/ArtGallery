package com.example.artgallery.infrastructure.paintings

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaintingsApi {
    @POST("images/generations")
    suspend fun createImage(@Body body: CreateImageRequest): Response<CreateImageResponse>
}