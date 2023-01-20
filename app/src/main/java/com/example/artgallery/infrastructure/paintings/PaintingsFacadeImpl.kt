package com.example.artgallery.infrastructure.paintings

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.ui.text.intl.Locale
import com.bumptech.glide.Glide
import com.example.artgallery.ArtGalleryApplication
import com.example.artgallery.domain.paintings.Painting
import com.example.artgallery.domain.paintings.PaintingsFacade
import com.example.artgallery.domain.shared.Wrapped
import com.example.artgallery.infrastructure.translations.TranslateRequest
import com.example.artgallery.infrastructure.translations.TranslationsApi
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class PaintingsFacadeImpl @Inject constructor(
    private val _paintingDao: PaintingDao,
    private val _paintingsApi: PaintingsApi,
    private val _translationsApi: TranslationsApi,
    private val _firebaseStorage: FirebaseStorage
) :
    PaintingsFacade {
    override suspend fun getPaintings(): Flow<Wrapped<List<Painting>>> {
        return callbackFlow {
            _paintingDao.getAll().onStart {
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
                val imageBytes = convertImageUrlToByteArray(painting.imageUrl)
                val imageUrl = uploadImageToFirebase(painting.id, imageBytes)
                _paintingDao.insert(painting.copy(imageUrl = imageUrl))
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
                _paintingDao.update(painting)
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
                _paintingDao.delete(painting)
                emit(Wrapped.Success(Unit))
            } catch (e: Exception) {
                emit(Wrapped.Error(Throwable(e.message)))
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun drawPainting(description: String): Flow<Wrapped<String>> {
        return flow {
            emit(Wrapped.Loading)
            try {
                val translation = translateText(description)
                val image = _paintingsApi.createImage(CreateImageRequest(translation))
                emit(Wrapped.Success(image.body()!!.data.first().url))
            } catch (e: Exception) {
                emit(Wrapped.Error(Throwable(e.message)))
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun convertImageUrlToByteArray(imageUrl: String) = withContext(Dispatchers.IO) {
        val drawable =
            Glide.with(ArtGalleryApplication.appContext).load(Uri.parse(imageUrl))
                .submit().get()
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.toByteArray()
    }

    private suspend fun uploadImageToFirebase(paintingId: UUID, imageBytes: ByteArray) =
        withContext(Dispatchers.IO) {
            val storageRef = _firebaseStorage.reference.child("images/$paintingId")
            storageRef.putBytes(imageBytes)
                .await().storage.downloadUrl.await().toString()
        }

    private suspend fun translateText(text: String): String {
        return _translationsApi.translate(
            TranslateRequest(
                listOf(text),
                Locale.current.language, "EN"
            )
        ).body()!!.translations.first().text
    }
}