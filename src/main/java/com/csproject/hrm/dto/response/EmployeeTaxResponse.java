package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeTaxResponse {
  private Long employee_tax_id;
  private BigDecimal value;
  private Long policy_type_id;
  private String tax_name;
  private String tax_value;
}
