package com.karrar.movieapp.ui.myList.myCollectionUIState

sealed interface MyCollectionUIEvent {
    object CreateButtonClicked : MyCollectionUIEvent
    object CLickAddEvent : MyCollectionUIEvent
    data class OnSelectItem(val createdCollectionUIState: CreatedCollectionUIState) : MyCollectionUIEvent
    data class DisplayError(val errorMessage: String) : MyCollectionUIEvent
}