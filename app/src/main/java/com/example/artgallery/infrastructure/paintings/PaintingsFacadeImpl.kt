package com.example.artgallery.infrastructure.paintings

import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.domain.paintings.PaintingsFacade
import com.example.artgallery.domain.shared.Wrapped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PaintingsFacadeImpl @Inject constructor(private val paintingDao: PaintingDao) : PaintingsFacade {
    override suspend fun getPaintings(): Flow<Wrapped<List<Painting>>> {
        return callbackFlow {
            paintingDao.getAll().onStart {
                trySend(Wrapped.Loading)
            }.catch { e ->
                trySend(Wrapped.Error(Throwable(e.message)))
            }.collectLatest { list ->
                trySend(Wrapped.Success(list))
            }
            awaitClose { close() }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addPainting(painting: Painting): Flow<Wrapped<Unit>> {
        return flow {
            emit(Wrapped.Loading)
            try {
                paintingDao.insert(painting)
                emit(Wrapped.Success(Unit))
            } catch (e: Exception) {
                emit(Wrapped.Error(Throwable(e.message)))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updatePainting(painting: Painting): Flow<Wrapped<Unit>> {

        return flow {
            emit(Wrapped.Loading)
            try {
                paintingDao.update(painting)
                emit(Wrapped.Success(Unit))
            } catch (e: Exception) {
                emit(Wrapped.Error(Throwable(e.message)))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deletePainting(painting: Painting): Flow<Wrapped<Unit>> {
        return flow {
            emit(Wrapped.Loading)
            try {
                paintingDao.delete(painting)
                emit(Wrapped.Success(Unit))
            } catch (e: Exception) {
                emit(Wrapped.Error(Throwable(e.message)))
            }
        }.flowOn(Dispatchers.IO)
    }
}