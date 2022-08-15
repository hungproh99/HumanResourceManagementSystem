package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WorkingHistoryRequest {
  @Positive(message = "Working History \"ID\" must be a positive number!")
  private Long id;

  @NotBlank(message = "Employee ID must not be blank!")
  private String employeeId;

  @NotBlank(message = "Company Name must not be blank!")
  private String companyName;

  @NotBlank(message = "Position must not be blank!")
  private String position;

  @Past(message = "Start Date must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @Past(message = "End Date must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;
}