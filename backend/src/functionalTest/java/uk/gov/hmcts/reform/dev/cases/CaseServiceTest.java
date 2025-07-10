package uk.gov.hmcts.reform.dev.cases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.dev.cases.api.NewCaseDTO;
import uk.gov.hmcts.reform.dev.cases.model.CaseDescription;
import uk.gov.hmcts.reform.dev.cases.model.CaseEntity;
import uk.gov.hmcts.reform.dev.cases.model.CaseStatus;
import uk.gov.hmcts.reform.dev.cases.model.CaseStatus.Status;
import uk.gov.hmcts.reform.dev.cases.model.CaseTitle;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CaseServiceTest {

  @Autowired
  private CaseService caseService;

  @BeforeEach
  public void setUp() {
    caseService.deleteAll();
  }

  @Test
  void saveCaseAndReturnCase() throws DuplicateCaseNumberException {
    // given
    CaseEntity savedCase = saveCase("ABC1");
    // then
    assertThat(savedCase).isNotNull();
    assertThat(savedCase.getId()).isGreaterThan(0);
    assertThat(savedCase.getCaseNumber()).isEqualTo("ABC1");
  }

  @Test
  void saveCasesWithSameCaseNumber() {
    // given
    try {
      CaseEntity savedCase = saveCase("ABC1");
      DuplicateCaseNumberException ex = assertThrows(
          DuplicateCaseNumberException.class,
          () -> saveCase("ABC1"),
          "Expected divide(10, 0) to throw, but it didn't"
      );

      assertTrue(ex.getMessage().startsWith("could not execute statement"));
    } catch (DuplicateCaseNumberException e) {
    }
  }

  @Test
  void findCases() throws DuplicateCaseNumberException {
    // given
    CaseEntity savedCase1 = saveCase("ABC1");
    CaseEntity savedCase2 = saveCase("ABC2");
    // when
    Optional<CaseEntity> foundCase1Wrapped = caseService.getByID(savedCase1.getId());
    Optional<CaseEntity> foundCase2Wrapped = caseService.getByID(savedCase2.getId());
    // then
    CaseEntity foundCase1 = foundCase1Wrapped.get();
    CaseEntity foundCase2 = foundCase2Wrapped.get();
    assertThat(foundCase1).isNotNull();
    assertThat(foundCase1.getId()).isGreaterThan(0);
    assertThat(foundCase1.getCaseNumber()).isEqualTo("ABC1");
    assertThat(foundCase2).isNotNull();
    assertThat(foundCase2.getId()).isGreaterThan(0);
    assertThat(foundCase2.getCaseNumber()).isEqualTo("ABC2");
  }

  @Test
  void findAllCases() throws DuplicateCaseNumberException {
    // given
    CaseEntity savedCase1 = saveCase("ABC1");
    CaseEntity savedCase2 = saveCase("ABC2");
    List<CaseEntity> casesCreated = List.of(savedCase1, savedCase2);
    // when
    List<CaseEntity> foundCases = caseService.getAll();
    // then
    assertThat(casesCreated).isEqualTo(foundCases);
  }

  @Test
  void updateCaseStatus() throws CaseNotFoundException, DuplicateCaseNumberException {
    // given
    CaseEntity savedCase1 = saveCase("ABC1");
    CaseStatus newCaseStatus = new CaseStatus(Status.DONE);
    // when
    CaseEntity updatedCase1 = caseService.updateStatus(savedCase1.getId(), newCaseStatus);
    Optional<CaseEntity> foundCase1Wrapped = caseService.getByID(updatedCase1.getId());
    // then
    CaseEntity foundCase1 = foundCase1Wrapped.get();
    assertThat(foundCase1).isNotNull();
    assertThat(foundCase1.getId()).isGreaterThan(0);
    assertThat(foundCase1.getCaseNumber()).isEqualTo("ABC1");
    assertThat(foundCase1.getStatus()).isEqualTo("DONE");
  }

  @Test
  void updateCaseTitle() throws CaseNotFoundException, DuplicateCaseNumberException {
    // given
    CaseEntity savedCase1 = saveCase("ABC1");
    CaseTitle newCaseTitle = new CaseTitle("new title");
    // when
    CaseEntity updatedCase1 = caseService.updateTitle(savedCase1.getId(), newCaseTitle);
    Optional<CaseEntity> foundCase1Wrapped = caseService.getByID(updatedCase1.getId());
    // then
    CaseEntity foundCase1 = foundCase1Wrapped.get();
    assertThat(foundCase1).isNotNull();
    assertThat(foundCase1.getId()).isGreaterThan(0);
    assertThat(foundCase1.getCaseNumber()).isEqualTo("ABC1");
    assertThat(foundCase1.getTitle()).isEqualTo("new title");
  }

  @Test
  void updateCaseDescription() throws CaseNotFoundException, DuplicateCaseNumberException {
    // given
    CaseEntity savedCase1 = saveCase("ABC1");
    CaseDescription newCaseDescription = new CaseDescription("new description");
    // when
    CaseEntity updatedCase1 = caseService.updateDescription(savedCase1.getId(), newCaseDescription);
    Optional<CaseEntity> foundCase1Wrapped = caseService.getByID(updatedCase1.getId());
    // then
    CaseEntity foundCase1 = foundCase1Wrapped.get();
    assertThat(foundCase1).isNotNull();
    assertThat(foundCase1.getId()).isGreaterThan(0);
    assertThat(foundCase1.getCaseNumber()).isEqualTo("ABC1");
    assertThat(foundCase1.getDescription()).isEqualTo("new description");
  }

  @Test
  void deleteCase() throws DuplicateCaseNumberException {
    // given
    CaseEntity savedCase1 = saveCase("ABC1");
    // when
    caseService.delete(savedCase1.getId());
    Optional<CaseEntity> foundCase1Wrapped = caseService.getByID(savedCase1.getId());
    // then
    assertThat(foundCase1Wrapped.isEmpty()).isEqualTo(true);
  }

  @Test
  void deleteAllCases() throws DuplicateCaseNumberException {
    // given
    CaseEntity savedCase1 = saveCase("ABC1");
    CaseEntity savedCase2 = saveCase("ABC2");
    // when
    caseService.deleteAll();
    Optional<CaseEntity> foundCase1Wrapped = caseService.getByID(savedCase1.getId());
    Optional<CaseEntity> foundCase2Wrapped = caseService.getByID(savedCase2.getId());
    // then
    assertThat(foundCase1Wrapped.isEmpty()).isEqualTo(true);
    assertThat(foundCase2Wrapped.isEmpty()).isEqualTo(true);
  }

  private CaseEntity saveCase(String caseNumber) throws DuplicateCaseNumberException {
    NewCaseDTO caseToCreate = new NewCaseDTO(-1, caseNumber, "case title", "case description",
        "TODO", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.of(2025, 12, 31, 14, 38));
    return caseService.create(caseToCreate);
  }
}
