package com.breakingbad.di

import android.content.Context
import androidx.room.Room
import com.breakingbad.data.persistance.CharacterDAO
import com.breakingbad.data.persistance.CharacterDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun providesCharacterDatabase(@ApplicationContext context: Context): CharacterDataBase =
        Room.databaseBuilder(
            context.applicationContext,
            CharacterDataBase::class.java,
            "breaking_bad_database.sqlite3"
        ).build()

    @Provides
    fun providesCharacterDAO(dataBase: CharacterDataBase): CharacterDAO = dataBase.getCharacterDAO()
}
