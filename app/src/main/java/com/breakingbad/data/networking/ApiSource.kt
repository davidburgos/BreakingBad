package com.breakingbad.data.networking

import com.breakingbad.common.handleError
import javax.inject.Inject

class ApiSource @Inject constructor(private val apiService: BreakingBadAPI){

    /**
     * Gets all characters from Breaking Bad API.
     *
     * @param limit  the amount of characters to receive.
     * @param offset the position where the requested list will start.
     *
     * @return character list.
     * */
    suspend fun getCharacters(limit: Int, offset: Int) = handleError {
        apiService.getAllCharacters(limit, offset)
    }
}
