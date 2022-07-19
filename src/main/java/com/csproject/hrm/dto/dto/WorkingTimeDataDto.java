package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeDataDto {
  private LocalTime startTime;
  private LocalTime endTime;
  private List<RangePolicy> listRange;
}
