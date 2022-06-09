package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateHrmRequest {
  private String fullName;
  private String emailCompany;
  private boolean workingStatus;
  private String phone;
  private String gender;
  private LocalDate birthDate;
  private long areaId;
  private long jobId;
  private long officeId;
  private LocalDate startDate;
  private long workingType;
}
