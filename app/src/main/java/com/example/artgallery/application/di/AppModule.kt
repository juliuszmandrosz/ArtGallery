package com.example.artgallery.application.di

import android.content.Context
import androidx.room.Room
import com.example.artgallery.BuildConfig
import com.example.artgallery.domain.paintings.PaintingsFacade
import com.example.artgallery.infrastructure.ArtGalleryDatabase
import com.example.artgallery.infrastructure.paintings.PaintingDao
import com.example.artgallery.infrastructure.paintings.PaintingsApi
import com.example.artgallery.infrastructure.paintings.PaintingsFacadeImpl
import com.example.artgallery.infrastructure.translations.TranslationsApi
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): ArtGalleryDatabase =
        Room.databaseBuilder(
            context,
            ArtGalleryDatabase::class.java,
            ArtGalleryDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providePaintingsDao(db: ArtGalleryDatabase): PaintingDao = db.paintingDao()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Provides
    @Singleton
    fun providePaintingsApi(gson: Gson): PaintingsApi =
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(getPaintingHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PaintingsApi::class.java)

    @Provides
    @Singleton
    fun provideTranslationsApi(gson: Gson): TranslationsApi =
        Retrofit.Builder()
            .baseUrl("https://api-free.deepl.com/v2/")
            .client(getTranslationsHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TranslationsApi::class.java)


    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()


    @Singleton
    @Provides
    fun providePaintingsFacade(
        dao: PaintingDao,
        paintingsApi: PaintingsApi,
        storage: FirebaseStorage,
        translationsApi: TranslationsApi

    ): PaintingsFacade =
        PaintingsFacadeImpl(dao, paintingsApi, translationsApi, storage)


    private fun getPaintingHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(getPaintingApiHeaderInterceptor())
            .build()
    }


    private fun getTranslationsHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(getTranslationsApiHeaderInterceptor())
            .build()
    }

    private fun getPaintingApiHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .header("Authorization", "Bearer ${BuildConfig.OPEN_AI_API_KEY}")
                    .header("Content-Type", "application/json")
                    .build()
            chain.proceed(request)
        }
    }

    private fun getTranslationsApiHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .header("Authorization", "DeepL-Auth-Key ${BuildConfig.DEEPL_API_KEY}")
                    .header("Content-Type", "application/json")
                    .build()
            chain.proceed(request)
        }
    }

}