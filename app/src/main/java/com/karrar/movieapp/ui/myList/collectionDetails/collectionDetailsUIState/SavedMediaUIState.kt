package com.karrar.movieapp.ui.myList.collectionDetails.collectionDetailsUIState

import com.karrar.movieapp.utilities.Constants

data class SavedMediaUIState(
    val mediaID: Int = 0,
    val title: String = "",
    val mediaType: String = Constants.MOVIE,
    val voteAverage: Double = 0.0,
    val releaseDate: String = "",
    val image: String = "",
)