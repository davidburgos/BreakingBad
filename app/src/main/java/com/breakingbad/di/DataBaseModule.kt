package com.breakingbad.di

import android.content.Context
import androidx.room.Room
import com.breakingbad.data.persistance.CharacterDAO
import com.breakingbad.data.persistance.BreakingBadDataBase
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
    fun providesCharacterDatabase(@ApplicationContext context: Context): BreakingBadDataBase =
        Room.databaseBuilder(
            context.applicationContext,
            BreakingBadDataBase::class.java,
            "breaking_bad_database.sqlite3"
        ).build()

    @Provides
    fun providesCharacterDAO(dataBase: BreakingBadDataBase): CharacterDAO = dataBase.getCharacterDAO()
}
