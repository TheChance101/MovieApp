package com.karrar.movieapp.ui.myList.myCollectionUIState

import com.karrar.movieapp.ui.category.uiState.ErrorUIState

data class MyCollectionUIState(
    val createdList: List<CreatedCollectionUIState> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: List<ErrorUIState> = emptyList()
)