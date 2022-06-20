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
public class HrmPojo extends HrmRequest {
  private String employeeId;
  private String companyEmail;
  private String password;
  private boolean workStatus;
  private String companyName;
}
