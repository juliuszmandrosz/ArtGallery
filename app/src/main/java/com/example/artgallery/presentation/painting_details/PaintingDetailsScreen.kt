package com.example.artgallery.presentation.painting_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.artgallery.application.add_painting.PaintingDetailsViewModel
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.presentation.painting_details.components.UpdatePaintingDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = Painting::class)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PaintingDetailsScreen(
    navigator: DestinationsNavigator,
    paintingDetailsViewModel: PaintingDetailsViewModel = hiltViewModel()
) {

    val paintingDetailsState by paintingDetailsViewModel.paintingDetailsState.collectAsState()

    val showUpdateDialog = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = paintingDetailsState.painting!!.title)
            },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Spacer(modifier = Modifier.weight(1.0f))

                SubcomposeAsyncImage(
                    model = paintingDetailsState.painting!!.imageUrl,
                    contentDescription = "Image",
                    loading = {
                        CircularProgressIndicator()
                    }
                )

                Spacer(modifier = Modifier.weight(1.0f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showUpdateDialog.value = true }) {
                        Text(text = "Edit")
                    }

                    Button(onClick = {
                        paintingDetailsViewModel.deletePainting(paintingDetailsState.painting!!)
                        navigator.navigateUp()
                    }) {
                        Text(text = "Delete")
                    }
                }

            }

            if (showUpdateDialog.value) {
                UpdatePaintingDialog(
                    painting = paintingDetailsState.painting!!,
                    showDialog = showUpdateDialog,
                )
            }

        }
    )
}