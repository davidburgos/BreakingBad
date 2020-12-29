package com.breakingbad.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.breakingbad.R
import com.breakingbad.common.createViewModelFactory
import com.breakingbad.data.model.Character
import com.breakingbad.ui.list.CharacterListAdapter
import com.breakingbad.ui.list.CharacterListViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A Detail [Fragment] to display the character information.
 */
class CharacterDetailFragment : Fragment(R.layout.fragment_detail) {

    private val args: CharacterDetailFragmentArgs by navArgs()
    private var character: Character? = null

    private val viewModel by navGraphViewModels<CharacterListViewModel>(R.id.nav_graph) {
        createViewModelFactory { CharacterListViewModel(requireActivity().application) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initListeners()
    }

    private fun initListeners() {
        viewModel.loading().observe(viewLifecycleOwner) { setLoading(it) }
        characterDetailFavorite.setOnClickListener {
            character?.let {
                it.isFavorite = !(it.isFavorite)
                viewModel.setCharacterAsFavorite(it)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.show()
        } else {
            progressBar.hide()
        }
    }

    private fun loadData() {
        if (args.characterId >= 0) {
            viewModel.getCharacterById(args.characterId).observe(viewLifecycleOwner) {
                character = it
                refreshUI()
            }
        }
    }

    private fun refreshUI() {
        character?.apply {
            characterDetailNickName.text = nickname
            characterDetailOccupation.text = occupation.joinToString()
            characterDetailStatus.text = status
            characterDetailPortrayed.text = portrayed

            if (isFavorite) {
                characterDetailFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                characterDetailFavorite.setImageResource(R.drawable.ic_favorite_border)
            }

            if (img.isNotEmpty()) {
                Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.ic_default_user_image)
                    .error(R.drawable.ic_something_went_wrong)
                    .resize(CharacterListAdapter.IMAGE_WIDTH, CharacterListAdapter.IMAGE_HEIGHT)
                    .centerCrop()
                    .into(characterDetailImage)
            }
        }
    }
}
