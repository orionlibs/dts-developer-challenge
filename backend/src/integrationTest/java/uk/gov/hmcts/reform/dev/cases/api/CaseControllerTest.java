package uk.gov.hmcts.reform.dev.cases.api;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.JSONService;
import uk.gov.hmcts.reform.dev.cases.CaseService;
import uk.gov.hmcts.reform.dev.cases.model.CaseDescription;
import uk.gov.hmcts.reform.dev.cases.model.CaseStatus;
import uk.gov.hmcts.reform.dev.cases.model.CaseTitle;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CaseControllerTest {

  @Autowired
  private JSONService jsonService;
  @Autowired
  private CaseService caseService;
  private String basePath = "http://localhost:4000" + ControllerUtils.baseAPIPath + "/cases";


  @BeforeEach
  public void setUp() {
    RestAssured.useRelaxedHTTPSValidation();
    caseService.deleteAll();
  }


  @Test
  void createCase() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    assertEquals(201, response.statusCode());
    assertTrue(response.header("Location").startsWith(ControllerUtils.baseAPIPath + "/cases"));
  }

  @Test
  void getCase() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response1 = makeAPICallToCreateCase(caseToCreate);
    RestAssured.baseURI = "http://localhost:4000" + response1.header("Location");
    Response response2 = given()
        .contentType(ContentType.JSON)
        .when()
        .get()
        .then()
        .extract().response();
    assertEquals(200, response2.statusCode());
    CaseDTO response2Body = response2.as(CaseDTO.class);
    assertEquals("ABC1", response2Body.caseNumber());
  }

  @Test
  void getCase_notFound() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response1 = makeAPICallToCreateCase(caseToCreate);
    RestAssured.baseURI = basePath + "/64";
    Response response2 = given()
        .contentType(ContentType.JSON)
        .when()
        .get()
        .then()
        .extract().response();
    assertEquals(404, response2.statusCode());
  }

  @Test
  void getAllCases() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate1 = createCaseRequest("ABC1");
    Response response1 = makeAPICallToCreateCase(caseToCreate1);
    NewCaseDTO caseToCreate2 = createCaseRequest("ABC2");
    Response response2 = makeAPICallToCreateCase(caseToCreate2);
    RestAssured.baseURI = basePath;
    Response response3 = given()
        .contentType(ContentType.JSON)
        .when()
        .get()
        .then()
        .extract().response();
    assertEquals(200, response3.statusCode());
    CasesDTO response3Body = response3.as(CasesDTO.class);
    assertEquals(2, response3Body.cases().size());
    assertEquals("ABC1", response3Body.cases().get(0).caseNumber());
    assertEquals("ABC2", response3Body.cases().get(1).caseNumber());
  }

  @Test
  void updateCaseStatus() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    String newCaseURI = response.header("Location");
    int newCaseID = Integer.parseInt(newCaseURI.substring(newCaseURI.lastIndexOf("/") + 1));
    RestAssured.baseURI = basePath + "/" + newCaseID + "/statuses";
    CaseStatus newCaseStatus = new CaseStatus(CaseStatus.Status.DONE);
    Response response2 = given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(jsonService.toJson(newCaseStatus))
        .when()
        .patch()
        .then()
        .extract().response();
    assertEquals(204, response2.statusCode());
  }

  @Test
  void updateCaseStatus_caseNotFound() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    RestAssured.baseURI = basePath + "64/statuses";
    CaseStatus newCaseStatus = new CaseStatus(CaseStatus.Status.DONE);
    Response response2 = given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(jsonService.toJson(newCaseStatus))
        .when()
        .patch()
        .then()
        .extract().response();
    assertEquals(404, response2.statusCode());
  }


  @Test
  void updateCaseTitle() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    String newCaseURI = response.header("Location");
    int newCaseID = Integer.parseInt(newCaseURI.substring(newCaseURI.lastIndexOf("/") + 1));
    RestAssured.baseURI = basePath + "/" + newCaseID + "/titles";
    CaseTitle newCaseTitle = new CaseTitle("new title");
    Response response2 = given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(jsonService.toJson(newCaseTitle))
        .when()
        .patch()
        .then()
        .extract().response();
    assertEquals(204, response2.statusCode());
  }

  @Test
  void updateCaseTitle_caseNotFound() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    RestAssured.baseURI = basePath + "64/titles";
    CaseTitle newCaseTitle = new CaseTitle("new title");
    Response response2 = given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(jsonService.toJson(newCaseTitle))
        .when()
        .patch()
        .then()
        .extract().response();
    assertEquals(404, response2.statusCode());
  }


  @Test
  void updateCaseDescription() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    String newCaseURI = response.header("Location");
    int newCaseID = Integer.parseInt(newCaseURI.substring(newCaseURI.lastIndexOf("/") + 1));
    RestAssured.baseURI = basePath + "/" + newCaseID + "/descriptions";
    CaseDescription newCaseDescription = new CaseDescription("new description");
    Response response2 = given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(jsonService.toJson(newCaseDescription))
        .when()
        .patch()
        .then()
        .extract().response();
    assertEquals(204, response2.statusCode());
  }

  @Test
  void updateCaseDescription_caseNotFound() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    RestAssured.baseURI = basePath + "64/statuses";
    CaseDescription newCaseDescription = new CaseDescription("new description");
    Response response2 = given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(jsonService.toJson(newCaseDescription))
        .when()
        .patch()
        .then()
        .extract().response();
    assertEquals(404, response2.statusCode());
  }


  @Test
  void deleteCase() {
    RestAssured.baseURI = basePath;
    NewCaseDTO caseToCreate = createCaseRequest("ABC1");
    Response response = makeAPICallToCreateCase(caseToCreate);
    String newCaseURI = response.header("Location");
    RestAssured.baseURI = "http://localhost:4000" + newCaseURI;
    Response response2 = given()
        .contentType(ContentType.JSON)
        .when()
        .delete()
        .then()
        .extract().response();
    assertEquals(204, response2.statusCode());
    Response response3 = given()
        .contentType(ContentType.JSON)
        .when()
        .get()
        .then()
        .extract().response();
    assertEquals(404, response3.statusCode());
  }

  private NewCaseDTO createCaseRequest(String caseNumber) {
    return new NewCaseDTO(-1, caseNumber, "case title", "case description",
        "TODO", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.of(2025, 12, 31, 14, 38));
  }

  private Response makeAPICallToCreateCase(NewCaseDTO caseToCreate) {
    return given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(jsonService.toJson(caseToCreate))
        .when()
        .post()
        .then()
        .extract().response();
  }
}
