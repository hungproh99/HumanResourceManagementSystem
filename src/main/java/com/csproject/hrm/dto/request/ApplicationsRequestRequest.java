package com.csproject.hrm.dto.request;

import lombok.*;

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