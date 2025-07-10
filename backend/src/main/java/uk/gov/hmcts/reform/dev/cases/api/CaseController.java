package uk.gov.hmcts.reform.dev.cases.api;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.cases.CaseNotFoundException;
import uk.gov.hmcts.reform.dev.cases.CaseService;
import uk.gov.hmcts.reform.dev.cases.DuplicateCaseNumberException;
import uk.gov.hmcts.reform.dev.cases.converter.CaseEntityToDTOConverter;
import uk.gov.hmcts.reform.dev.cases.model.CaseEntity;
import uk.gov.hmcts.reform.dev.cases.model.CaseStatus;
import uk.gov.hmcts.reform.dev.cases.model.CaseStatus.Status;

@RestController
@RequestMapping(value = ControllerUtils.baseAPIPath + "/cases")
@Validated
@Tag(name = "Cases", description = "Manage MoJ cases")
public class CaseController {

  @Autowired
  private CaseService caseService;
  @Autowired
  private CaseEntityToDTOConverter caseConverter;

  @GetMapping(value = "/examples", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CaseDTO> getExampleCase() {
    return ok(new CaseDTO(1, "ABC12345", "Case Title", "Case Description", "Case Status",
        LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()));
  }


  @Operation(
      summary = "Get a case by ID",
      description = "Retrieves a case",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              schema = @Schema(implementation = Integer.class)
          )
      ),
      responses = {
          @ApiResponse(responseCode = "200", description = "Case retrieved"),
          @ApiResponse(responseCode = "404", description = "Case not found")
      }
  )
  @GetMapping(value = "/{caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CaseDTO> getCase(@PathVariable Integer caseID) {
    return caseService.getByID(caseID)
        .map(caseConverter::convert)
        .filter(Objects::nonNull)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }


  @Operation(
      summary = "Get all cases",
      description = "Retrieves all cases",
      responses = {
          @ApiResponse(responseCode = "200", description = "Case retrieved")
      }
  )
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CasesDTO> getAllCases() {
    List<CaseEntity> casesFound = caseService.getAll();
    List<CaseDTO> caseToReturn = caseConverter.convert(casesFound);
    return ok(new CasesDTO(caseToReturn));
  }


  @Operation(
      summary = "Create a case",
      description = "Creates a case with default status TODO",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              schema = @Schema(implementation = NewCaseDTO.class)
          )
      ),
      responses = {
          @ApiResponse(responseCode = "201", description = "Case created"),
          @ApiResponse(responseCode = "400", description = "Duplicate case number found")
      }
  )
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<APIMessageDTO> createCase(@Valid @RequestBody NewCaseDTO caseToCreate) {
    caseToCreate.setCreatedDateTime(LocalDateTime.now());
    caseToCreate.setUpdatedDateTime(LocalDateTime.now());
    caseToCreate.setStatus(Status.TODO.name());

    try {
      CaseEntity newCase = caseService.create(caseToCreate);
      return created(URI.create(ControllerUtils.baseAPIPath + "/cases/" + newCase.getId())).build();
    } catch (DuplicateCaseNumberException e) {
      return ResponseEntity.badRequest().body(new APIMessageDTO("This case number already exists"));
    }

  }


  @Operation(
      summary = "Updates the status of a case",
      description = "Updates the status of a case",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              schema = @Schema(implementation = CaseStatus.class)
          )
      ),
      responses = {
          @ApiResponse(responseCode = "200", description = "Case status updated"),
          @ApiResponse(responseCode = "404", description = "Case not found")
      }
  )
  @PatchMapping(value = "/{caseID}/statuses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateCaseStatus(@PathVariable Integer caseID,
      @Valid @RequestBody CaseStatus status) {
    try {
      caseService.updateStatus(caseID, status);
      return ok(null);
    } catch (CaseNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }


  @Operation(
      summary = "Deletes a case",
      description = "Deletes a case",
      responses = {
          @ApiResponse(responseCode = "200", description = "Case deleted")
      }
  )
  @DeleteMapping(value = "/{caseID}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteCase(@PathVariable Integer caseID) {
    caseService.delete(caseID);
    return ok(null);
  }
}
