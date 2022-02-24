package no.njm

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class BookClient(
    private val restTemplate: RestTemplate,
    @Value("\${base.url}")
    private val baseUrl: String
) {

    val booksUrl = "$baseUrl/books"

    fun getBooks(): List<Book> {
        val responseEntity = restTemplate.getForEntity(booksUrl, Array<Book>::class.java)

        responseEntity.body?.let {
            return it.toList()
        }
        return emptyList()
    }

    fun updateBookTitle(id: Int, title: String): Book? {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val httpEntity = HttpEntity(Book(title = title), headers)
        val responseEntity = restTemplate.exchange("$booksUrl/$id", HttpMethod.PATCH, httpEntity, Book::class.java)
        return responseEntity.body
    }
}

@JsonInclude(Include.NON_NULL)
data class Book(
    val id: Int? = null,
    val title: String? = null,
    val author: String? = null,
)
