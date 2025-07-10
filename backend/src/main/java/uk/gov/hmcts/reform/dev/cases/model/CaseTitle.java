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
public class CaseTitle implements Serializable {

  @NotNull(message = "title must be provided")
  private String title;
}
