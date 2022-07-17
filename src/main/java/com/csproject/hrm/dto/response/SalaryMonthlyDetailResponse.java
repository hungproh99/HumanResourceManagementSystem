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
public class SalaryMonthlyDetailResponse {
  private Long salary_id;
  private String employee_id;
  private String full_name;
  private String position;
  private Date start_date;
  private Date end_date;
  private BigDecimal base_salary;
  private BigDecimal final_salary;
  private PointResponseList pointResponsesList;
  private OTResponseList otResponseList;
  private BonusSalaryResponseList bonusSalaryResponseList;
  private DeductionSalaryResponseList deductionSalaryResponseList;
  private AdvanceSalaryResponseList advanceSalaryResponseList;
  private EmployeeTaxResponseList employeeTaxResponseList;
  private EmployeeInsuranceResponseList employeeInsuranceResponseList;
}
