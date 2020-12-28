package com.breakingbad.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("char_id")
    val id: Int,
    val name: String,
    val birthday: String,
    val occupation: List<String>,
    val img: String,
    val status: String,
    val nickname: String,
    val appearance: List<Int>,
    val portrayed: String,
    val category: String,
    val better_call_saul_appearance: List<Int>,
    var isFavorite: Boolean = false
) {
    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Character, newItem: Character) = oldItem == newItem
        }
    }
}