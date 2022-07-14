package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeInsuranceResponseList {
  private List<EmployeeInsuranceResponse> employeeInsuranceResponseList;
  private BigDecimal total;
}
