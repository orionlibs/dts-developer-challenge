package uk.gov.hmcts.reform.dev.cases.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.dev.cases.api.CaseDTO;
import uk.gov.hmcts.reform.dev.cases.model.CaseEntity;

@Component
public class CaseEntityToDTOConverter implements Converter<Optional<CaseEntity>, CaseDTO> {

  @Override
  public CaseDTO convert(Optional<CaseEntity> caseToConvertWrapper) {
    if (caseToConvertWrapper.isEmpty()) {
      return null;
    }
    CaseEntity caseToConvert = caseToConvertWrapper.get();
    return new CaseDTO(caseToConvert.getId(), caseToConvert.getCaseNumber(),
        caseToConvert.getTitle(), caseToConvert.getDescription(), caseToConvert.getStatus(),
        caseToConvert.getCreatedAt(), caseToConvert.getUpdatedAt(), caseToConvert.getDueAt(), null);
  }

  public CaseDTO convert(CaseEntity caseToConvert) {
    if (caseToConvert == null) {
      return null;
    }
    return new CaseDTO(caseToConvert.getId(), caseToConvert.getCaseNumber(),
        caseToConvert.getTitle(), caseToConvert.getDescription(), caseToConvert.getStatus(),
        caseToConvert.getCreatedAt(), caseToConvert.getUpdatedAt(), caseToConvert.getDueAt(), null);
  }

  public List<CaseDTO> convert(List<CaseEntity> casesToConvert) {
    List<CaseDTO> convertedEntities = new ArrayList<>();
    if (casesToConvert != null && !casesToConvert.isEmpty()) {
      casesToConvert.forEach(c -> convertedEntities.add(convert(Optional.of(c))));
    }
    return convertedEntities;
  }
}
