package com.breakingbad.data.networking

import com.breakingbad.data.CHARACTERS
import com.breakingbad.data.model.Character
import retrofit2.http.GET

interface BreakingBadAPI {

    @GET(CHARACTERS)
    suspend fun getAllCharacters(): List<Character>
}
