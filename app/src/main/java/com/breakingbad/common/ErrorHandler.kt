package com.breakingbad.common

import com.breakingbad.R
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> handleError(codeBlock: () -> T): Result<T> =
    try {
        Result.Success(true, codeBlock())
    } catch (e: Exception) {
        e.printStackTrace()
        val message: Int = when (e) {
            is SocketTimeoutException, is InterruptedIOException -> R.string.error_message_time_out
            is AccessDeniedException -> R.string.error_message_not_found
            is UnknownHostException -> R.string.error_message_no_internet_connection
            else -> R.string.error_message_default
        }
        Result.Error(e, message)
    }