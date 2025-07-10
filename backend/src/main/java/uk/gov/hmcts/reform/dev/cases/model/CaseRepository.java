package uk.gov.hmcts.reform.dev.cases.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<CaseEntity, Integer> {

  /**
   * Finds a CaseEntity by its caseNumber column
   *
   * @param caseNumber The case number to look by
   * @return The found case or empty Optional
   */
  Optional<CaseEntity> findByCaseNumber(String caseNumber);
}
