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
  @Positive(message = "Application Request Id must be a positive number!")
  private Long applicationRequestId;

  @NotBlank(message = "Employee Id must not be blank!")
  private String employeeId;

  @Positive(message = "Request Name Id must be a positive number!")
  private Long requestNameId;

  @Positive(message = "Request Status Id must be a positive number!")
  private Long requestStatusId;

  @NotBlank(message = "Full Name must not be blank!")
  @Pattern(
      regexp = ALPHANUMERIC_VALIDATION,
      flags = Pattern.Flag.UNICODE_CASE,
      message = "Full Name must only alphanumeric!")
  private String fullName;

  private String description;

  @NotBlank(message = "Approver must not be blank!")
  private String approver;

  private Boolean isBookmark;
}