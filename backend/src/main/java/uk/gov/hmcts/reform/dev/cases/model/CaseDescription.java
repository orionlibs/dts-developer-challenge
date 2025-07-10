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
public class CaseDescription implements Serializable {

  @NotNull(message = "description must be provided")
  private String description;
}
