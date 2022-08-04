package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimekeepingIdOvertimeTypeDto {
  private Long timekeepingId;
  private LocalDate currDate;
  private Long otType;
}
