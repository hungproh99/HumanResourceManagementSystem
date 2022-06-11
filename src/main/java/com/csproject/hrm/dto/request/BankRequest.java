package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BankRequest {
  private Long id;
  private String nameBank;
  private String address;
  private String accountNumber;
  private String accountName;
  private String employeeId;
}