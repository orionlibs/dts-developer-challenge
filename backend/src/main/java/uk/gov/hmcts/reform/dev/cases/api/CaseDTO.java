package uk.gov.hmcts.reform.dev.cases.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object used as response bean in the case API holding the data of a case
 *
 * @param id          The caseID
 * @param caseNumber  The case number
 * @param title       the case title
 * @param description The case description
 * @param status      The case status
 * @param createdAt   The datetime the case was created
 * @param updatedAt   The datetime the case was last updated
 * @param dueAt       The datetime the case is due
 */
public record CaseDTO(int id, @JsonProperty("case_number") String caseNumber, String title,
                      String description, String status,
                      @JsonProperty("created_at") LocalDateTime createdAt,
                      @JsonProperty("updated_at") LocalDateTime updatedAt,
                      @JsonProperty("due_at") LocalDateTime dueAt,
                      String message) implements Serializable {

}
