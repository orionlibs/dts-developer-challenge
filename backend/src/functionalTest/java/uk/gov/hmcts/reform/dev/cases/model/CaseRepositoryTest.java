package uk.gov.hmcts.reform.dev.cases.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CaseRepositoryTest {

  @Autowired
  private CaseRepository caseRepository;

  @Test
  void saveCaseAndReturnCase() {
    // given
    CaseEntity case1 = saveCase("ABC12345");
    // when
    CaseEntity savedCase = caseRepository.save(case1);
    // then
    assertThat(savedCase).isNotNull();
    assertThat(savedCase.getId()).isGreaterThan(0);
    assertThat(savedCase.getCaseNumber()).isEqualTo("ABC12345");
  }

  @Test
  void findCases() {
    // given
    CaseEntity case1 = saveCase("ABC1");
    CaseEntity case2 = saveCase("ABC2");
    CaseEntity savedCase1 = caseRepository.save(case1);
    CaseEntity savedCase2 = caseRepository.save(case2);
    // when
    Optional<CaseEntity> foundCase1Wrapped = caseRepository.findById(savedCase1.getId());
    Optional<CaseEntity> foundCase2Wrapped = caseRepository.findById(savedCase2.getId());
    Optional<CaseEntity> foundCase3Wrapped = caseRepository.findById(64);
    // then
    CaseEntity foundCase1 = foundCase1Wrapped.get();
    CaseEntity foundCase2 = foundCase2Wrapped.get();
    assertThat(foundCase1).isNotNull();
    assertThat(foundCase1.getId()).isGreaterThan(0);
    assertThat(foundCase1.getCaseNumber()).isEqualTo("ABC1");
    assertThat(foundCase2).isNotNull();
    assertThat(foundCase2.getId()).isGreaterThan(0);
    assertThat(foundCase2.getCaseNumber()).isEqualTo("ABC2");
    assertTrue(foundCase3Wrapped.isEmpty());
  }

  private CaseEntity saveCase(String caseNumber) {
    CaseEntity case1 = new CaseEntity(caseNumber, "Case Title", "Case Description", "Case Status",
        LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
    return caseRepository.save(case1);
  }
}
