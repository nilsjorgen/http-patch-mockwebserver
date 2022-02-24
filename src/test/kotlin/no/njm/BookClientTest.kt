package no.njm

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class BookClientTest {

    companion object {

        var mockWebServer: MockWebServer = MockWebServer()
            .also { it.start() }
            .also {
                System.setProperty("base.url", "http://localhost:${it.port}")
            }
    }

    @Autowired
    private lateinit var bookClient: BookClient

    @Test
    fun `fetching all books should return all three books`() {
        val mockResult = objectMapper.readValue(File("data/books.json"), Array<Book>::class.java)

        val mockResponse = MockResponse()
            .setBody(mockResult.toJson())
            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        mockWebServer.enqueue(mockResponse)

        val books = bookClient.getBooks()

        books.size shouldBe 3

        val request = mockWebServer.takeRequest()
        request.path `should be equal to` "/books"
        request.method `should be equal to` "GET"
    }

    @Test
    fun `updating a book title should return the updated book`() {
        val mockResult = Book(3, "Småfolk", "Thorbjørn Egner")

        val mockResponse = MockResponse()
            .setBody(mockResult.toJson())
            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        mockWebServer.enqueue(mockResponse)

        val updatedBook = bookClient.updateBookTitle(1, "Updated")

        updatedBook?.title `should be equal to` "Småfolk"

        val request = mockWebServer.takeRequest()
        request.path `should be equal to` "/books/1"
        request.method `should be equal to` "PATCH"
    }
}
