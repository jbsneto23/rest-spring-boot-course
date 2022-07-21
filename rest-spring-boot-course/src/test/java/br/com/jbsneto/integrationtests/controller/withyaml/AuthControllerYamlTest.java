package br.com.jbsneto.integrationtests.controller.withyaml;

import br.com.jbsneto.config.TestConfig;
import br.com.jbsneto.integrationtests.AbstractIntegrationTest;
import br.com.jbsneto.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.jbsneto.integrationtests.dto.AccountCredentialsDTO;
import br.com.jbsneto.integrationtests.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;
    private static YMLMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        objectMapper = new YMLMapper();
    }

    @Test
    @Order(1)
    void testSignin() throws JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
        tokenDTO = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfig.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestConfig.CONTENT_TYPE_YAML)
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YAML)
                .body(user, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, objectMapper);

        assertNotNull(tokenDTO);
        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresf() throws JsonProcessingException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
        var newTokenDTO = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfig.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestConfig.CONTENT_TYPE_YAML)
                .basePath("/auth/refresh")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YAML)
                .pathParam("username", tokenDTO.getUsername())
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, objectMapper);

        assertNotNull(newTokenDTO);
        assertNotNull(newTokenDTO.getAccessToken());
        assertNotNull(newTokenDTO.getRefreshToken());
    }

}