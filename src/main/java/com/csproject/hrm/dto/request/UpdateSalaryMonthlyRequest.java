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
public class UpdateSalaryMonthlyRequest {
  @Positive(message = "salaryMonthlyId must be a positive number!")
  private Long salaryMonthlyId;

  @NotBlank(message = "salaryStatus must not be blank!")
  private String salaryStatus;

  @NotBlank(message = "approverId must not be blank!")
  private String approverId;
}
