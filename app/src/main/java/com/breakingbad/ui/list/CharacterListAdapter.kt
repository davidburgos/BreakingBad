package com.breakingbad.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.breakingbad.R
import com.breakingbad.data.model.Character
import com.breakingbad.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class CharacterListAdapter(private val onFavoriteClicked: ((item: Character) -> Unit)?,
                           private val onCharacterClicked: ((item: Character) -> Unit)?):
    PagingDataAdapter<Character, CharacterListAdapter.ViewHolder>(Character.diffCallback) {

    companion object {
        const val IMAGE_WIDTH = 100
        const val IMAGE_HEIGHT = 150
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, holder.item)
        }
    }

    inner class ViewHolder(val item: ItemCharacterBinding): RecyclerView.ViewHolder(item.root) {

        private fun markCharacterAsFavorite(isFavorite: Boolean) {
            if (isFavorite) {
                item.characterFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                item.characterFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        fun bind(item: Character, itemView: ItemCharacterBinding) {
            itemView.characterName.text = item.name
            itemView.characterNickname.text = item.nickname

            markCharacterAsFavorite(item.isFavorite)

            itemView.root.setOnClickListener{ onCharacterClicked?.invoke(item) }
            itemView.characterFavorite.setOnClickListener {
                item.isFavorite = !item.isFavorite
                markCharacterAsFavorite(item.isFavorite)
                onFavoriteClicked?.invoke(item)
            }

            if (item.img?.isNotEmpty() == true) {
                Picasso.get()
                    .load(item.img)
                    .placeholder(R.drawable.ic_default_user_image)
                    .error(R.drawable.ic_something_went_wrong)
                    .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                    .centerCrop()
                    .into(itemView.characterImage)
            }
        }
    }
}
