package com.csproject.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationResponse {
  private Long education_id;
  private String name_school;
  private LocalDate start_date;
  private LocalDate end_date;
  private String certificate;
  private String status;
}