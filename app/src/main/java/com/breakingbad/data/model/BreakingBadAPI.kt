package com.breakingbad.data.model

import com.breakingbad.data.CHARACTERS
import retrofit2.http.GET

interface BreakingBadAPI {

    @GET(CHARACTERS)
    suspend fun getAllCharacters(): List<Character>
}
