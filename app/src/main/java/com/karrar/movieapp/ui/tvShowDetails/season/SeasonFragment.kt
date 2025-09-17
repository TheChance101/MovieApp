package com.karrar.movieapp.ui.tvShowDetails.season

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentSeasonBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.tvShowDetails.SeasonAdapterUIState
import com.karrar.movieapp.ui.tvShowDetails.TvShowDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeasonFragment: BaseFragment<FragmentSeasonBinding>() {

    override val layoutIdFragment = R.layout.fragment_season
    override val viewModel: TvShowDetailsViewModel by viewModels({ requireParentFragment() })
    private val adapter by lazy { SeasonAdapterUIState(emptyList(), viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seasonList.adapter = adapter

        lifecycleScope.launch {
            viewModel.stateFlow.collectLatest { state ->
                adapter.setItems(state.seriesSeasonsResult)
            }
        }
    }

}