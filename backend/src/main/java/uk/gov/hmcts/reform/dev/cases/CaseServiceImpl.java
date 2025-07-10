package uk.gov.hmcts.reform.dev.cases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.dev.cases.api.NewCaseDTO;
import uk.gov.hmcts.reform.dev.cases.converter.NewCaseToCaseConverter;
import uk.gov.hmcts.reform.dev.cases.model.CaseDescription;
import uk.gov.hmcts.reform.dev.cases.model.CaseEntity;
import uk.gov.hmcts.reform.dev.cases.model.CaseRepository;
import uk.gov.hmcts.reform.dev.cases.model.CaseStatus;
import uk.gov.hmcts.reform.dev.cases.model.CaseTitle;

/**
 * Provider of functionality related to cases
 */
@Service
@Slf4j
public class CaseServiceImpl implements CaseService {

  @Autowired
  private CaseRepository caseRepository;
  @Autowired
  private NewCaseToCaseConverter newCaseToCaseConverter;

  @Override
  public Optional<CaseEntity> getByID(Integer caseID) {
    return caseRepository.findById(caseID);
  }

  @Override
  public Optional<CaseEntity> getByCaseNumber(String caseNumber) {
    return caseRepository.findByCaseNumber(caseNumber);
  }

  @Override
  public List<CaseEntity> getAll() {
    return caseRepository.findAll();
  }

  @Transactional(rollbackFor = DuplicateCaseNumberException.class)
  @Override
  public CaseEntity create(NewCaseDTO caseToCreate) throws DuplicateCaseNumberException {
    CaseEntity caseToSave = newCaseToCaseConverter.convert(caseToCreate);
    try {
      caseToSave = caseRepository.save(caseToSave);
      log.info("Created new case");
      return caseToSave;
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateCaseNumberException(e.getMessage());
    }
  }

  @Transactional
  @Override
  public CaseEntity updateStatus(Integer caseID, CaseStatus status) throws CaseNotFoundException {
    Optional<CaseEntity> caseToUpdateWrapped = getByID(caseID);
    if (caseToUpdateWrapped.isPresent()) {
      CaseEntity caseToUpdate = caseToUpdateWrapped.get();
      caseToUpdate.setStatus(status.getStatus().name());
      caseToUpdate.setUpdatedAt(LocalDateTime.now());
      caseRepository.save(caseToUpdate);
      log.info("Updated case status");
      return caseToUpdate;
    } else {
      throw new CaseNotFoundException(String.format("Case with ID %s not found", caseID));
    }
  }

  @Transactional
  @Override
  public CaseEntity updateTitle(Integer caseID, CaseTitle title) throws CaseNotFoundException {
    Optional<CaseEntity> caseToUpdateWrapped = getByID(caseID);
    if (caseToUpdateWrapped.isPresent()) {
      CaseEntity caseToUpdate = caseToUpdateWrapped.get();
      caseToUpdate.setTitle(title.getTitle());
      caseToUpdate.setUpdatedAt(LocalDateTime.now());
      caseRepository.save(caseToUpdate);
      log.info("Updated case title");
      return caseToUpdate;
    } else {
      throw new CaseNotFoundException(String.format("Case with ID %s not found", caseID));
    }
  }

  @Transactional
  @Override
  public CaseEntity updateDescription(Integer caseID, CaseDescription description)
      throws CaseNotFoundException {
    Optional<CaseEntity> caseToUpdateWrapped = getByID(caseID);
    if (caseToUpdateWrapped.isPresent()) {
      CaseEntity caseToUpdate = caseToUpdateWrapped.get();
      caseToUpdate.setDescription(description.getDescription());
      caseToUpdate.setUpdatedAt(LocalDateTime.now());
      caseRepository.save(caseToUpdate);
      log.info("Updated case description");
      return caseToUpdate;
    } else {
      throw new CaseNotFoundException(String.format("Case with ID %s not found", caseID));
    }
  }

  @Override
  public void delete(Integer caseID) {
    caseRepository.deleteById(caseID);
    log.info("Deleted case");
  }

  @Override
  public void deleteAll() {
    caseRepository.deleteAll();
    log.info("Deleted all cases");
  }
}
