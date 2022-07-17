package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryContractDto {
  private Long salary_contract_id;
  private BigDecimal base_salary;
  private BigDecimal additional_salary;
  private String working_type;
}
