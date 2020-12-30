package com.breakingbad.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.breakingbad.data.model.Character
import com.breakingbad.data.networking.ApiSource
import com.breakingbad.data.paging.ApiRemoteMediator
import com.breakingbad.data.persistance.BreakingBadDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiSource: ApiSource,
    private val breakingBadDataBase: BreakingBadDataBase
) {
    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

    /**
     * Get the full list of characters saved on database, or requested from Api if database is empty.
     *
     * @return flow<PagingData<Character>> observable list with characters, contemplating pagination.
     * */
    suspend fun getCharacters(): Flow<PagingData<Character>> =
        withContext(Dispatchers.IO) {
            Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    enablePlaceholders = false
                ),
                remoteMediator = ApiRemoteMediator(apiSource, breakingBadDataBase),
                pagingSourceFactory = { breakingBadDataBase.getCharacterDAO().getAllCharacters() }
            ).flow
        }

    /**
     * Mark the selected character as favorite.
     *
     * @param characterId Id for the selected character.
     * @param isFavorite whether the character is favorite or not.
     *
     * @return if character was saved on database or not.
     * */
    suspend fun setCharacterAsFavorite(characterId: Long, isFavorite: Boolean = true): Boolean =
        withContext(Dispatchers.IO) {
            breakingBadDataBase.getCharacterDAO().setCharacterAsFavorite(characterId, isFavorite) == 1
        }

    /**
     * Get a single character for a given Id.
     *
     * @param characterId id for the desired character.
     *
     * @return Observable character object. can be null.
     * */
    suspend fun getCharacterById(characterId: Long): LiveData<Character?> =
        withContext(Dispatchers.IO) {
            breakingBadDataBase.getCharacterDAO().loadCharacter(characterId)
        }
}
