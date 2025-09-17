package com.karrar.movieapp.ui.home.adapter

import com.karrar.movieapp.R
import com.karrar.movieapp.domain.enums.AllMediaType
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.home.homeUiState.PopularUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.profile.watchhistory.MediaHistoryUiState
import com.karrar.movieapp.ui.profile.watchhistory.WatchHistoryInteractionListener

class RecentlyViewedAdapter(
    items: List<MediaHistoryUiState>,
    val listener: RecentlyViewedInteractionListener
) :
    BaseAdapter<MediaHistoryUiState>(items, listener) {
    override val layoutID: Int = R.layout.item_recently_viewed
}

interface RecentlyViewedInteractionListener : BaseInteractionListener {
    fun onClickMovie(item: MediaHistoryUiState)
    fun onClickSeeAllRecentlyViewed()
}