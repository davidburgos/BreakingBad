package com.breakingbad.ui.list

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.breakingbad.R
import com.breakingbad.common.createViewModelFactory
import com.breakingbad.common.visible
import com.breakingbad.data.model.Character
import kotlinx.android.synthetic.main.empty_message_layout.*
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A List [Fragment] that will display all characters.
 */
class CharacterListFragment : Fragment(R.layout.fragment_list) {

    private val adapter = CharacterListAdapter({ onFavoriteClicked(it) }, { onCharacterClicked(it) })

    private val viewModel by navGraphViewModels<CharacterListViewModel>(R.id.nav_graph) {
        createViewModelFactory { CharacterListViewModel(requireActivity().application) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initData()
    }

    private fun setListeners() {
        viewModel.loading().observe(viewLifecycleOwner) { setLoading(it) }
        viewModel.getError().observe(viewLifecycleOwner) { displayMessage(it, R.drawable.ic_something_went_wrong) }
        viewModel.getCharacters().observe(viewLifecycleOwner) { setListItems(it) }
    }

    private fun displayMessage(messageId: Int, iconId: Int) {
        recyclerListView.visible = false
        emptyMessageView.visible = true
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
            recyclerListView.visible = false
            progressBar.show()
        } else {
            recyclerListView.visible = true
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
        recyclerListView.visible = true
        emptyMessageView.visible = false
    }

    private fun setListItems(characters: List<Character>) {
        if (characters.isEmpty()) {
            displayMessage(R.string.fragment_list_empty_message, R.drawable.ic_empty_message)
        } else {
            adapter.submitList(characters)
            cleanMessage()
        }
    }
}
