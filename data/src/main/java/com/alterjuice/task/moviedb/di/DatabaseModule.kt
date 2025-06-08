package com.alterjuice.task.moviedb.di

import android.content.Context
import androidx.room.Room
import com.alterjuice.task.moviedb.database.AppDatabase
import com.alterjuice.task.moviedb.database.AppDatabaseConsts
import com.alterjuice.task.moviedb.database.dao.MovieDao
import com.alterjuice.task.moviedb.database.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabaseConsts.DATABASE_NAME
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.remoteKeysDao()
    }
}