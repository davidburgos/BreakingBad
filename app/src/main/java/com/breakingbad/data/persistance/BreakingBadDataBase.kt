package com.breakingbad.data.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.breakingbad.data.model.Character
import com.breakingbad.data.paging.RemoteKeys

@Database(entities = [RemoteKeys::class, Character::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class BreakingBadDataBase : RoomDatabase() {
    abstract fun getCharacterDAO(): CharacterDAO
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}
