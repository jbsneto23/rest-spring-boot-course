package br.com.jbsneto.integrationtests.controller.cors.withjson;

import br.com.jbsneto.config.TestConfig;
import br.com.jbsneto.integrationtests.AbstractIntegrationTest;
import br.com.jbsneto.integrationtests.dto.AccountCredentialsDTO;
import br.com.jbsneto.integrationtests.dto.PersonDTO;
import br.com.jbsneto.integrationtests.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerCorsJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDTO person;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    @Order(0)
    void authorization() throws JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
        var accessToken = given()
                .basePath("/auth/signin")
                    .port(TestConfig.SERVER_PORT)
                    .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(user)
                    .when()
                .post()
                    .then()
                        .statusCode(200)
                        .extract()
                        .body()
                            .as(TokenDTO.class)
                                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman", createdPerson.getLastName());
        assertEquals("New York", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    void testCreateWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST2)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO persistedPerson = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("Stallman", persistedPerson.getLastName());
        assertEquals("New York", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    void testFindByIdWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST2)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York");
        person.setGender("Male");
    }

}
