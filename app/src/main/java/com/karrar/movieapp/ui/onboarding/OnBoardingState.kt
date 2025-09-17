package com.karrar.movieapp.ui.onboarding

import com.karrar.movieapp.utilities.StringValue

data class OnBoardingState(
    val currentPage: Int = 0,
    val pages: List<PageUiState> = emptyList(),
)

data class PageUiState(
    val imageResId: Int,
    val title: StringValue,
    val description: StringValue
)