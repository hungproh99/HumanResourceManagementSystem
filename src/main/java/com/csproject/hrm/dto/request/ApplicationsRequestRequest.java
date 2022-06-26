package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationsRequestRequest {
  private Long applicationRequestId;
  private String employeeId;
  private Long requestTypeId;
  private Long requestNameId;
  private Long requestStatusId;
  private String fullName;
  private String description;
  private String approver;
  private Boolean isBookmark;
}