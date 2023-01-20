package com.example.artgallery.application.paintings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artgallery.domain.paintings.PaintingsFacade
import com.example.artgallery.domain.shared.Wrapped
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaintingsViewModel @Inject constructor(
    private val _paintingsFacade: PaintingsFacade
) : ViewModel() {

    private val _paintingsState = MutableStateFlow(PaintingsState())
    val paintingsState: StateFlow<PaintingsState> = _paintingsState.asStateFlow()

    init {
        getPaintings()
    }

    private fun getPaintings() = viewModelScope.launch {
        _paintingsFacade.getPaintings().collect { result ->
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