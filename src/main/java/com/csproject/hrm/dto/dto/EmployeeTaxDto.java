package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTaxDto {
  private Long employeeTaxID;
  private Long taxTypeID;
  private String taxTypeName;
  private String employeeID;
  private Boolean taxStatus;
}