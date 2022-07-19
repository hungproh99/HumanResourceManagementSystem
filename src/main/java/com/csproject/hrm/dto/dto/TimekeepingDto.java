package com.csproject.hrm.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimekeepingDto {
  private Double pointWorkDay;
  private Double pointOTDay;
  private LocalDate date;
  private String employeeId;
}
