package com.example.artgallery.domain.shared

sealed class Wrapped<out R> {
    data class Success<out T>(val data: T): Wrapped<T>()
    data class Error(val exception: Throwable): Wrapped<Nothing>()
    object Loading: Wrapped<Nothing>()
}