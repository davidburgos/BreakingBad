package com.breakingbad.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(private val apiSource: ApiSource) {

    suspend fun getCharacters() = withContext(Dispatchers.IO) {
        apiSource.getCharacters()
    }
}
