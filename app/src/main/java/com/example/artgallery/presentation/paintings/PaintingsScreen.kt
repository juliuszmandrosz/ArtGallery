package com.example.artgallery.presentation.paintings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artgallery.application.paintings.PaintingsViewModel
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.presentation.destinations.AddPaintingScreenDestination
import com.example.artgallery.presentation.paintings.components.PaintingListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@RootNavGraph(start = true)
@Destination
@Composable
fun PaintingsScreen(
    viewModel: PaintingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val paintingsState by viewModel.paintingsState.collectAsState()

    var title by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
                 TopAppBar(title = {
                     Text(text = "Art Gallery")
                 })
        },
        content = { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") })
                Spacer(Modifier.height(20.dp))
                Button(onClick = {
                    viewModel.addPainting(Painting(title = title, imageUrl = ""))
                    title = ""
                }) { Text("Submit") }
                Spacer(Modifier.height(20.dp))
                when {
                    paintingsState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    !paintingsState.errorMsg.isNullOrEmpty() -> {
                        Text(text = paintingsState.errorMsg ?: "Error")
                    }

                    paintingsState.paintings.isEmpty() -> {
                        Text(text = "No paintings")
                    }

                    paintingsState.paintings.isNotEmpty() -> {
                        LazyColumn {
                            items(paintingsState.paintings.size) { i ->
                                PaintingListItem(
                                    painting = paintingsState.paintings[i],
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(onClick = {
                    navigator.navigate(AddPaintingScreenDestination())
                }) {
                    Icon(Icons.Filled.Add,"")
                }
            }
        }
    )

}