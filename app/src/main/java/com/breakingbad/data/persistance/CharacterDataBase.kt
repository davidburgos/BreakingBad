package com.breakingbad.data.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.breakingbad.data.model.Character

@Database(entities = [Character::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class CharacterDataBase : RoomDatabase() {
    abstract fun getCharacterDAO(): CharacterDAO
}
