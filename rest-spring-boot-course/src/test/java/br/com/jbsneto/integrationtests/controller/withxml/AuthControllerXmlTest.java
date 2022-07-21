package br.com.jbsneto.integrationtests.controller.withxml;

import br.com.jbsneto.config.TestConfig;
import br.com.jbsneto.integrationtests.AbstractIntegrationTest;
import br.com.jbsneto.integrationtests.dto.AccountCredentialsDTO;
import br.com.jbsneto.integrationtests.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    void testSignin() throws JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
        tokenDTO = given()
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDTO);
        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresf() throws JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
        var newTokenDTO = given()
                .basePath("/auth/refresh")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_XML)
                .pathParam("username", tokenDTO.getUsername())
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(newTokenDTO);
        assertNotNull(newTokenDTO.getAccessToken());
        assertNotNull(newTokenDTO.getRefreshToken());
    }

}