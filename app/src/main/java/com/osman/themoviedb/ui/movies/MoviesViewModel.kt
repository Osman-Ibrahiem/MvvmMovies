package com.osman.themoviedb.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.domain.common.model.Movie
import com.domain.common.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject internal constructor(
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _moviesList = MutableLiveData<PagingData<Movie>>()

    suspend fun getMoviesList(): LiveData<PagingData<Movie>> {
        val response = repository.getMoviesList().cachedIn(viewModelScope)
        _moviesList.value = response.value
        return response
    }
}