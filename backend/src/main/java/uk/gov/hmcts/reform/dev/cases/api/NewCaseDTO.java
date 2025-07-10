package uk.gov.hmcts.reform.dev.cases.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used as request bean in the case API holding the data of a new case to be
 * registered with the system
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewCaseDTO implements Serializable {

  private int id;
  @JsonProperty("case_number")
  @NotBlank(message = "case_number must not be blank")
  @Schema(description = "Case number", example = "ABC1")
  private String caseNumber;
  @NotBlank(message = "title must not be blank")
  private String title;
  private String description;
  private String status;
  @JsonProperty("created_date_time")
  private LocalDateTime createdDateTime;
  @JsonProperty("updated_date_time")
  private LocalDateTime updatedDateTime;
  @JsonProperty("due_date_time")
  private LocalDateTime dueDateTime;
}
