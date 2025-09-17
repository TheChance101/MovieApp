package com.karrar.movieapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentHomeBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.home.adapter.HomeAdapter
import com.karrar.movieapp.ui.home.homeUiState.HomeUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutIdFragment = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModels()
    lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
        setAdapter()
        collectEvent()
        collectHomeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

    private fun collectHomeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeUiState.collect { uiState ->
                homeAdapter.setItems(
                    mutableListOf(
                        uiState.popularMovies,
                        uiState.onTheAiringSeries,
                        uiState.upcomingMovies,
                        uiState.recentlyReleasedMovies,
                        uiState.browseEverything,
                        uiState.letUsChooseForYou,
                        uiState.recentlyViewed,
                        uiState.collections
                    )
                )
            }
        }
    }


    private fun setAdapter() {
        homeAdapter = HomeAdapter(mutableListOf(), viewModel, viewLifecycleOwner.lifecycleScope)
        binding.recyclerView.adapter = homeAdapter
    }

    private fun collectEvent() {
        collectLast(viewModel.homeUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: HomeUIEvent) {
        val action = when (event) {
            is HomeUIEvent.ClickMovieEvent -> {
                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(
                    event.movieID
                )
            }
            is HomeUIEvent.ClickSeeAllMovieEvent -> {
                HomeFragmentDirections.actionHomeFragmentToAllMovieFragment(
                    -1, event.mediaType
                )
            }
            is HomeUIEvent.ClickSeeAllTVShowsEvent -> {
                HomeFragmentDirections.actionHomeFragmentToAllMovieFragment(
                    -1,
                    event.mediaType
                )
            }
            is HomeUIEvent.ClickSeriesEvent -> {
                HomeFragmentDirections.actionHomeFragmentToTvShowDetailsFragment(
                    event.seriesID
                )
            }
            is HomeUIEvent.ClickBrowseEverythingEvent -> {
                HomeFragmentDirections.actionHomeFragmentToExploringFragment()
            }

            is HomeUIEvent.ClickSeeAllRecentlyViewedEvent -> {
                HomeFragmentDirections.actionHomeFragmentToWatchHistoryFragment()
            }

            is HomeUIEvent.ClickLetUsChooseForYouEvent -> {
                HomeFragmentDirections.actionHomeFragmentToMyListFragment()
            }

            is HomeUIEvent.ClickCollectionList -> {
                HomeFragmentDirections.actionHomeFragmentToListDetailsFragment(
                    event.list.listID,
                    event.list.name
                )
            }

            HomeUIEvent.ClickSeeAllCollectionsEvent -> {
                HomeFragmentDirections.actionHomeFragmentToMyListFragment()
            }
        }
        findNavController().navigate(action)
    }

}
