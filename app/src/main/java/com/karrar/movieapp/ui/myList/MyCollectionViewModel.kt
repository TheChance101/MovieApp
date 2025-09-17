package com.karrar.movieapp.ui.myList

import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.usecases.mylist.CreateMovieListUseCase
import com.karrar.movieapp.domain.usecases.mylist.GetMyListUseCase
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.category.uiState.ErrorUIState
import com.karrar.movieapp.ui.myList.myCollectionUIState.CreateCollectionDialogUIState
import com.karrar.movieapp.ui.myList.myCollectionUIState.CreatedCollectionUIState
import com.karrar.movieapp.ui.myList.myCollectionUIState.MyCollectionUIEvent
import com.karrar.movieapp.ui.myList.myCollectionUIState.MyCollectionUIState
import com.karrar.movieapp.utilities.ErrorUI.INTERNET_CONNECTION
import com.karrar.movieapp.utilities.ErrorUI.NEED_LOGIN
import com.karrar.movieapp.utilities.ErrorUI.NO_LOGIN
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyCollectionViewModel @Inject constructor(
    private val createMovieListUseCase: CreateMovieListUseCase,
    private val getMyListUseCase: GetMyListUseCase,
    private val createdCollectionUIMapper: CreatedCollectionUIMapper,
) : BaseViewModel(), CreatedListInteractionListener {

    private val _createdListUIState = MutableStateFlow(MyCollectionUIState())
    val createdListUIState = _createdListUIState.asStateFlow()

    private val _createCollectionDialogUIState = MutableStateFlow(CreateCollectionDialogUIState())
    val createListDialogUIState = _createCollectionDialogUIState.asStateFlow()

    private val _myCollectionUIEvent: MutableStateFlow<Event<MyCollectionUIEvent?>> = MutableStateFlow(Event(null))
    val myListUIEvent = _myCollectionUIEvent.asStateFlow()

    override fun getData() {
        _createdListUIState.update {
            it.copy(
                isLoading = true,
                isEmpty = false,
                error = emptyList()
            )
        }
        viewModelScope.launch {
            try {
                val list = getMyListUseCase().map { createdCollectionUIMapper.map(it) }
                _createdListUIState.update {
                    it.copy(isLoading = false, isEmpty = list.isEmpty(), createdList = list)
                }
            } catch (t: Throwable) {
                setError(t)
            }
        }
    }

    fun onListNameInputChange(listName: CharSequence) {
        _createCollectionDialogUIState.update { it.copy(mediaListName = listName.toString()) }
    }

    fun onCreateList() {
        _myCollectionUIEvent.update { Event(MyCollectionUIEvent.CreateButtonClicked) }
    }

    fun onClickAddList() {
        viewModelScope.launch {
            try {
                _createdListUIState.update {
                    it.copy(
                        isLoading = false,
                        createdList = createMovieListUseCase(_createCollectionDialogUIState.value.mediaListName)
                            .map { createdCollectionUIMapper.map(it) },
                        error = emptyList(),
                        isEmpty = false,
                    )
                }
            } catch (t: Throwable) {
                _myCollectionUIEvent.update { Event(MyCollectionUIEvent.DisplayError(t.message.toString())) }
            }
            _createCollectionDialogUIState.update { it.copy(mediaListName = "") }
            _myCollectionUIEvent.emit(Event(MyCollectionUIEvent.CLickAddEvent))
        }
    }

    override fun onListClick(item: CreatedCollectionUIState) {
        _myCollectionUIEvent.update { Event(MyCollectionUIEvent.OnSelectItem(item)) }
    }

    private fun setError(t: Throwable) {
        _createdListUIState.update {
            val error = if (t.message == NO_LOGIN) {
                listOf(ErrorUIState(NEED_LOGIN, t.message.toString()))
            } else {
                listOf(ErrorUIState(INTERNET_CONNECTION, t.message.toString()))
            }
            it.copy(isLoading = false, error = error)
        }
    }
}