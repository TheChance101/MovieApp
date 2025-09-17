package com.karrar.movieapp.ui.home.adapter

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.myList.myCollectionUIState.CreatedCollectionUIState

class YourCollectionsAdapter(
    items: List<CreatedCollectionUIState>,
    val listener: YourCollectionsInteractionListener
) :
    BaseAdapter<CreatedCollectionUIState>(items, listener) {
    override val layoutID: Int = R.layout.item_my_collection
}

interface YourCollectionsInteractionListener : BaseInteractionListener {
    fun onClickCollection(collection: CreatedCollectionUIState)
    fun onClickSeeAllCollections()
}