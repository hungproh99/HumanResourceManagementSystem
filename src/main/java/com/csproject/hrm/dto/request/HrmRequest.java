package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

import static com.csproject.hrm.common.constant.Constants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HrmRequest {
  @NotBlank(message = "fullName must not be blank!")
  @Pattern(regexp = ALPHANUMERIC_VALIDATION, message = "fullName must only alphanumeric!")
  private String fullName;

  @Positive(message = "role must be a positive number!")
  private Long role;

  @NotBlank(message = "phone must not be blank!")
  @Pattern(regexp = PHONE_VALIDATION, message = "phone is not valid!")
  private String phone;

  @NotBlank(message = "gender must not be blank!")
  @Pattern(regexp = ALPHANUMERIC_VALIDATION, message = "gender must only alphanumeric!")
  private String gender;

  @NotBlank(message = "birthDate must not be blank!")
  @Past(message = "birthDate must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @Positive(message = "grade must be a positive number!")
  private Long grade;

  @Positive(message = "position must be a positive number!")
  private Long position;

  @Positive(message = "office must be a positive number!")
  private Long office;

  @Positive(message = "area must be a positive number!")
  private Long area;

  @Positive(message = "workingType must be a positive number!")
  private Long workingType;

  @NotBlank(message = "managerId must not be blank!")
  private String managerId;

  @Positive(message = "employeeType must be a positive number!")
  private Long employeeType;

  @NotBlank(message = "personalEmail must not be blank!")
  @Pattern(regexp = EMAIL_VALIDATION, message = "personalEmail is not valid!")
  private String personalEmail;

  @NotBlank(message = "startDate must not be blank!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @NotBlank(message = "endDate must not be blank!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;
}
