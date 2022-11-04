package com.thechance.movie.ui.profile.watchhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfalah.usecases.GetWatchHistoryUseCase
import com.thechance.movie.utilities.Constants
import com.thechance.movie.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchHistoryViewModel @Inject constructor(
    private val getWatchHistoryUseCase: GetWatchHistoryUseCase,
    private val watchHistoryUIStateMapper: WatchHistoryUIStateMapper,
) : ViewModel(), WatchHistoryInteractionListener {

    private val _uiState = MutableStateFlow(WatchHistoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _watchHistoryUIEvent: MutableStateFlow<Event<WatchHistoryUIEvent?>> =
        MutableStateFlow(Event(null))
    val watchHistoryUIEvent = _watchHistoryUIEvent.asStateFlow()

    init {
        getWatchHistoryData()
    }

    private fun getWatchHistoryData() {
        viewModelScope.launch {
            try {
                getWatchHistoryUseCase().collect { list ->
                    _uiState.update { watchHistoryUiState ->
                        watchHistoryUiState.copy(allMedia = list.map {
                            watchHistoryUIStateMapper.map(
                                it
                            )
                        })
                    }
                }
            } catch (t: Throwable) {
                _uiState.update { it.copy(error = listOf(Error(400, t.message.toString()))) }
            }

        }
    }

    override fun onClickMovie(item: MediaHistoryUiState) {
        if (item.mediaType.equals(Constants.MOVIE, true)) {
            _watchHistoryUIEvent.update { Event(WatchHistoryUIEvent.MovieEvent(item.id)) }
        } else {
            _watchHistoryUIEvent.update { Event(WatchHistoryUIEvent.TVShowEvent(item.id)) }
        }
    }

}