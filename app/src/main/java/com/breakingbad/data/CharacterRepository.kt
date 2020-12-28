package com.breakingbad.data

class CharacterRepository(private val apiSource: ApiSource) {

    suspend fun getCharacters() = apiSource.getCharacters()

}
