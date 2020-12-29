package com.breakingbad.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.breakingbad.R
import com.breakingbad.common.createViewModelFactory
import com.breakingbad.data.model.Character
import com.breakingbad.data.repository.CharacterRepository
import com.breakingbad.databinding.FragmentDetailBinding
import com.breakingbad.ui.list.CharacterListAdapter
import com.breakingbad.ui.list.CharacterViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A Detail [Fragment] to display the character information.
 */
@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_detail) {

    private val args: CharacterDetailFragmentArgs by navArgs()
    private var character: Character? = null
    private lateinit var binding: FragmentDetailBinding

    @Inject
    lateinit var repository: CharacterRepository

    private val viewModel by navGraphViewModels<CharacterViewModel>(R.id.nav_graph) {
        createViewModelFactory { CharacterViewModel(repository) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initListeners()
    }

    private fun initListeners() {
        viewModel.loading().observe(viewLifecycleOwner) { setLoading(it) }
        binding.characterDetailFavorite.setOnClickListener {
            character?.let {
                it.isFavorite = !(it.isFavorite)
                viewModel.setCharacterAsFavorite(it)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.show()
        } else {
            binding.progressBar.hide()
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
            binding.characterDetailNickName.text = nickname
            binding.characterDetailOccupation.text = occupation?.joinToString()
            binding.characterDetailStatus.text = status
            binding.characterDetailPortrayed.text = portrayed

            if (isFavorite) {
                binding.characterDetailFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.characterDetailFavorite.setImageResource(R.drawable.ic_favorite_border)
            }

            if (img?.isNotEmpty() == true) {
                Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.ic_default_user_image)
                    .error(R.drawable.ic_something_went_wrong)
                    .resize(CharacterListAdapter.IMAGE_WIDTH, CharacterListAdapter.IMAGE_HEIGHT)
                    .centerCrop()
                    .into(binding.characterDetailImage)
            }
        }
    }
}
