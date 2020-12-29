package com.breakingbad.data.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.breakingbad.data.model.Character
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Character::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class CharacterDataBase : RoomDatabase() {
    abstract fun getCharacterDAO(): CharacterDAO

    companion object {
        @Volatile
        private var INSTANCE: CharacterDataBase? = null

        fun getDatabase(context: Context): CharacterDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDataBase::class.java,
                    "breaking_bad_database.sqlite3"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
