package com.karrar.movieapp.domain.usecases.searchUseCase

import com.karrar.movieapp.domain.models.SearchHistory
import com.karrar.movieapp.domain.usecases.home.mappers.search.SearchHistoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetSearchHistoryUseCase @Inject constructor(
    private val movieRepository: com.thechance.repository.MovieRepository,
    private val searchHistoryMapper: SearchHistoryMapper
) {
    suspend operator fun invoke(): Flow<List<SearchHistory>> {
        return movieRepository.getAllSearchHistory().map { response ->
            response.map { searchHistoryMapper.map(it) }
        }
    }
}