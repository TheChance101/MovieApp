package com.karrar.movieapp.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.karrar.movieapp.data.remote.service.MovieService
import com.karrar.movieapp.domain.mappers.ActorMapper
import com.karrar.movieapp.domain.mappers.ListMapper
import com.karrar.movieapp.domain.models.Actor
import javax.inject.Inject


class ActorDataSource @Inject constructor(
    private val repository: MovieService,
    private val actorMapper: ActorMapper
) : PagingSource<Int, Actor>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Actor> {
        val pageNumber = params.key ?: 1
        return try {
            val response = repository.getTrendingActors(page = pageNumber)
            val items = ListMapper(actorMapper).mapList(response.body()?.items!!)

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = response.body()?.page?.plus(1)
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Actor>): Int? {
        return null
    }
}