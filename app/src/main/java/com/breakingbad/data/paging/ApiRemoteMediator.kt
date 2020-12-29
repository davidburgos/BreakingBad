package com.breakingbad.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.breakingbad.common.Result
import com.breakingbad.data.model.Character
import com.breakingbad.data.networking.ApiSource
import com.breakingbad.data.persistance.BreakingBadDataBase
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ApiRemoteMediator @Inject constructor(
    private val service: ApiSource,
    private val breakingBadDataBase: BreakingBadDataBase
) : RemoteMediator<Int, Character>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        var limit = state.config.pageSize
        val offset = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                limit = state.config.initialLoadSize

                if (remoteKeys == null && hasDataBaseItems()) {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                remoteKeys?.nextKey ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey ?: 0
            }
        }

        try {
            var characters: List<Character> = emptyList()
            var endOfPaginationReached = false

            when (val apiResponse = service.getCharacters(limit, offset)) {
                is Result.Success -> {
                    characters = apiResponse.data
                    endOfPaginationReached = characters.isEmpty() || characters.size != limit
                }
                is Result.Error -> {
                    return MediatorResult.Error(apiResponse.exception)
                }
            }

            breakingBadDataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    breakingBadDataBase.getRemoteKeysDao().clearRemoteKeys()
                    breakingBadDataBase.getCharacterDAO().clearCharacters()
                }
                val prevKey = if (offset == 0) null else (offset - limit)
                val nextKey = if (endOfPaginationReached) null else (offset + limit)
                val keys = characters.map {
                    RemoteKeys(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                breakingBadDataBase.getRemoteKeysDao().insertAll(keys)
                breakingBadDataBase.getCharacterDAO().insertAll(characters)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Character>): RemoteKeys? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                breakingBadDataBase.getRemoteKeysDao().remoteKeysCharacterId(it.id)
            }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Character>): RemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                breakingBadDataBase.getRemoteKeysDao().remoteKeysCharacterId(it.id)
            }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Character>): RemoteKeys? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                breakingBadDataBase.getRemoteKeysDao().remoteKeysCharacterId(id)
            }
        }

    private suspend fun hasDataBaseItems(): Boolean {
        return breakingBadDataBase.getCharacterDAO().charactersCount() > 0
    }
}
