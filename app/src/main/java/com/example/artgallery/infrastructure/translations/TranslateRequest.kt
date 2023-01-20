package com.example.artgallery.infrastructure.translations

data class TranslateRequest(
    val text: List<String>,
    val source_lang: String,
    val target_lang: String
)
