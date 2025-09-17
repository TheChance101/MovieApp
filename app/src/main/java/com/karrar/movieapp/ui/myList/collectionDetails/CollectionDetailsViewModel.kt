package com.karrar.movieapp.ui.myList.collectionDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.mylist.GetMyMediaListDetailsUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.category.uiState.ErrorUIState
import com.karrar.movieapp.ui.myList.collectionDetails.collectionDetailsUIState.CollectionDetailsUIEvent
import com.karrar.movieapp.ui.myList.collectionDetails.collectionDetailsUIState.CollectionDetailsUIState
import com.karrar.movieapp.ui.myList.collectionDetails.collectionDetailsUIState.SavedMediaUIState
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    private val getMyMediaListDetailsUseCase: GetMyMediaListDetailsUseCase,
    private val mediaUIStateMapper: MediaUIStateMapper,
    saveStateHandle: SavedStateHandle
) : BaseViewModel(), ListDetailsInteractionListener {

    val args = CollectionDetailsFragmentArgs.fromSavedStateHandle(saveStateHandle)

    private val _collectionDetailsUIState = MutableStateFlow(CollectionDetailsUIState())
    val listDetailsUIState = _collectionDetailsUIState.asStateFlow()

    private val _collectionDetailsUIEvent = MutableStateFlow<Event<CollectionDetailsUIEvent?>>(Event(null))
    val listDetailsUIEvent = _collectionDetailsUIEvent.asStateFlow()

    init {
        getData()
    }

    override fun getData() {
        _collectionDetailsUIState.update {
            it.copy(isLoading = true, isEmpty = false, error = emptyList())
        }
        viewModelScope.launch {
            try {
                val result =
                    getMyMediaListDetailsUseCase(args.id).map { mediaUIStateMapper.map(it) }
                _collectionDetailsUIState.update {
                    it.copy(
                        isLoading = false,
                        isEmpty = result.isEmpty(),
                        savedMedia = result
                    )
                }

            } catch (t: Throwable) {
                _collectionDetailsUIState.update {
                    it.copy(
                        isLoading = false, error = listOf(
                            ErrorUIState(0, t.message.toString())
                        )
                    )
                }
            }
        }
    }

    override fun onItemClick(item: SavedMediaUIState) {
        _collectionDetailsUIEvent.update { Event(CollectionDetailsUIEvent.OnItemSelected(item)) }
    }

}

