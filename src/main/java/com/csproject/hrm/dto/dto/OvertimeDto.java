package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OvertimeDto {
  private LocalTime start_time;
  private LocalTime end_time;
  private String overtime_type;
}
