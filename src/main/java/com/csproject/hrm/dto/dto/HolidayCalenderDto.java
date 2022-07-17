package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayCalenderDto {
  private Long holiday_calender_id;
  private String holiday_name;
  private LocalDate start_date;
  private LocalDate end_date;
}
