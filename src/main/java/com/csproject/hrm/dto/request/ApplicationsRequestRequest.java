package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import static com.csproject.hrm.common.constant.Constants.ALPHANUMERIC_VALIDATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationsRequestRequest {
  @Positive(message = "applicationRequestId must be a positive number!")
  private Long applicationRequestId;

  @NotBlank(message = "employeeId must not be blank!")
  private String employeeId;

  @Positive(message = "requestNameId must be a positive number!")
  private Long requestNameId;

  @Positive(message = "requestStatusId must be a positive number!")
  private Long requestStatusId;

  @NotBlank(message = "fullName must not be blank!")
  @Pattern(
      regexp = ALPHANUMERIC_VALIDATION,
      flags = Pattern.Flag.UNICODE_CASE,
      message = "fullName must only alphanumeric!")
  private String fullName;

  private String description;

  @NotBlank(message = "approver must not be blank!")
  private String approver;

  private Boolean isBookmark;
}