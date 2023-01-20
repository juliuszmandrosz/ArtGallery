package com.example.artgallery.presentation.paintings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.presentation.destinations.PaintingDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun PaintingItem(
    painting: Painting,
    navigator: DestinationsNavigator,
) {
    SubcomposeAsyncImage(
        model = painting.imageUrl,
        contentDescription = painting.title,
        loading = {
            CircularProgressIndicator(
                Modifier.then(Modifier.size(8.dp))
            )
        },
        modifier = Modifier.clickable {
            navigator.navigate(PaintingDetailsScreenDestination(painting))
        }
    )

}