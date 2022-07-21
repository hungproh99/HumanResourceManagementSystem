package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OTDetailResponse {
  private Long overtime_id;
  private LocalDate date;
  private LocalTime start_time;
  private LocalTime end_time;
  private Double otPoint;
}
