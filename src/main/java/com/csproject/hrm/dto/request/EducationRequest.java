package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EducationRequest {

  private Long id;
  private String nameSchool;
  private LocalDate startDate;
  private LocalDate endDate;
  private String certificate;
  private String status;
  private String employeeId;
}