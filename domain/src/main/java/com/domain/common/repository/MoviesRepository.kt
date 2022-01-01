package com.domain.common.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.domain.common.model.Movie
import com.domain.core.repository.Repository

interface MoviesRepository : Repository {

    suspend fun getMoviesList(): LiveData<PagingData<Movie>>
}
