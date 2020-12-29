package com.breakingbad.ui.list

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.breakingbad.R
import com.breakingbad.common.createViewModelFactory
import com.breakingbad.common.mapExceptionToMessageId
import com.breakingbad.data.model.Character
import com.breakingbad.data.repository.CharacterRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.empty_message_layout.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A List [Fragment] that will display all characters.
 */
@AndroidEntryPoint
class CharacterListFragment : Fragment(R.layout.fragment_list) {

    private val adapter = CharacterListAdapter({ onFavoriteClicked(it) }, { onCharacterClicked(it) })

    @Inject
    lateinit var repository: CharacterRepository

    private val viewModel by navGraphViewModels<CharacterViewModel>(R.id.nav_graph) {
        createViewModelFactory { CharacterViewModel(repository) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setListeners()
    }

    private fun setListeners() {
        viewModel.loading().observe(viewLifecycleOwner) { setLoading(it) }
        viewModel.getError().observe(viewLifecycleOwner) { displayMessage(it, R.drawable.ic_something_went_wrong) }
        viewModel.getCharacters().observe(viewLifecycleOwner) { setListItems(it) }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when(loadStates.refresh) {
                    is LoadState.Error -> {
                        val message: Int = mapExceptionToMessageId((loadStates.refresh as LoadState.Error).error)
                        displayMessage(message, R.drawable.ic_something_went_wrong)
                    }
                }
            }
        }
    }

    private fun displayMessage(messageId: Int, iconId: Int) {
        recyclerListView.isVisible = false
        emptyMessageView.isVisible = true
        messageImage.setImageResource(iconId)
        messageDescription.text = resources.getString(messageId)
    }

    private fun onCharacterClicked(item: Character) {
        findNavController().navigate(Uri.parse(requireContext().getString(R.string.detail_deep_link, item.id, item.name)))
    }

    private fun onFavoriteClicked(character: Character) {
        viewModel.setCharacterAsFavorite(character)
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            recyclerListView?.isVisible = false
            progressBar.show()
        } else {
            recyclerListView?.isVisible = true
            progressBar.hide()
        }
    }

    private fun initData() {
        recyclerListView?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = DefaultItemAnimator()
            it.adapter = adapter
            it.setHasFixedSize(false)
        }
    }

    private fun cleanMessage() {
        recyclerListView?.isVisible = true
        emptyMessageView?.isVisible = false
    }

    private fun setListItems(characters: PagingData<Character>) {
        lifecycleScope.launch {
            adapter.submitData(characters)
            cleanMessage()
        }
    }
}
