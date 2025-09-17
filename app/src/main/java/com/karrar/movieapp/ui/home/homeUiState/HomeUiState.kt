package com.karrar.movieapp.ui.home.homeUiState

import com.karrar.movieapp.ui.home.HomeItem

data class HomeUiState (
    val popularMovies: HomeItem = HomeItem.Slider(emptyList()),
    val recentlyReleasedMovies: HomeItem = HomeItem.RecentlyReleased(emptyList()),
    val upcomingMovies: HomeItem = HomeItem.Upcoming(emptyList()),
    val onTheAiringSeries: HomeItem = HomeItem.OnTheAiring(emptyList()),
    val browseEverything: HomeItem = HomeItem.BrowseEverything(),
    val letUsChooseForYou: HomeItem = HomeItem.LetUsChooseForYou(),
    val recentlyViewed: HomeItem = HomeItem.RecentlyViewed(emptyList()),
    val collections: HomeItem = HomeItem.CollectionsList(emptyList()),
    val isLoading:Boolean = false,
    val error : List<String> = emptyList(),
    val username: String? = null,
    val isLoggedIn: Boolean = false
)