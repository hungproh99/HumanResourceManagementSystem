package com.csproject.hrm.dto.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingPlaceDto {
  private Long working_place_id;
  private Boolean working_place_status;
  private Long area_id;
  private Long grade_id;
  private Long job_id;
  private Long office_id;
  private Long working_contract_id;
  private LocalDate start_date;
}