package uk.gov.hmcts.reform.dev.cases.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object used as response bean in the case API holding the data of a case
 *
 * @param id The caseID
 * @param caseNumber The case number
 * @param title the case title
 * @param description The case description
 * @param status The case status
 * @param createdDateTime The datetime the case was created
 * @param updatedDateTime The datetime the case was last updated
 * @param dueDateTime The datetime the case is due
 */
public record CaseDTO(int id, @JsonProperty("case_number") String caseNumber, String title,
                      String description, String status,
                      @JsonProperty("created_date_time") LocalDateTime createdDateTime,
                      @JsonProperty("updated_date_time") LocalDateTime updatedDateTime,
                      @JsonProperty("due_date_time") LocalDateTime dueDateTime) implements
    Serializable {

}
