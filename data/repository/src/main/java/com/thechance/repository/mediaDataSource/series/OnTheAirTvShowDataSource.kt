package com.thechance.repository.mediaDataSource.series


import com.thechance.remote.response.TVShowsDTO
import com.thechance.remote.service.MovieService
import com.thechance.repository.mediaDataSource.BasePagingSource
import javax.inject.Inject


class OnTheAirTvShowDataSource @Inject constructor(
    private val service: MovieService,
) : BasePagingSource<TVShowsDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TVShowsDTO> {
        val pageNumber = params.key ?: 1

        return try {
            val response = service.getOnTheAir(page = pageNumber)

            LoadResult.Page(
                data = response.body()?.items ?: emptyList(),
                prevKey = null,
                nextKey = response.body()?.page?.plus(1)
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

}