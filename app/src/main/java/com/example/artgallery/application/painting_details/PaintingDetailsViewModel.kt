package com.example.artgallery.application.add_painting

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artgallery.application.painting_details.PaintingDetailsState
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.domain.paintings.PaintingsFacade
import com.example.artgallery.domain.shared.Wrapped
import com.example.artgallery.presentation.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaintingDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val _paintingsFacade: PaintingsFacade,
    @ApplicationContext private val _context: Context,
) : ViewModel() {

    private val _paintingDetailsState = MutableStateFlow(PaintingDetailsState())
    val paintingDetailsState: StateFlow<PaintingDetailsState> = _paintingDetailsState.asStateFlow()

    init {
        val painting = savedStateHandle.navArgs<Painting>()
        _paintingDetailsState.update { it.copy(painting = painting) }
    }

    fun updatePainting(painting: Painting) = viewModelScope.launch {
        _paintingDetailsState.update { it.copy(painting = painting) }
        _paintingsFacade.updatePainting(painting).collect { result ->
            when (result) {
                is Wrapped.Success -> {
                    Toast.makeText(_context, "Painting updated successfully", Toast.LENGTH_SHORT)
                        .show()
                }
                is Wrapped.Error -> {
                    Toast.makeText(_context, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                is Wrapped.Loading -> {}
            }
        }
    }

    fun deletePainting(painting: Painting) = viewModelScope.launch {
        _paintingsFacade.deletePainting(painting).collect { result ->
            when (result) {
                is Wrapped.Success -> {
                    Toast.makeText(_context, "Painting deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                }
                is Wrapped.Error -> {
                    Toast.makeText(_context, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                is Wrapped.Loading -> {}
            }
        }
    }


}