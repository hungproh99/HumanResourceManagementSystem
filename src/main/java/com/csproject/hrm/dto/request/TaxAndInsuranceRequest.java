package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaxAndInsuranceRequest {
  private String taxCode;
  private String insuranceId;
  private String employeeId;
  private String insuranceAddress;
  private String policyNameId;
}