package com.karrar.movieapp.ui.myList.collectionDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentListDetailsBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.myList.collectionDetails.collectionDetailsUIState.CollectionDetailsUIEvent
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionDetailsFragment : BaseFragment<FragmentListDetailsBinding>() {
    override val layoutIdFragment = R.layout.fragment_list_details
    override val viewModel: CollectionDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(true, viewModel.args.listName)
        binding.lists.adapter = ListDetailsAdapter(mutableListOf(), viewModel)
        collectLast(viewModel.listDetailsUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: CollectionDetailsUIEvent) {
        if (event is CollectionDetailsUIEvent.OnItemSelected) {
            if (event.savedMediaUIState.mediaType == Constants.MOVIE) {
                navigateToMovieDetails(event.savedMediaUIState.mediaID)
            } else {
                navigateToTvShowDetails(event.savedMediaUIState.mediaID)
            }
        }
    }

    private fun navigateToMovieDetails(id: Int) {
        findNavController().navigate(
            CollectionDetailsFragmentDirections.actionSavedListFragmentToMovieDetailFragment(id)
        )
    }

    private fun navigateToTvShowDetails(id: Int) {
        findNavController().navigate(
            CollectionDetailsFragmentDirections.actionListDetailsFragmentToTvShowDetailsFragment(id)
        )
    }

}