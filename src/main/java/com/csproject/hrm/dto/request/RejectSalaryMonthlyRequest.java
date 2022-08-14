package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RejectSalaryMonthlyRequest {
  @Positive(message = "Salary Monthly Id must be a positive number!")
  private Long salaryMonthlyId;

  private String comment;
}
