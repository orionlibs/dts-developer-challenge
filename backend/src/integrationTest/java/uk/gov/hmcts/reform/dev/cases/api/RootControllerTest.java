package uk.gov.hmcts.reform.dev.cases.api;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RootControllerTest {

  @Value("${TEST_URL:http://localhost:4000/api/v1/home}")
  private String testUrl;


  @BeforeEach
  public void setUp() {
    RestAssured.baseURI = testUrl;
    RestAssured.useRelaxedHTTPSValidation();
  }


  @Test
  void welcomeMessageTest() {
    Response response = given()
        .contentType(ContentType.JSON)
        .when()
        .get()
        .then()
        .extract().response();
    assertEquals(200, response.statusCode());
    assertTrue(response.asString().startsWith("Welcome"));
  }
}
