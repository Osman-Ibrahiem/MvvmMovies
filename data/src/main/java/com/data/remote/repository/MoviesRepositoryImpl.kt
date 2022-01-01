package com.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.data.remote.mapper.MoviesListMapper
import com.data.remote.pagingsources.MoviesPagingSource
import com.data.remote.service.MoviesService
import com.domain.common.model.Movie
import com.domain.common.repository.MoviesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val moviesService: MoviesService,
    private val moviesListMapper: MoviesListMapper,
) : MoviesRepository {

    override suspend fun getMoviesList(): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviesPagingSource(moviesService, moviesListMapper)
            }
        ).liveData
    }

}
