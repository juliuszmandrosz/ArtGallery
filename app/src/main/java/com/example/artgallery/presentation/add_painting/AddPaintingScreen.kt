package com.example.artgallery.presentation.add_painting

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.artgallery.application.add_painting.AddPaintingViewModel
import com.example.artgallery.domain.paintings.Painting
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun AddPaintingScreen(
    navigator: DestinationsNavigator,
    addPaintingViewModel: AddPaintingViewModel = hiltViewModel()
) {
    val addPaintingState by addPaintingViewModel.addPaintingState.collectAsState()

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )
    SideEffect {
        permissionState.launchPermissionRequest()
    }


    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract(),
        onResult = {
            addPaintingViewModel.changeTextValue(it.toString())
        }
    )

    var title by rememberSaveable { mutableStateOf("") }

    when {
        addPaintingState.isSuccess -> {
            navigator.navigateUp()
        }
    }


    Scaffold(
        floatingActionButton = {
            when {
                addPaintingState.isPaintingAdding -> CircularProgressIndicator()
                !addPaintingState.isPaintingAdding -> FloatingActionButton(onClick = {
                    if (title.isEmpty() || addPaintingState.imageUrl.isNullOrEmpty()) return@FloatingActionButton
                    addPaintingViewModel.addPainting(
                        Painting(
                            title = title,
                            imageUrl = addPaintingState.imageUrl!!
                        )
                    )
                }) {
                    Icon(Icons.Default.Save, "Save")
                }
            }
        },
        topBar = {
            TopAppBar(title = {
                Text(text = "Add Painting")
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
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") })
                Spacer(Modifier.height(20.dp))

                if (!addPaintingState.description.isNullOrEmpty()) {
                    Text(
                        text = addPaintingState.description!!,
                        fontSize = 24.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                IconButton(
                    onClick = {
                        if (permissionState.status.isGranted) {
                            speechRecognizerLauncher.launch(Unit)
                        } else
                            permissionState.launchPermissionRequest()
                    }) {
                    Icon(Icons.Filled.Mic, contentDescription = "Speak")
                }

                Spacer(modifier = Modifier.height(20.dp))


                if (!addPaintingState.isPaintingDrawing) {
                    Button(onClick = {
                        addPaintingViewModel.drawPainting()
                    }) {
                        Text(text = "Draw")
                    }
                }

                when {
                    addPaintingState.isPaintingDrawing -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    !addPaintingState.imageUrl.isNullOrEmpty() -> {
                        SubcomposeAsyncImage(
                            model = addPaintingState.imageUrl,
                            contentDescription = "Image",
                            loading = {
                                CircularProgressIndicator()
                            }
                        )
                    }
                }

            }

        }
    )
}
