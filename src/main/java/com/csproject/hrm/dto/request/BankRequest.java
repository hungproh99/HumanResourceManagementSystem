package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

import static com.csproject.hrm.common.constant.Constants.NUMERIC_VALIDATION;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BankRequest {
  @Positive(message = "Bank \"id\" must be a positive number!")
  private Long id;

  @NotBlank(message = "nameBank must not be blank!")
  private String nameBank;

  @NotBlank(message = "address must not be blank!")
  private String address;

  @NotBlank(message = "accountNumber must not be blank!")
  @Pattern(regexp = NUMERIC_VALIDATION, message = "accountNumber accept numeric only!")
  private String accountNumber;

  @NotBlank(message = "accountName must not be blank!")
  private String accountName;

  @NotBlank(message = "employeeId must not be blank!")
  private String employeeId;
}