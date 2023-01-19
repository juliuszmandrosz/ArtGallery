package com.example.artgallery.application.add_painting

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
class AddPaintingViewModel @Inject constructor(
    private val _facade: PaintingsFacade,
    @ApplicationContext private val _context: Context
) : ViewModel() {

    private val _addPaintingState = MutableStateFlow(AddPaintingState())
    val addPaintingState: StateFlow<AddPaintingState> = _addPaintingState.asStateFlow()


    fun addPainting(painting: Painting) = viewModelScope.launch {
        _facade.addPainting(painting).collect { result ->
            when (result) {
                is Wrapped.Success -> {
                    Toast.makeText(_context, "Painting added successfully", Toast.LENGTH_SHORT).show()
                }
                is Wrapped.Error -> {
                    Toast.makeText(_context, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                is Wrapped.Loading -> {}
            }
        }
    }

    fun changeTextValue(text: String){
        viewModelScope.launch {
            _addPaintingState.update {
                it.copy(text = text)
            }
        }
    }
}