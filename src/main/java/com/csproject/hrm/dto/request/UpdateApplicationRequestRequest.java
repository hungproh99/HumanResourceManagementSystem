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
  @Positive(message = "applicationRequestId must be a positive number!")
  private Long applicationRequestId;

  @NotBlank(message = "requestStatus must not be blank!")
  private String requestStatus;

  @NotBlank(message = "approverId must not be blank!")
  private String approverId;
}
