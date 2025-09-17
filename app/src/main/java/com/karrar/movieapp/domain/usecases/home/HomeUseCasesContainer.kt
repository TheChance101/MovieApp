package com.karrar.movieapp.domain.usecase.home

import com.karrar.movieapp.domain.usecase.home.getData.GetTrendingActorsUseCase
import com.karrar.movieapp.domain.usecase.home.getData.series.GetAiringTodaySeriesUseCase
import com.karrar.movieapp.domain.usecase.home.getData.series.GetOnTheAirSeriesUseCase
import com.karrar.movieapp.domain.usecase.home.getData.series.GetTopRatedTvShowSeriesUseCase
import com.karrar.movieapp.domain.usecase.home.getData.movie.*
import com.karrar.movieapp.domain.usecases.CheckIfLoggedInUseCase
import com.karrar.movieapp.domain.usecases.GetAccountDetailsUseCase
import com.karrar.movieapp.domain.usecases.GetWatchHistoryUseCase
import com.karrar.movieapp.domain.usecases.mylist.GetMyListUseCase

import javax.inject.Inject

class HomeUseCasesContainer @Inject constructor(
    val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    val getAiringTodayUseCase: GetAiringTodaySeriesUseCase,
    val getOnTheAirUseCase: GetOnTheAirSeriesUseCase,
    val getTopRatedTvShowUseCase: GetTopRatedTvShowSeriesUseCase,
    val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    val getMysteryMoviesUseCase: GetMysteryMoviesUseCase,
    val getAdventureMoviesUseCase: GetAdventureMoviesUseCase,
    val getNowStreamingMoviesUseCase: GetNowStreamingMoviesUseCase,
    val getTrendingActorsUseCase: GetTrendingActorsUseCase,
    val getWatchHistoryUseCase: GetWatchHistoryUseCase,
    val getMyListUseCase: GetMyListUseCase,
    val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    val checkIfLoggedInUseCase: CheckIfLoggedInUseCase
)