package com.karrar.movieapp.ui.home

import com.karrar.movieapp.domain.enums.HomeItemsType
import com.karrar.movieapp.ui.home.homeUiState.PopularUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.myList.myCollectionUIState.CreatedCollectionUIState
import com.karrar.movieapp.ui.profile.watchhistory.MediaHistoryUiState

sealed class HomeItem(val priority: Int) {

    data class Slider(val items: List<PopularUiState>) : HomeItem(0)

    data class RecentlyReleased(
        val items: List<MediaUiState>,
        val type: HomeItemsType = HomeItemsType.RECENTLY_RELEASED
    ) : HomeItem(1)

    data class LetUsChooseForYou(val ctaNumber: Int = 12) : HomeItem(2)

    data class Upcoming(
        val items: List<MediaUiState>,
        val type: HomeItemsType = HomeItemsType.UPCOMING
    ) : HomeItem(3)

    data class OnTheAiring(
        val items: List<MediaUiState>,
        val type: HomeItemsType = HomeItemsType.TOP_RATED_TV_SHOWS
    ) : HomeItem(6)

    data class RecentlyViewed(
        val items: List<MediaHistoryUiState>,
        val type: HomeItemsType = HomeItemsType.RECENTLY_VIEWED
    ) : HomeItem(7)

    data class CollectionsList(
        val items: List<CreatedCollectionUIState>,
        val type: HomeItemsType = HomeItemsType.YOUR_COLLECTIONS
    ) : HomeItem(8)

    data class BrowseEverything(val ctaNumber: Int = 9) : HomeItem(ctaNumber)

}