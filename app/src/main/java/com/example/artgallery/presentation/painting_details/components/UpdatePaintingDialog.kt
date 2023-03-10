package com.example.artgallery.presentation.painting_details.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artgallery.application.add_painting.PaintingDetailsViewModel
import com.example.artgallery.domain.paintings.Painting

@Composable
fun UpdatePaintingDialog(
    painting: Painting,
    showDialog: MutableState<Boolean>,
    paintingsViewModel: PaintingDetailsViewModel = hiltViewModel(),
) {
    var title by rememberSaveable { mutableStateOf(painting.title) }
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        text = {
            TextField(
                value = title,
                onValueChange = { title = it }
            )
        },
        confirmButton = {
            Button(onClick = {
                paintingsViewModel.updatePainting(painting.copy(title = title))
                showDialog.value = false
            }) {
                Text(text = "Update")
            }
        }
    )
}