package uk.gov.hmcts.reform.dev.cases;

public class CaseNotFoundException extends Exception {

  public CaseNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
