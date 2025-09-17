package com.karrar.movieapp.ui.myList.myCollectionUIState

import com.karrar.movieapp.ui.category.uiState.ErrorUIState

data class CreateCollectionDialogUIState(
    val mediaListName: String = "",
    val error: List<ErrorUIState> = emptyList()
)