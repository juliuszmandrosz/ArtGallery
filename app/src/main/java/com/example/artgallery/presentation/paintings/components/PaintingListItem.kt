package com.example.artgallery.presentation.paintings.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artgallery.application.paintings.PaintingsViewModel
import com.example.artgallery.domain.paintings.Painting

@Composable
fun PaintingListItem(
    painting: Painting,
    paintingsViewModel: PaintingsViewModel = hiltViewModel(),
    ) {
    val showUpdateDialog = remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Title: ${painting.title}",
        )
        Row() {
            IconButton(onClick = {
                showUpdateDialog.value = true
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = {
                paintingsViewModel.deletePainting(painting)
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
    if (showUpdateDialog.value) {
        UpdatePaintingDialog(
            painting = painting,
            showDialog = showUpdateDialog,
        )
    }
}