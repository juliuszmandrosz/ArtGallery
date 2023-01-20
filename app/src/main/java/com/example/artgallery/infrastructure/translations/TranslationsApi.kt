package com.example.artgallery.infrastructure.translations

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslationsApi {
    @POST("translate")
    suspend fun translate(@Body body: TranslateRequest): Response<TranslateResponse>
}