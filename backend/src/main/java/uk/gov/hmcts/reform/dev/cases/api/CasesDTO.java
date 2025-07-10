package uk.gov.hmcts.reform.dev.cases.api;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object used as response bean in the case API holding the data of multiple cases
 * @param cases The list of cases found
 */
public record CasesDTO(List<CaseDTO> cases, String message) implements Serializable {

}
