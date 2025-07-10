package uk.gov.hmcts.reform.dev.cases.model;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CaseStatus implements Serializable {

  public enum Status {
    TODO,
    IN_PROGRESS,
    DONE
  }

  @NotNull(message = "status must be provided")
  private Status status;
}
