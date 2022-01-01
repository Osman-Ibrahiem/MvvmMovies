package com.data.remote.mapper

import com.data.remote.model.MovieRemote
import com.domain.common.model.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MovieMapper @Inject constructor(
    private val dateMapper: DateMapper,
) : Mapper<MovieRemote?, Movie> {

    override fun mapFromRemote(
        type: MovieRemote?,
    ): Movie {
        val votes = ((type?.voteAverage ?: 0.0) * 10).toInt()
        return Movie(
            id = type?.id ?: 0L,
            title = type?.title ?: "",
            poster = type?.posterPath ?: "",
            date = dateMapper.mapFromRemote(type?.releaseDate),
            votes = votes,
            votesString = "${votes}%",
            votesColor = when (votes) {
                in 0..20 -> "#ff0000"
                in 21..40 -> "#ffa700"
                in 41..60 -> "#fff400"
                in 61..80 -> "#a3ff00"
                in 81..100 -> "#2cba00"
                else -> "#000000"
            }
        )
    }
}
