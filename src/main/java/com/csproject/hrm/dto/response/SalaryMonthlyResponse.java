package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryMonthlyResponse {
  private Long salary_id;
  private Date start_date;
  private Date end_date;
  private BigDecimal base_salary;
  private BigDecimal final_salary;
  private List<BonusSalaryResponse> bonusSalaryResponses;
}
