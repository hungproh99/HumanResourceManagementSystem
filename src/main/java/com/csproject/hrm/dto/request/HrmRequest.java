package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HrmRequest {
  private String fullName;
  private String role;
  private String phone;
  private String gender;
  private LocalDate birthDate;
  private String grade;
  private String position;
  private String office;
  private String area;
  private String workingType;
  private String managerId;
  private String employeeType;
  private String personalEmail;
}
