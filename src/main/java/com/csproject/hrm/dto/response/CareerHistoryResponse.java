package com.csproject.hrm.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareerHistoryResponse {
  private BigDecimal hiring_salary;
  private LocalDate hiring_date;
  private BigDecimal current_salary;
  private LocalDate current_date;
  private String seniority;
  private Integer career_development;
  private List<SalaryTimeline> salary_timelines;
}