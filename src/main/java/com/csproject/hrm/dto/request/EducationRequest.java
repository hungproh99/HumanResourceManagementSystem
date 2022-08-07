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
public class EducationRequest {

  @Positive(message = "Education \"id\" must be a positive number!")
  private Long id;

  @NotBlank(message = "nameSchool must not be blank!")
  private String nameSchool;

  @Past(message = "startDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @NotBlank(message = "certificate must not be blank!")
  private String certificate;

  @NotBlank(message = "status must not be blank!")
  private String status;

  @NotBlank(message = "employeeId must not be blank!")
  private String employeeId;
}