package com.karrar.movieapp.ui.myList

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.CreatedList
import com.karrar.movieapp.ui.myList.myCollectionUIState.CreatedCollectionUIState
import javax.inject.Inject

class CreatedCollectionUIMapper @Inject constructor() : Mapper<CreatedList, CreatedCollectionUIState> {

    override fun map(input: CreatedList): CreatedCollectionUIState {
        return CreatedCollectionUIState(
            listID = input.id,
            name = input.name,
            mediaCounts = input.itemCount
        )
    }
}


