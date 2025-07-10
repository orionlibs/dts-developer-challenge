package uk.gov.hmcts.reform.dev.cases.api;

import java.io.Serializable;

/**
 * Data Transfer Object used as response bean in the case API holding the data of a case
 *
 * @param message The error message to return
 */
public record APIMessageDTO(String message) implements Serializable {

}
