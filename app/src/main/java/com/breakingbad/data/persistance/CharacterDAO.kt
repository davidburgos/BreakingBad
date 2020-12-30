package com.breakingbad.data.persistance

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.breakingbad.data.model.Character

@Dao
interface CharacterDAO {

    @Insert(onConflict = IGNORE)
    suspend fun insertAll(worldsStats: List<Character>)

    @Query("SELECT * FROM Character WHERE id = :characterId")
    fun loadCharacter(characterId: Long): LiveData<Character?>

    @Query("SELECT * FROM Character ORDER BY isFavorite DESC")
    fun getAllCharacters(): PagingSource<Int, Character>

    @Query("DELETE FROM Character")
    suspend fun clearCharacters()

    @Query("SELECT count(*) FROM Character")
    suspend fun charactersCount(): Int

    @Query("UPDATE Character SET isFavorite = :isFavorite WHERE id = :characterId")
    suspend fun setCharacterAsFavorite(characterId: Long, isFavorite: Boolean = true): Int
}
