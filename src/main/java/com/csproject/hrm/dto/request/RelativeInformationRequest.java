package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RelativeInformationRequest {
  private Long id;
  private String parentName;
  private LocalDate birthDate;
  private Long relativeTypeId;
  private String status;
  private String contact;
  private String employeeId;
}