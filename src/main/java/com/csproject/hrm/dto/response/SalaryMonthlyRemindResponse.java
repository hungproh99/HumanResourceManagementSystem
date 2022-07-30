package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryMonthlyRemindResponse {
  private Long salaryMonthlyId;
  private String fullName;
  private LocalDate startDate;
  private LocalDate endDate;
  private String employeeId;
  private String position;
  private String approver;
  private List<String> checkedBy;
}
