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
  private Long role;
  private String phone;
  private String gender;
  private LocalDate birthDate;
  private Long grade;
  private Long position;
  private Long office;
  private Long area;
  private Long workingType;
  private String managerId;
  private Long employeeType;
  private String personalEmail;
}
