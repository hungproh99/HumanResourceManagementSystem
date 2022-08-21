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
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.csproject.hrm.common.constant.Constants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HrmRequest {
  @NotBlank(message = "Full Name must not be blank!")
  @Pattern(
      regexp = ALPHANUMERIC_VALIDATION,
      flags = Pattern.Flag.UNICODE_CASE,
      message = "Full Name must only alphanumeric!")
  private String fullName;

  @Positive(message = "Role must be a positive number!")
  private Long role;

  @NotBlank(message = "Phone must not be blank!")
  @Pattern(regexp = PHONE_VALIDATION, message = "Phone is not valid!")
  private String phone;

  @NotBlank(message = "Gender must not be blank!")
  @Pattern(
      regexp = ALPHANUMERIC_VALIDATION,
      flags = Pattern.Flag.UNICODE_CASE,
      message = "Gender must only alphanumeric!")
  private String gender;

  @Past(message = "Birth Date must less than today!")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @Positive(message = "Grade must be a positive number!")
  private Long grade;

  @Positive(message = "Position must be a positive number!")
  private Long position;

  @Positive(message = "Office must be a positive number!")
  private Long office;

  @Positive(message = "Area must be a positive number!")
  private Long area;

  @Positive(message = "Working Type must be a positive number!")
  private Long workingType;

  @NotBlank(message = "Manager Id must not be blank!")
  private String managerId;

  @Positive(message = "Employee Type must be a positive number!")
  private Long employeeType;

  @NotBlank(message = "Personal Email must not be blank!")
  @Pattern(regexp = EMAIL_VALIDATION, message = "Personal Email is not valid!")
  private String personalEmail;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  private BigDecimal baseSalary;

  private BigDecimal salary;
}
