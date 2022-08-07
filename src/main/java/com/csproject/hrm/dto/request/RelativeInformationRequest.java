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
public class RelativeInformationRequest {
  @Positive(message = "Relative \"id\" must be a positive number!")
  private Long id;

  @NotBlank(message = "parentName must not be blank!")
  private String parentName;

  @Past(message = "birthDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @Positive(message = "relativeTypeId must be a positive number!")
  private Long relativeTypeId;

  @NotBlank(message = "status must not be blank!")
  private String status;

  @NotBlank(message = "contact must not be blank!")
  private String contact;

  @NotBlank(message = "employeeId must not be blank!")
  private String employeeId;
}