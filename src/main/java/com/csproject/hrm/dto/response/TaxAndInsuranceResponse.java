package com.csproject.hrm.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxAndInsuranceResponse {
  private Long tax_code;
  private Long employee_insurance_id;
  private String address;
}