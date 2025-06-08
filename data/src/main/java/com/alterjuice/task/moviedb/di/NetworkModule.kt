package com.alterjuice.task.moviedb.di

import com.alterjuice.task.moviedb.auth.TokenProvider
import com.alterjuice.task.moviedb.auth.impl.StaticAuthProvider
import com.alterjuice.task.moviedb.network.factories.HttpClientFactory
import com.alterjuice.task.moviedb.network.factories.HttpClientFactoryImpl
import com.alterjuice.task.moviedb.network.service.MovieApiService
import com.alterjuice.task.moviedb.network.service.MovieApiServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface INetworkModule {

    @Binds
    @Singleton
    fun bindsTokenProvider(impl: StaticAuthProvider): TokenProvider

    @Binds
    @Singleton
    fun bindsHttpClientFactory(impl: HttpClientFactoryImpl): HttpClientFactory
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    internal fun provideHttpClient(
        factory: HttpClientFactory,
    ): HttpClient {
        return factory.create()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(httpClient: HttpClient): MovieApiService {
        return MovieApiServiceImpl(httpClient)
    }
}