package com.example.artgallery.presentation.add_painting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artgallery.application.add_painting.AddPaintingViewModel
import com.google.accompanist.permissions.isGranted


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

    Scaffold(
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
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (addPaintingState.text != null) {
                    Text(
                        text = addPaintingState.text!!,
                        fontSize = 24.sp
                    )
                }
                Spacer(modifier = Modifier.height(45.dp))

                Button(onClick = {
                    if (permissionState.status.isGranted) {
                        speechRecognizerLauncher.launch(Unit)
                    } else
                        permissionState.launchPermissionRequest()
                }) {
                    Text(text = "Speak")
                }

            }

        }
    )
}
