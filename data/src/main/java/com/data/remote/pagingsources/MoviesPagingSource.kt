package com.data.remote.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.data.remote.mapper.MoviesListMapper
import com.data.remote.service.MoviesService
import com.domain.common.model.Movie

class MoviesPagingSource(
    private val service: MoviesService,
    private val mapper: MoviesListMapper
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        // Start refresh at position 1 if undefined.
        val position = params.key ?: 1
        return try {
            val results = service.getPopularMovies(position).results
            val response = mapper.mapFromRemote(results)
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                // assume that if a full page is not loaded, that means the end of the data
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return null
    }
}