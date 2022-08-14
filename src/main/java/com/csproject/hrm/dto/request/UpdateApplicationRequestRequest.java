package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateApplicationRequestRequest {
  @Positive(message = "Application Request Id must be a positive number!")
  private Long applicationRequestId;

  @NotBlank(message = "Request Status must not be blank!")
  private String requestStatus;

  @NotBlank(message = "Approver Id must not be blank!")
  private String approverId;
}
