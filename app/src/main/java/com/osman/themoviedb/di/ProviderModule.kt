package com.osman.themoviedb.di

import com.data.remote.service.MoviesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Provides
    fun provideMoviesService(retrofit: Retrofit): MoviesService = MoviesService(retrofit)

}