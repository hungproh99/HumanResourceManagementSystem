package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSalaryMonthlyRequest {
  private Long salaryMonthlyId;
  private String salaryStatus;
  private String approverId;
}
