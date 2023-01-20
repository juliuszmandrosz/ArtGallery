package com.example.artgallery.presentation.paintings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artgallery.application.paintings.PaintingsViewModel
import com.example.artgallery.presentation.destinations.AddPaintingScreenDestination
import com.example.artgallery.presentation.paintings.components.PaintingItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@RootNavGraph(start = true)
@Destination
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PaintingsScreen(
    paintingsViewModel: PaintingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val paintingsState by paintingsViewModel.paintingsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Art Gallery")
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigator.navigate(AddPaintingScreenDestination())
            }) {
                Icon(Icons.Filled.Add, "")
            }
        },
        content = { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3)
                        ) {
                            items(paintingsState.paintings.size) { i ->
                                PaintingItem(paintingsState.paintings[i], navigator)
                            }
                        }
                    }
                }


            }
        }
    )

}