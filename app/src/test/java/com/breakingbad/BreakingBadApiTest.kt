package com.breakingbad

import com.breakingbad.data.model.Character
import com.breakingbad.data.networking.BreakingBadAPI
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader

/**
 * Test for Breaking Bad API.
 */
class BreakingBadApiTest {

    lateinit var mockWebServer : MockWebServer
    lateinit var service: BreakingBadAPI

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockWebServer.url("").toString())
            .build()

        service = retrofit.create(BreakingBadAPI::class.java)
    }

    @Test
    @Throws(IOException::class)
    fun getAllCharacters() = runBlocking {
        val body = readFile("success_response.json")

        mockWebServer.enqueue(MockResponse().setBody(body))

        val result: List<Character> = service.getAllCharacters(10, 0)

        assertEquals(result.count(), 63)
    }

    @Test
    @Throws(IOException::class)
    fun emptyResponse() = runBlocking {
        val body = readFile("empty_response.json")

        mockWebServer.enqueue(MockResponse().setBody(body))

        val result: List<Character> = service.getAllCharacters(10, 0)

        assertEquals(result.count(), 0)
    }

    private fun readFile(path: String): String {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        val content = reader.readText()
        reader.close()
        return content
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}