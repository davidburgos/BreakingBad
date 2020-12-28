package com.breakingbad.common

import androidx.annotation.StringRes

/**
 * A generic class that holds a value with its result status.
 * @param <T>
 */
sealed class Result<out T> {

    data class Success<out T>(var success: Boolean, val data: T) : Result<T>()
    data class Error(val exception: Throwable, @StringRes val messageIdRes: Int) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
