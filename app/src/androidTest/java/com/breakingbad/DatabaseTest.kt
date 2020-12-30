package com.breakingbad

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.breakingbad.data.model.Character
import com.breakingbad.data.persistance.BreakingBadDataBase
import com.breakingbad.data.persistance.CharacterDAO
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will test database actions.
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var characterDAO: CharacterDAO
    private lateinit var db: BreakingBadDataBase
    private val charactersList = listOf<Character>(
        Character(id = 1, name = "character1", isFavorite = false),
        Character(id = 2, name = "character2", isFavorite = true),
        Character(id = 3, name = "character3", isFavorite = false),
        Character(id = 4, name = "character4", isFavorite = false),
        Character(id = 5, name = "character5", isFavorite = false)
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BreakingBadDataBase::class.java).build()
        characterDAO = db.getCharacterDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun countCharacters() = runBlocking {
        characterDAO.insertAll(charactersList)

        val characterCount = characterDAO.charactersCount()
        assertThat(characterCount, equalTo(charactersList.count()))
    }

    @Test
    @Throws(Exception::class)
    fun removeCharacters() = runBlocking {
        characterDAO.insertAll(charactersList)
        characterDAO.clearCharacters()

        val characterCount = characterDAO.charactersCount()
        assertThat(characterCount, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun getCharacter() = runBlocking {
        characterDAO.insertAll(charactersList)

        val characterCount = characterDAO.loadCharacter(charactersList.last().id)
        assertNotEquals(characterCount, null)
    }

    @Test
    @Throws(Exception::class)
    fun setCharacterAsFavorite() = runBlocking {
        characterDAO.insertAll(charactersList)

        val result = characterDAO.setCharacterAsFavorite(charactersList.last().id)
        assertEquals(result, 1)
    }

    @Test
    @Throws(Exception::class)
    fun setCharacterAsFavoriteWthWrongId() = runBlocking {
        characterDAO.insertAll(charactersList)

        val result = characterDAO.setCharacterAsFavorite(369)
        assertEquals(result, 0)
    }

}