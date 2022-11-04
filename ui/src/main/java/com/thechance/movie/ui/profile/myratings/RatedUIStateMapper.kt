package com.thechance.movie.ui.profile.myratings

import javax.inject.Inject

class RatedUIStateMapper @Inject constructor() :
    com.devfalah.usecases.mappers.Mapper<com.devfalah.models.Rated, RatedUIState> {
    override fun map(input: com.devfalah.models.Rated): RatedUIState {
        return RatedUIState(
            id = input.id,
            title = input.title,
            posterPath = input.posterPath,
            rating = input.rating,
            mediaType = input.mediaType,
            releaseDate = input.releaseDate
        )
    }
}