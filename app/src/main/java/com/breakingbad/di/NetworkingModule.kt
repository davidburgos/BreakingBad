package com.breakingbad.di

import com.breakingbad.data.BASE_URL
import com.breakingbad.data.networking.BreakingBadAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ActivityComponent::class)
object NetworkingModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = (HttpLoggingInterceptor.Level.BASIC) }

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideBreakingBadApiService(
        okHttpClient: OkHttpClient,
        gsonConverter: GsonConverterFactory
    ): BreakingBadAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverter)
        .client(okHttpClient)
        .build()
        .create(BreakingBadAPI::class.java)
}
