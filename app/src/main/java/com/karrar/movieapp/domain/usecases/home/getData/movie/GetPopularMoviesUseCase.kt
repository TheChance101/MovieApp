package com.karrar.movieapp.domain.usecases.home.getData.movie

import com.karrar.movieapp.domain.models.PopularMovie
import com.karrar.movieapp.domain.usecases.home.mappers.movie.PopularMovieMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val movieRepository: com.thechance.repository.MovieRepository,
    private val movieMapper: PopularMovieMapper,
) {
    suspend  operator fun invoke(): Flow<List<PopularMovie>> {
        return movieRepository.getPopularMovies().map { items ->
            items.map(movieMapper::map)
        }
    }
}