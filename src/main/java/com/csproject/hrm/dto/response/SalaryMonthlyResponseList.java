package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryMonthlyResponseList {
  private List<SalaryMonthlyResponse> salaryMonthlyResponses;
  private int total;
}
