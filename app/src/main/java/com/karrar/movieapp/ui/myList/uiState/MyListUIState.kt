package com.karrar.movieapp.ui.myList.uiState

import com.karrar.movieapp.ui.category.uiState.ErrorUIState

data class MyListUIState(
    val createdList: List<CreatedListUIState> = emptyList(),
    val isLoading: Boolean = false,
    val error: List<ErrorUIState> = emptyList()
)