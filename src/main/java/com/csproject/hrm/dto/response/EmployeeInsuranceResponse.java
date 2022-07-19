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
public class EmployeeInsuranceResponse {
  private Long employee_insurance_id;
  private BigDecimal value;
  private String policy_type;
  private String insurance_name;
  private Double insurance_value;
}
