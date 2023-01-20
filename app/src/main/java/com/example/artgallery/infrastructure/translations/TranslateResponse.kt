package com.example.artgallery.infrastructure.translations

data class TranslateResponse(
    val translations: List<Translation>
)

data class Translation(
    val text: String
)
