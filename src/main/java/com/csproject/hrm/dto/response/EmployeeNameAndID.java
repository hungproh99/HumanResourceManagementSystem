package com.csproject.hrm.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeNameAndID {
  private String employeeID;
  private String name;
}