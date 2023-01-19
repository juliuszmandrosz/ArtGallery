package com.example.artgallery.application.di

import com.example.artgallery.infrastructure.ArtGalleryDatabase
import android.content.Context
import androidx.room.Room
import com.example.artgallery.domain.paintings.PaintingsFacade
import com.example.artgallery.infrastructure.paintings.PaintingDao
import com.example.artgallery.infrastructure.paintings.PaintingsFacadeImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

    @Singleton
    @Provides
    fun providePaintingsFacade(dao: PaintingDao): PaintingsFacade = PaintingsFacadeImpl(dao)
}