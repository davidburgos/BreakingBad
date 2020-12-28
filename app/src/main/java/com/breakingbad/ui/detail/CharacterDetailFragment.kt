package com.breakingbad.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.breakingbad.R
import com.breakingbad.ui.list.CharacterListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A Detail [Fragment] to display the character information.
 */
class CharacterDetailFragment : Fragment(R.layout.fragment_detail) {

    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initListeners()
    }

    private fun initListeners() {
        characterDetailFavorite.setOnClickListener {
            args.item?.let {
                it.isFavorite = !(it.isFavorite)
                if (it.isFavorite) {
                    characterDetailFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    characterDetailFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }
    }

    private fun loadData() {
        args.item?.apply {
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
