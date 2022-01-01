package com.osman.themoviedb.di.remote

import android.content.Context
import com.domain.core.di.annotations.qualifiers.ApiKey
import com.domain.core.di.annotations.qualifiers.AppContext
import com.osman.themoviedb.BuildConfig
import com.osman.themoviedb.di.ApplicationModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module(includes = [ApplicationModule::class])
class OkHttpClientModule {

    @Provides
    fun cache(@AppContext context: Context): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    fun okHttpClientAuth(
        @AppContext context: Context,
        @ApiKey apikey: String,
        responseInterceptor: ResponseInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient()
            .newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url

                val language = Locale.getDefault().toLanguageTag()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", apikey)
                    .addQueryParameter("language", language)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                requestBuilder.header("Content-Type", "application/json")
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(responseInterceptor)

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return okHttpClientBuilder.build()
    }


    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }


}