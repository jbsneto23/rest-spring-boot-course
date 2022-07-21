package br.com.jbsneto.integrationtests.controller.withxml;

import br.com.jbsneto.config.TestConfig;
import br.com.jbsneto.integrationtests.AbstractIntegrationTest;
import br.com.jbsneto.integrationtests.dto.AccountCredentialsDTO;
import br.com.jbsneto.integrationtests.dto.BookDTO;
import br.com.jbsneto.integrationtests.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static BookDTO book;
    private static int sizeBeforeDelete;

    @BeforeAll
    public static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        book = new BookDTO();
        sizeBeforeDelete = 0;
    }

    @Test
    @Order(0)
    void authorization() throws JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
        var content = given()
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        TokenDTO tokenDTO = objectMapper.readValue(content, TokenDTO.class);

        var accessToken = tokenDTO.getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/book/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockBook();
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);

        book = createdBook;

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getLaunchDate());
        assertNotNull(createdBook.getPrice());

        assertTrue(createdBook.getId() > 0);

        assertEquals("J. K. Rowling", createdBook.getAuthor());
        assertEquals("Harry Potter e a Pedra Filosofal", createdBook.getTitle());
        assertEquals(Date.from(LocalDate.parse("2001-11-23").atStartOfDay(ZoneId.systemDefault()).toInstant()), createdBook.getLaunchDate());
        assertEquals(29.99D, createdBook.getPrice());
    }

    @Test
    @Order(2)
    void testFindById() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO persistedBook = objectMapper.readValue(content, BookDTO.class);

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());

        assertTrue(persistedBook.getId() == book.getId());

        assertEquals("J. K. Rowling", persistedBook.getAuthor());
        assertEquals("Harry Potter e a Pedra Filosofal", persistedBook.getTitle());
        assertEquals(Date.from(LocalDate.parse("2001-11-23").atStartOfDay(ZoneId.systemDefault()).toInstant()), persistedBook.getLaunchDate());
        assertEquals(29.99D, persistedBook.getPrice());
    }

    @Test
    @Order(3)
    void testAll() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var books = Arrays.asList(objectMapper.readValue(content, BookDTO[].class));

        sizeBeforeDelete = books.size();

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertTrue(books.contains(book));

        BookDTO bookInList = books.get(books.indexOf(book));

        assertNotNull(bookInList);
        assertNotNull(bookInList.getId());
        assertNotNull(bookInList.getAuthor());
        assertNotNull(bookInList.getTitle());
        assertNotNull(bookInList.getLaunchDate());
        assertNotNull(bookInList.getPrice());

        assertTrue(bookInList.getId() == book.getId());

        assertEquals("J. K. Rowling", bookInList.getAuthor());
        assertEquals("Harry Potter e a Pedra Filosofal", bookInList.getTitle());
        assertEquals(Date.from(LocalDate.parse("2001-11-23").atStartOfDay(ZoneId.systemDefault()).toInstant()), bookInList.getLaunchDate());
        assertEquals(29.99D, bookInList.getPrice());
    }

    @Test
    @Order(4)
    void testUpdate() throws JsonProcessingException {
        updateMockBook();
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .body(book)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookDTO updatedBook = objectMapper.readValue(content, BookDTO.class);
        var expectedId = book.getId();
        book = updatedBook;

        assertNotNull(updatedBook);
        assertNotNull(updatedBook.getId());
        assertNotNull(updatedBook.getAuthor());
        assertNotNull(updatedBook.getTitle());
        assertNotNull(updatedBook.getLaunchDate());
        assertNotNull(updatedBook.getPrice());

        assertTrue(updatedBook.getId() == expectedId);

        assertEquals("Joanne \"Jo\" Rowling", updatedBook.getAuthor());
        assertEquals("Harry Potter e a Pedra Filosofal", updatedBook.getTitle());
        assertEquals(Date.from(LocalDate.parse("2001-11-23").atStartOfDay(ZoneId.systemDefault()).toInstant()), updatedBook.getLaunchDate());
        assertEquals(19.99D, updatedBook.getPrice());
    }

    @Test
    @Order(5)
    void testDelete() throws JsonProcessingException {
        given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void testAllAfterDelete() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var books = Arrays.asList(objectMapper.readValue(content, BookDTO[].class));

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertFalse(books.contains(book));
        assertTrue(books.size() == sizeBeforeDelete - 1);
    }

    @Test
    @Order(7)
    void testAllWithoutToken() throws JsonProcessingException {
        var specificationWithoutAuthorization = new RequestSpecBuilder()
                .setBasePath("/api/book/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given()
                .spec(specificationWithoutAuthorization)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockBook() {
        book.setAuthor("J. K. Rowling");
        book.setTitle("Harry Potter e a Pedra Filosofal");
        book.setLaunchDate(Date.from(LocalDate.parse("2001-11-23").atStartOfDay(ZoneId.systemDefault()).toInstant()));
        book.setPrice(29.99D);
    }

    private void updateMockBook() {
        book.setAuthor("Joanne \"Jo\" Rowling");
        book.setPrice(19.99D);
    }
}
