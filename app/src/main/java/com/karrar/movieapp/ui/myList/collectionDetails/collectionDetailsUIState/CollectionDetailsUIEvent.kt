package com.karrar.movieapp.ui.myList.collectionDetails.collectionDetailsUIState

sealed interface CollectionDetailsUIEvent {
    data class OnItemSelected(val savedMediaUIState: SavedMediaUIState) : CollectionDetailsUIEvent
}