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
  @Positive(message = "Working History \"id\" must be a positive number!")
  private Long id;

  @NotBlank(message = "employeeId must not be blank!")
  private String employeeId;

  @NotBlank(message = "companyName must not be blank!")
  private String companyName;

  @NotBlank(message = "position must not be blank!")
  private String position;

  @Past(message = "startDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @Past(message = "endDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;
}