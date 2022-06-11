package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WorkingHistoryRequest {
  private Long id;
  private String employeeId;
  private String companyName;
  private String position;
  private LocalDate startDate;
  private LocalDate endDate;
}