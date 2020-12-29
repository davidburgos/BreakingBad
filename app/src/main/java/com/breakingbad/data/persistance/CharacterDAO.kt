package com.breakingbad.data.persistance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.breakingbad.data.model.Character

@Dao
interface CharacterDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(worldsStats: List<Character>)

    @Query("SELECT * FROM Character WHERE id = :characterId")
    fun loadCharacter(characterId: Int): LiveData<Character?>

    @Query("SELECT * FROM Character order by isFavorite desc")
    fun getAllCharacters(): LiveData<List<Character>>

    @Query("SELECT count(*) FROM Character")
    fun charactersCount(): Int

    @Query("UPDATE Character SET isFavorite = :isFavorite WHERE id = :characterId")
    suspend fun setCharacterAsFavorite(characterId: Int, isFavorite: Boolean = true): Int
}