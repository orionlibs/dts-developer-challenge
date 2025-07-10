package uk.gov.hmcts.reform.dev;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SmokeTest {

  @Value("${TEST_URL:http://localhost:4000/api/v1/home}")
  private String testUrl;
  @Autowired
  private TestRestTemplate restTemplate;


  @BeforeEach
  public void setUp() {
    RestAssured.baseURI = testUrl;
    RestAssured.useRelaxedHTTPSValidation();
  }


  @Test
  void springContextLoading_success() {
    // if there is any problem, then this test will fail
  }


  @Test
  void smokeTest() {
    Response response = given()
        .contentType(ContentType.JSON)
        .when()
        .get()
        .then()
        .extract().response();
    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertTrue(response.asString().startsWith("Welcome"));
  }


  @Test
  void whenGetHealth_thenStatusUp() {
    ResponseEntity<Map> response = restTemplate.getForEntity(
        "/health", Map.class
    );
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Map<?, ?> body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.get("status")).isEqualTo("UP");
  }
}
