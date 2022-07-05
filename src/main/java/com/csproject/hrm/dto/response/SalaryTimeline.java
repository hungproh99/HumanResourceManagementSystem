package com.csproject.hrm.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalaryTimeline {
  private BigDecimal base_salary;
  private LocalDate contract_date;
}