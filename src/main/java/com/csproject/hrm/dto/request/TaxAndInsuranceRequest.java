package com.csproject.hrm.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaxAndInsuranceRequest {
  private String taxCode;
  private Long insuranceId;
  private String employeeId;
  private String insuranceName;
  private String insuranceAddress;
  private String insurancePercent;
  private String insuranceTitle;
  private String insuranceDescription;
}