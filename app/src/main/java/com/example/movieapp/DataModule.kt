package com.example.movieapp


import android.content.Context
import androidx.room.Room
import com.example.movieapp.data.remote.MovieApi
import com.example.movieapp.data.remote.dao.BookmarkDao
import com.example.movieapp.data.remote.local.entity.MovieDatabase
import com.example.movieapp.data.remote.repository.MovieRepositoryImpl
import com.example.movieapp.domain.model.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: MovieDatabase): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApi,
        bookmarkDao: BookmarkDao
    ): MovieRepository {
        return MovieRepositoryImpl(api, bookmarkDao)
    }
}