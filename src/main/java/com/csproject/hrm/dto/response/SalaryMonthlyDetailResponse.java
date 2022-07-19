package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryMonthlyDetailResponse {
  private Long salary_monthly_id;
  private String employee_id;
  private String full_name;
  private String position;
  private LocalDate start_date;
  private LocalDate end_date;
  private BigDecimal base_salary;
  private BigDecimal final_salary;
  private Double standardPoint;
  private PointResponse pointResponses;
  private OTResponseList otResponseList;
  private BonusSalaryResponseList bonusSalaryResponseList;
  private DeductionSalaryResponseList deductionSalaryResponseList;
  private AdvanceSalaryResponseList advanceSalaryResponseList;
  private EmployeeTaxResponseList employeeTaxResponseList;
  private EmployeeInsuranceResponseList employeeInsuranceResponseList;
}
