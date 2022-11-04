package com.thechance.movie.ui.myList

import com.thechance.movie.ui.myList.myListUIState.CreatedListUIState
import javax.inject.Inject

class CreatedListUIMapper @Inject constructor() :
    com.devfalah.usecases.mappers.Mapper<com.devfalah.models.CreatedList, CreatedListUIState> {

    override fun map(input: com.devfalah.models.CreatedList): CreatedListUIState {
        return CreatedListUIState(
            listID = input.id,
            name = input.name,
            mediaCounts = input.itemCount
        )
    }
}

