package com.karrar.movieapp.ui.onboarding

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter

class OnBoardingPagerAdapter(
    pages: List<PageUiState>,
    listener: OnBoardingInteractionListener
) : BaseAdapter<PageUiState>(pages, listener) {

    override val layoutID: Int = R.layout.item_onboarding_page
    fun updatePages(newPages: List<PageUiState>) {
        setItems(newPages)
    }
}