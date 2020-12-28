package com.breakingbad.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.breakingbad.R
import com.breakingbad.data.model.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterListAdapter: ListAdapter<Character, CharacterListAdapter.ViewHolder>(Character.diffCallback) {

    companion object {
        const val IMAGE_WIDTH = 100
        const val IMAGE_HEIGHT = 150
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: Character, itemView: View) {
            itemView.characterName.text = item.name
            itemView.characterNickname.text = item.nickname

            if (item.img.isNotEmpty()) {
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
