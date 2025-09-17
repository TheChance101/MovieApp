package com.karrar.movieapp.ui.explore

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentExploringBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.explore.exploreUIState.ExploringUIEvent
import com.karrar.movieapp.ui.explore.exploreUIState.TrendyMediaUIState
import com.karrar.movieapp.utilities.Constants
import com.google.android.material.tabs.TabLayout
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExploringFragment : BaseFragment<FragmentExploringBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_exploring
    override val viewModel: ExploringViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(true, resources.getString(R.string.explore_label))
        collectEvent()
        binding.recyclerTrend.adapter = TrendAdapter(mutableListOf(), viewModel)

        // Add your tabs
        binding.includeTabs.layout.apply {
            addTab(newTab().setText("Movies"))
            addTab(newTab().setText("TV Shows"))
            addTab(newTab().setText("Actors"))
        }

        // Listen for tab selection
        binding.includeTabs.layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.onClickMovies()
                    1 -> viewModel.onClickTVShow()
                    2 -> viewModel.onClickActors()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun collectEvent() {
        collectLast(viewModel.exploringUIEvent) {
            it?.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: ExploringUIEvent) {
        when (event) {
            ExploringUIEvent.ActorsEvent -> {
                findNavController().navigate(ExploringFragmentDirections.actionExploringFragmentToActorsFragment())
            }
            ExploringUIEvent.MoviesEvent -> {
                findNavController().navigate(
                    ExploringFragmentDirections.actionExploringFragmentToCategoryFragment(
                        Constants.MOVIE_CATEGORIES_ID
                    )
                )
            }
            ExploringUIEvent.SearchEvent -> navigateToSearch()
            ExploringUIEvent.TVShowEvent -> {
                findNavController().navigate(
                    ExploringFragmentDirections.actionExploringFragmentToCategoryFragment(
                        Constants.TV_CATEGORIES_ID
                    )
                )
            }
            is ExploringUIEvent.TrendEvent -> {
                navigateToMediaDetails(event.trendyMediaUIState)
            }
        }
    }

    private fun navigateToSearch() {
        val extras = FragmentNavigatorExtras(binding.inputSearch to "search_box")
        Navigation.findNavController(binding.root)
            .navigate(
                ExploringFragmentDirections.actionExploringFragmentToSearchFragment(),
                extras
            )
    }

    private fun navigateToMediaDetails(item: TrendyMediaUIState) {
        when (item.mediaType) {
            Constants.MOVIE -> {
                findNavController().navigate(
                    ExploringFragmentDirections.actionExploringFragmentToMovieDetailFragment(
                        item.mediaID
                    )
                )
            }
            Constants.TV_SHOWS -> {
                findNavController().navigate(
                    ExploringFragmentDirections.actionExploringFragmentToTvShowDetailsFragment(
                        item.mediaID
                    )
                )
            }
        }
    }

}