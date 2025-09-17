package com.karrar.movieapp.ui.onboarding

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseViewModel
import com.karrar.movieapp.utilities.StringValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel(), OnBoardingInteractionListener {

    private val _uiState = MutableStateFlow(OnBoardingState())
    val uiState: StateFlow<OnBoardingState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<OnBoardingScreenEvents>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    init {
        getData()
    }

    override fun getData() {
        getPages()
    }

    private fun getPages() {
        updateState {
            it.copy(
                pages = listOf(
                    PageUiState(
                        imageResId = R.drawable.on_boarding_1,
                        title = StringValue.StringResource(R.string.welcome_to_your_movie_universe),
                        description = StringValue.StringResource(R.string.discover_track_and_rate_your_favorite_movies_series)
                    ),
                    PageUiState(
                        imageResId = R.drawable.on_boarding_2,
                        title = StringValue.StringResource(R.string.track_everything),
                        description = StringValue.StringResource(R.string.your_watchlist_your_ratings_all_in_one_place)
                    ),
                    PageUiState(
                        imageResId = R.drawable.on_boarding_3,
                        title = StringValue.StringResource(R.string.personalized_recommendations),
                        description = StringValue.StringResource(R.string.answer_fun_questions_to_get_handpicked_recommendations)
                    )
                )
            )
        }
    }

    private fun updateState(transform: (OnBoardingState) -> OnBoardingState) {
        _uiState.update { transform(it) }
    }

    private fun sendEvent(event: OnBoardingScreenEvents) {
        viewModelScope.launch {
            _uiEffect.send(event)
        }
    }

    override fun onPageChanged(pageIndex: Int) {
        updateState { it.copy(currentPage = pageIndex) }
    }

    override fun onClickPreviousButton() {
        val prev = uiState.value.currentPage - 1
        if (prev >= 0) {
            updateState { it.copy(currentPage = prev) }
        }
    }

    override fun onClickNextButton() {
        val next = uiState.value.currentPage + 1
        if (next < uiState.value.pages.size) {
            updateState { it.copy(currentPage = next) }
        }
    }

    override fun onClickGetStartedButton() {
        sharedPrefs.edit().putBoolean("onboarding_completed", true).apply()
        sendEvent(OnBoardingScreenEvents.NavigateToLoginScreen)
    }
}