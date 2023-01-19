package com.example.artgallery.domain.paintings

import com.example.artgallery.domain.shared.Wrapped
import kotlinx.coroutines.flow.Flow

interface PaintingsFacade {
    suspend fun getPaintings() : Flow<Wrapped<List<Painting>>>
    suspend fun addPainting(painting: Painting) : Flow<Wrapped<Unit>>
    suspend fun updatePainting(painting: Painting) : Flow<Wrapped<Unit>>
    suspend fun deletePainting(painting: Painting) : Flow<Wrapped<Unit>>
}