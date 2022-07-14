package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeductionSalaryResponse {
  private Long deduction_id;
  private BigDecimal value;
  private String deduction_name;
  private Date date;
  private String description;
}
