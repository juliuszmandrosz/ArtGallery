package com.example.artgallery.presentation.paintings.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import coil.compose.SubcomposeAsyncImage
import com.example.artgallery.domain.paintings.Painting

@Composable
fun PaintingItem(
    painting: Painting,
) {
    SubcomposeAsyncImage(
        model = painting.imageUrl,
        contentDescription = painting.title,
        loading = {
            CircularProgressIndicator()
        }
    )

}