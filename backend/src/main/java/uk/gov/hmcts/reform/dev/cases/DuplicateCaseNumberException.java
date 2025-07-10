package uk.gov.hmcts.reform.dev.cases;

public class DuplicateCaseNumberException extends Exception {

  public DuplicateCaseNumberException(String errorMessage) {
    super(errorMessage);
  }
}
