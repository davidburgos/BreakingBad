package com.breakingbad.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Character(
    @SerializedName("char_id")
    @PrimaryKey val id: Int = 0,
    val name: String? = "",
    val occupation: List<String>? = listOf(),
    val img: String? = "",
    val status: String? = "",
    val nickname: String? = "",
    val portrayed: String? = "",
    var isFavorite: Boolean = false
): Parcelable {
    companion object {
        val diffCallback = object: DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Character, newItem: Character) = oldItem == newItem
        }
    }
}