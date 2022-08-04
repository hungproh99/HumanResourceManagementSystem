package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import static com.csproject.hrm.common.constant.Constants.PHONE_VALIDATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HrmRequest {
  @NotBlank(message = "fullName must not be blank")
  private String fullName;

  private Long role;

  @NotBlank(message = "phone must not be blank")
  @Pattern(regexp = PHONE_VALIDATION, message = "phone is not valid")
  private String phone;

  @NotBlank(message = "gender must not be blank")
  private String gender;

  @NotBlank(message = "birthDate must not be blank")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @NotBlank(message = "role must not be blank")
  private Long grade;

  @NotBlank(message = "role must not be blank")
  private Long position;

  @NotBlank(message = "role must not be blank")
  private Long office;

  @NotBlank(message = "role must not be blank")
  private Long area;

  @NotBlank(message = "role must not be blank")
  private Long workingType;

  @NotBlank(message = "role must not be blank")
  private String managerId;

  @NotBlank(message = "role must not be blank")
  private Long employeeType;

  @NotBlank(message = "role must not be blank")
  private String personalEmail;

  @NotBlank(message = "role must not be blank")
  private LocalDate startDate;

  @NotBlank(message = "role must not be blank")
  private LocalDate endDate;
}
