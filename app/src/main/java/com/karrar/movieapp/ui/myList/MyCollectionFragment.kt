package com.karrar.movieapp.ui.myList

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMyListsBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.ui.myList.myCollectionUIState.MyCollectionUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyCollectionFragment : BaseFragment<FragmentMyListsBinding>() {

    override val layoutIdFragment: Int = R.layout.fragment_my_lists
    override val viewModel: MyCollectionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(true, getString(R.string.myList))
        binding.savedList.adapter = CreatedListAdapter(emptyList(), viewModel)
        collectEvent()
    }

    private fun collectEvent() {
        collectLast(viewModel.myListUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: MyCollectionUIEvent) {
        var action: NavDirections? = null
        when (event) {
            MyCollectionUIEvent.CreateButtonClicked -> {
                action = MyCollectionFragmentDirections.actionMyListFragmentToCreateSavedList()
            }
            is MyCollectionUIEvent.DisplayError -> {
                Toast.makeText(requireContext(), event.errorMessage, Toast.LENGTH_LONG).show()
            }
            is MyCollectionUIEvent.OnSelectItem -> {
                action = MyCollectionFragmentDirections.actionMyListFragmentToSavedListFragment(
                    event.createdCollectionUIState.listID,
                    event.createdCollectionUIState.name
                )
            }
            else -> {
            }
        }
        action?.let { findNavController().navigate(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }

}