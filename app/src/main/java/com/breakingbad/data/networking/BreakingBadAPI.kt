package com.breakingbad.data.networking

import com.breakingbad.data.CHARACTERS
import com.breakingbad.data.model.Character
import retrofit2.http.GET
import retrofit2.http.Query

interface BreakingBadAPI {

    @GET(CHARACTERS)
    suspend fun getAllCharacters(@Query("limit") limit: Int, @Query("offset") offset: Int): List<Character>
}
