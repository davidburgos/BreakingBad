package com.breakingbad.data.persistance

import androidx.room.TypeConverter
import com.google.gson.Gson

class RoomConverters {

    @TypeConverter
    fun fromString(string: String?) = string?.let{ Gson().fromJson<List<String>>(string, List::class.java) }

    @TypeConverter
    fun fromInt(string: String?) = string?.let{ Gson().fromJson<List<Int>>(string, List::class.java) }

    @TypeConverter
    fun toString(list:List<String>) = Gson().toJson(list)

    @TypeConverter
    fun intListToString(list:List<Int>) = Gson().toJson(list)
}
