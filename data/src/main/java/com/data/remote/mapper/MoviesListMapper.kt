package com.data.remote.mapper

import com.data.remote.model.MovieRemote
import com.domain.common.model.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MoviesListMapper @Inject constructor(
    private val movieMapper: MovieMapper,
) : Mapper<List<MovieRemote>?, List<Movie>> {

    override fun mapFromRemote(
        type: List<MovieRemote>?,
    ): List<Movie> {
        return if (type.isNullOrEmpty()) {
            emptyList()
        } else {
            type.map(movieMapper::mapFromRemote)
        }
    }
}
