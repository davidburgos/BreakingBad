package com.breakingbad.data.networking

import com.breakingbad.common.handleError
import javax.inject.Inject

class ApiSource @Inject constructor(private val apiService: BreakingBadAPI){

    /**
     * Gets all characters from Breaking Bad API.
     *
     * @return character list.
     * */
    suspend fun getCharacters() = handleError {
        apiService.getAllCharacters()
    }
}
