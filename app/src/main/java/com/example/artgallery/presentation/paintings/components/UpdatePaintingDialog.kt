package com.example.artgallery.presentation.paintings.components
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artgallery.application.paintings.PaintingsViewModel
import com.example.artgallery.domain.paintings.Painting

@Composable
fun UpdatePaintingDialog(
    painting: Painting,
    showDialog: MutableState<Boolean>,
    viewModel: PaintingsViewModel = hiltViewModel(),
) {
    var title by remember { mutableStateOf(painting.title) }
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
                viewModel.updatePainting(painting.copy(title = title))
                showDialog.value = false
            }) {
                Text(text = "Update")
            }
        }
    )
}