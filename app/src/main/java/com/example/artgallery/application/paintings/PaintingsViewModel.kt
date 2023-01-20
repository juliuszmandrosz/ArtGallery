package com.example.artgallery.application.paintings

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.domain.paintings.PaintingsFacade
import com.example.artgallery.domain.shared.Wrapped
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaintingsViewModel @Inject constructor(
    private val _facade: PaintingsFacade,
    @ApplicationContext private val _context: Context
) : ViewModel() {

    private val _paintingsState = MutableStateFlow(PaintingsState())
    val paintingsState: StateFlow<PaintingsState> = _paintingsState.asStateFlow()

    init {
        getPaintings()
    }

    fun updatePainting(painting: Painting) = viewModelScope.launch {
        _facade.updatePainting(painting).collect { result ->
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

    private fun getPaintings() = viewModelScope.launch {
        _facade.getPaintings().collect { result ->
            when (result) {
                is Wrapped.Success -> {
                    _paintingsState.update {
                        it.copy(paintings = result.data, isLoading = false)
                    }
                }
                is Wrapped.Error -> {
                    _paintingsState.update {
                        it.copy(isLoading = false, errorMsg = result.exception.message)
                    }
                }
                is Wrapped.Loading -> {
                    _paintingsState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}