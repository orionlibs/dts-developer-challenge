package uk.gov.hmcts.reform.dev.cases.api;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

  @GetMapping(ControllerUtils.baseAPIPath + "/home")
  public ResponseEntity<String> welcome() {
    return ok("Welcome to test-backend");
  }
}
