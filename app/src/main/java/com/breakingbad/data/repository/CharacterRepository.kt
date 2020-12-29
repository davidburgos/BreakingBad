package com.breakingbad.data.repository

import androidx.lifecycle.LiveData
import com.breakingbad.common.Result
import com.breakingbad.data.model.Character
import com.breakingbad.data.networking.ApiSource
import com.breakingbad.data.persistance.CharacterDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val apiSource: ApiSource,
    private val characterDAO: CharacterDAO
) {

    /**
     * Get the full list of characters saved on database.
     *
     * @return LiveData<List<Character>> observable list with characters.
     * */
    suspend fun getCharacters(): LiveData<List<Character>> {
        return withContext(Dispatchers.IO) {
            if (characterDAO.charactersCount() == 0) {
                refreshCharacters()
            }
            characterDAO.getAllCharacters()
        }
    }

    /**
     * Mark the selected character as favorite.
     *
     * @param characterId Id for the selected character.
     * @param isFavorite whether the character is favorite or not.
     *
     * @return if character was saved on database or not.
     * */
    suspend fun setCharacterAsFavorite(characterId: Int, isFavorite: Boolean = true): Boolean {
        return withContext(Dispatchers.IO){
            characterDAO.setCharacterAsFavorite(characterId, isFavorite) == 1
        }
    }

    /**
     * Get a single character for a given Id.
     *
     * @param characterId id for the desired character.
     *
     * @return Observable character object. can be null.
     * */
    suspend fun getCharacterById(characterId: Int): LiveData<Character?> {
        return withContext(Dispatchers.IO) {
            characterDAO.loadCharacter(characterId)
        }
    }

    private suspend fun refreshCharacters() {
        when (val result = apiSource.getCharacters()) {
            is Result.Success -> characterDAO.insertAll(result.data)
            is Result.Error -> {}
        }
    }
}
