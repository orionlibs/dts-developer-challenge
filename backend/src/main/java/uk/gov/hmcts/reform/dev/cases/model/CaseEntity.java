package uk.gov.hmcts.reform.dev.cases.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cases", schema = "cases", indexes = {
    @Index(
        name = "idx_id",
        columnList = "id"
    )
})
public class CaseEntity implements Comparable<CaseEntity> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "case_number", length = 50, nullable = false, unique = true)
  private String caseNumber;
  @Column(name = "title", length = 700, nullable = false)
  private String title;
  @Lob
  @Column(name = "description", columnDefinition = "TEXT", nullable = false)
  private String description;
  @Column(name = "status", length = 50, nullable = false)
  private String status;
  @Column(name = "created_date_time", nullable = false)
  private LocalDateTime createdDateTime;
  @Column(name = "updated_date_time", nullable = false)
  private LocalDateTime updatedDateTime;
  @Column(name = "due_date_time", nullable = false)
  private LocalDateTime dueDateTime;

  public CaseEntity() {
  }

  public CaseEntity(String caseNumber, String title, String description,
      String status, LocalDateTime createdDateTime,
      LocalDateTime updatedDateTime, LocalDateTime dueDateTime) {
    this.caseNumber = caseNumber;
    this.title = title;
    this.description = description;
    this.status = status;
    this.createdDateTime = createdDateTime;
    this.updatedDateTime = updatedDateTime;
    this.dueDateTime = dueDateTime;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCaseNumber() {
    return caseNumber;
  }

  public void setCaseNumber(String caseNumber) {
    this.caseNumber = caseNumber;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(LocalDateTime createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public LocalDateTime getUpdatedDateTime() {
    return updatedDateTime;
  }

  public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
    this.updatedDateTime = updatedDateTime;
  }

  public LocalDateTime getDueDateTime() {
    return dueDateTime;
  }

  public void setDueDateTime(LocalDateTime dueDateTime) {
    this.dueDateTime = dueDateTime;
  }

  /**
   * Natural ordering by id then caseNumber.
   */
  @Override
  public int compareTo(CaseEntity other) {
    if (this == other) {
      return 0;
    }

    int idCompare = this.getId().compareTo(other.getId());
    if (idCompare != 0) {
      return idCompare;
    }

    return this.getCaseNumber().compareTo(other.getCaseNumber());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaseEntity that = (CaseEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(caseNumber, that.caseNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, caseNumber);
  }
}
