package com.data.remote.service

import com.data.remote.model.MovieRemote
import com.data.remote.model.RemoteResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    companion object {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<MoviesService>()
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
    ): RemoteResponse<List<MovieRemote>>

}
