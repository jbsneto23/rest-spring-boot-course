package br.com.jbsneto.integrationtests.controller.withxml;

import br.com.jbsneto.config.TestConfig;
import br.com.jbsneto.integrationtests.AbstractIntegrationTest;
import br.com.jbsneto.integrationtests.dto.AccountCredentialsDTO;
import br.com.jbsneto.integrationtests.dto.PersonDTO;
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

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static PersonDTO person;

    private static int sizeBeforeDelete;

    @BeforeAll
    public static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
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
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
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
        assertNotNull(createdPerson.getEnabled());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman", createdPerson.getLastName());
        assertEquals("New York", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void testFindById() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
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
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Richard", persistedPerson.getFirstName());
        assertEquals("Stallman", persistedPerson.getLastName());
        assertEquals("New York", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());
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

        var people = Arrays.asList(objectMapper.readValue(content, PersonDTO[].class));

        sizeBeforeDelete = people.size();

        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertTrue(people.contains(person));

        PersonDTO personInList = people.get(people.indexOf(person));

        assertNotNull(personInList);
        assertNotNull(personInList.getId());
        assertNotNull(personInList.getFirstName());
        assertNotNull(personInList.getLastName());
        assertNotNull(personInList.getAddress());
        assertNotNull(personInList.getGender());
        assertNotNull(personInList.getEnabled());

        assertTrue(personInList.getId() == person.getId());

        assertEquals("Richard", personInList.getFirstName());
        assertEquals("Stallman", personInList.getLastName());
        assertEquals("New York", personInList.getAddress());
        assertEquals("Male", personInList.getGender());
        assertTrue(personInList.getEnabled());
    }

    @Test
    @Order(4)
    void testUpdate() throws JsonProcessingException {
        updateMockPerson();
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO updatedPerson = objectMapper.readValue(content, PersonDTO.class);
        var expectedId = person.getId();
        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());
        assertNotNull(updatedPerson.getEnabled());

        assertTrue(updatedPerson.getId() == expectedId);

        assertEquals("Richard 2", updatedPerson.getFirstName());
        assertEquals("Stallman 2", updatedPerson.getLastName());
        assertEquals("New York 2", updatedPerson.getAddress());
        assertEquals("Male 2", updatedPerson.getGender());
        assertTrue(updatedPerson.getEnabled());
    }

    @Test
    @Order(5)
    void testDisablePerson() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .accept(TestConfig.CONTENT_TYPE_XML)
                .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
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
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Richard 2", persistedPerson.getFirstName());
        assertEquals("Stallman 2", persistedPerson.getLastName());
        assertEquals("New York 2", persistedPerson.getAddress());
        assertEquals("Male 2", persistedPerson.getGender());
        assertFalse(persistedPerson.getEnabled());
    }

    @Test
    @Order(6)
    void testDelete() throws JsonProcessingException {
        given()
            .spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_XML)
            .accept(TestConfig.CONTENT_TYPE_XML)
            .header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_TEST)
            .pathParam("id", person.getId())
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }

    @Test
    @Order(7)
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

        var people = Arrays.asList(objectMapper.readValue(content, PersonDTO[].class));

        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertFalse(people.contains(person));
        assertTrue(people.size() == sizeBeforeDelete - 1);
    }

    @Test
    @Order(8)
    void testAllWithoutToken() throws JsonProcessingException {
        var specificationWithoutAuthorization = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
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

    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York");
        person.setGender("Male");
        person.setEnabled(true);
    }

    private void updateMockPerson() {
        person.setFirstName("Richard 2");
        person.setLastName("Stallman 2");
        person.setAddress("New York 2");
        person.setGender("Male 2");
    }

}
