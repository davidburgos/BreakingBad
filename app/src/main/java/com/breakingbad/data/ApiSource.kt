package com.breakingbad.data

import com.breakingbad.common.handleError
import com.breakingbad.data.model.BreakingBadAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiSource {

    private val interceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(BreakingBadAPI::class.java)
    }

    /**
     * Gets all characters
     *
     * @return character list.
     * */
    suspend fun getCharacters() = handleError {
        apiService.getAllCharacters()
    }
}
