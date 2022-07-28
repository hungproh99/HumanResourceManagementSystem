package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RejectSalaryMonthlyRequest {
  private Long salaryMonthlyId;
  private String comment;
}
