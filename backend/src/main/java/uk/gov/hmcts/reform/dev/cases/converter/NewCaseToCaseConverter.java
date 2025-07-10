package uk.gov.hmcts.reform.dev.cases.converter;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.dev.cases.api.NewCaseDTO;
import uk.gov.hmcts.reform.dev.cases.model.CaseEntity;

@Component
public class NewCaseToCaseConverter implements Converter<NewCaseDTO, CaseEntity> {

  @Override
  public CaseEntity convert(NewCaseDTO caseToConvert) {
    if (caseToConvert == null) {
      return null;
    }
    return new CaseEntity(caseToConvert.getCaseNumber(), caseToConvert.getTitle(),
        caseToConvert.getDescription(), caseToConvert.getStatus(),
        caseToConvert.getCreatedDateTime(), caseToConvert.getUpdatedDateTime(),
        caseToConvert.getDueDateTime());
  }
}
