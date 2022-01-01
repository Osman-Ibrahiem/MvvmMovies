package com.osman.themoviedb.di

import android.app.Application
import android.content.Context
import com.domain.core.di.annotations.qualifiers.ApiKey
import com.domain.core.di.annotations.qualifiers.AppContext
import com.domain.core.di.annotations.qualifiers.AppRemoteUrl
import com.domain.core.di.annotations.qualifiers.ImageRemoteUrl
import com.osman.themoviedb.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @AppRemoteUrl
    @Provides
    fun serviceURl(): String {
        return "${BuildConfig.BASE_URL}/3/"
    }

    @ImageRemoteUrl
    @Provides
    fun imagesURl(): String {
        return BuildConfig.IMAGE_URL
    }

    @ApiKey
    @Provides
    fun apiKey(): String {
        return BuildConfig.API_KEY
    }

    @AppContext
    @Provides
    fun context(application: Application): Context {
        return application.applicationContext
    }

}