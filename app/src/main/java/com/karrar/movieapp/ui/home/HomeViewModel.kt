package com.karrar.movieapp.ui.home

import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.domain.enums.AllMediaType
import com.karrar.movieapp.domain.enums.HomeItemsType
import com.karrar.movieapp.domain.mappers.WatchHistoryMapper
import com.karrar.movieapp.domain.usecase.home.HomeUseCasesContainer
import com.karrar.movieapp.ui.adapters.MediaInteractionListener
import com.karrar.movieapp.ui.adapters.MovieInteractionListener
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.ui.home.adapter.RecentlyViewedInteractionListener
import com.karrar.movieapp.ui.home.adapter.TVShowInteractionListener
import com.karrar.movieapp.ui.home.adapter.YourCollectionsInteractionListener
import com.karrar.movieapp.ui.home.homeUiState.HomeUIEvent
import com.karrar.movieapp.ui.home.homeUiState.HomeUiState
import com.karrar.movieapp.ui.mappers.MediaUiMapper
import com.karrar.movieapp.ui.myList.CreatedCollectionUIMapper
import com.karrar.movieapp.ui.myList.myCollectionUIState.CreatedCollectionUIState
import com.karrar.movieapp.ui.profile.watchhistory.MediaHistoryUiState
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCasesContainer: HomeUseCasesContainer,
    private val mediaUiMapper: MediaUiMapper,
    private val popularUiMapper: PopularUiMapper,
    private val watchHistoryMapper: WatchHistoryMapper,
    private val createdCollectionUIMapper: CreatedCollectionUIMapper,
) : BaseViewModel(), HomeInteractionListener, MovieInteractionListener,
    MediaInteractionListener, TVShowInteractionListener, RecentlyViewedInteractionListener,
    YourCollectionsInteractionListener {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _homeUIEvent = MutableStateFlow<Event<HomeUIEvent?>>(Event(null))
    val homeUIEvent = _homeUIEvent.asStateFlow()

    init {
        getHomeData()
    }

    private fun getHomeData() {
        _homeUiState.update { it.copy(isLoading = true) }
        getRecentlyReleased()
        getUpcoming()
        getOnTheAir()
        getPopularMovies()
        getRecentlyViewed()
        getUserName()
        getMyCollections()
    }

    private fun getMyCollections() {
        viewModelScope.launch {
            try {
                val items = homeUseCasesContainer.getMyListUseCase().map { createdCollectionUIMapper.map(it) }
                _homeUiState.update {
                    it.copy(isLoading = false, collections = HomeItem.CollectionsList(items))
                }
            } catch (th: Throwable) {
                onError(th.message.toString())
            }
        }
    }
    private fun getRecentlyViewed() {
        viewModelScope.launch {
            try {
                homeUseCasesContainer.getWatchHistoryUseCase().collect { list ->
                    if(list.isNotEmpty()){
                        val items = list.map(watchHistoryMapper::map)
                        _homeUiState.update {
                            it.copy(
                                recentlyViewed = HomeItem.RecentlyViewed(items),
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (th: Throwable) {
                onError(th.message.toString())
            }

        }
    }
    private fun getUserName() {
        viewModelScope.launch {
            try {
                val isLoggedIn = homeUseCasesContainer.checkIfLoggedInUseCase()
                if (isLoggedIn) {
                    val accountDetails = homeUseCasesContainer.getAccountDetailsUseCase()
                    _homeUiState.update {
                        it.copy(
                            username = accountDetails.username,
                            isLoggedIn = true,
                            isLoading = false
                        )
                    }
                } else {
                    _homeUiState.update {
                        it.copy(
                            username = "",
                            isLoggedIn = false,
                            isLoading = false
                        )
                    }
                }
            } catch (th: Throwable) {
                onError(th.message.toString())
            }
        }
    }

    override fun getData() {
        getHomeData()
        _homeUiState.update { it.copy(error = emptyList()) }
    }


    private fun getPopularMovies() {
        viewModelScope.launch {
            try {
                homeUseCasesContainer.getPopularMoviesUseCase().collect { list ->
                    if (list.isNotEmpty()) {
                        val items = list.map(popularUiMapper::map)
                        _homeUiState.update {
                            it.copy(popularMovies = HomeItem.Slider(items),
                                isLoading = false)
                        }
                    }
                }
            } catch (th: Throwable) {
                onError(th.message.toString())
            }
        }
    }

    private fun onError(message: String) {
        val errors = _homeUiState.value.error.toMutableList()
        errors.add(message)
        _homeUiState.update { it.copy(error = errors, isLoading = false) }
    }

    private fun getUpcoming() {
        viewModelScope.launch {
            try {
                homeUseCasesContainer.getUpcomingMoviesUseCase().collect { list ->
                    if (list.isNotEmpty()) {
                        val items = list.map(mediaUiMapper::map)
                        _homeUiState.update {
                            it.copy(upcomingMovies = HomeItem.Upcoming(items),
                                isLoading = false)
                        }
                    }
                }
            } catch (th: Throwable) {
                onError(th.message.toString())
            }
        }


    }

    private fun getRecentlyReleased() {
        viewModelScope.launch {
            try {
                homeUseCasesContainer.getNowStreamingMoviesUseCase().collect { list ->
                    if (list.isNotEmpty()) {
                        val items = list.map(mediaUiMapper::map)
                        _homeUiState.update {
                            it.copy(recentlyReleasedMovies = HomeItem.RecentlyReleased(items),
                                isLoading = false)
                        }
                    }
                }
            } catch (th: Throwable) {
                onError(th.message.toString())
            }
        }

    }
    private fun getOnTheAir() {
        viewModelScope.launch {
            try {
                homeUseCasesContainer.getOnTheAirUseCase().collect { list ->
                    if (list.isNotEmpty()) {
                        val items = list.map(mediaUiMapper::map)
                        _homeUiState.update {
                            it.copy(onTheAiringSeries = HomeItem.OnTheAiring(items),
                                isLoading = false)
                        }
                    }
                }
            } catch (th: Throwable) {
                onError(th.message.toString())
            }
        }

    }

    override fun onClickMovie(movieId: Int) {
        _homeUIEvent.update { Event(HomeUIEvent.ClickMovieEvent(movieId)) }
    }

    override fun onClickSeeAllMovie(homeItemsType: HomeItemsType) {
        val type = when (homeItemsType) {
            HomeItemsType.TOP_RATED_TV_SHOWS -> AllMediaType.TOP_RATED
            HomeItemsType.RECENTLY_RELEASED -> AllMediaType.RECENTLY_RELEASED
            HomeItemsType.UPCOMING -> AllMediaType.UPCOMING
            HomeItemsType.NON -> AllMediaType.ACTOR_MOVIES
            HomeItemsType.RECENTLY_VIEWED -> TODO("There is no need to add new attribute to AllMediaType")
            HomeItemsType.YOUR_COLLECTIONS -> TODO("There is no need to add new attribute to AllMediaType")
        }
        _homeUIEvent.update { Event(HomeUIEvent.ClickSeeAllMovieEvent(type)) }
    }

    override fun onClickBrowseEverything() {
        _homeUIEvent.update { Event(HomeUIEvent.ClickBrowseEverythingEvent) }
    }

    override fun onClickLetUsChooseForYou() {
        _homeUIEvent.update { Event(HomeUIEvent.ClickLetUsChooseForYouEvent) }
    }

    override fun onClickMedia(mediaId: Int) {
        _homeUIEvent.update { Event(HomeUIEvent.ClickSeriesEvent(mediaId)) }
    }

    override fun onClickTVShow(tVShowID: Int) {
        _homeUIEvent.update { Event(HomeUIEvent.ClickSeriesEvent(tVShowID)) }
    }

    override fun onClickSeeTVShow(type: AllMediaType) {
        _homeUIEvent.update { Event(HomeUIEvent.ClickSeeAllTVShowsEvent(type)) }
    }

    override fun onClickMovie(item: MediaHistoryUiState) {
        if (item.mediaType.equals(Constants.MOVIE, true)) {
            _homeUIEvent.update { Event(HomeUIEvent.ClickMovieEvent(item.id)) }
        } else {
            _homeUIEvent.update { Event(HomeUIEvent.ClickSeriesEvent(item.id)) }
        }
    }

    override fun onClickSeeAllRecentlyViewed() {
        _homeUIEvent.update { Event(HomeUIEvent.ClickSeeAllRecentlyViewedEvent) }
    }

    override fun onClickCollection(collection: CreatedCollectionUIState) {
        _homeUIEvent.update { Event(HomeUIEvent.ClickCollectionList(collection)) }
    }

    override fun onClickSeeAllCollections() {
        _homeUIEvent.update { Event(HomeUIEvent.ClickSeeAllCollectionsEvent) }
    }
}