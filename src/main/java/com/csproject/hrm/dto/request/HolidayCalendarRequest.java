package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayCalendarRequest {
  private String holiday_name;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate start_date;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate end_date;
}
