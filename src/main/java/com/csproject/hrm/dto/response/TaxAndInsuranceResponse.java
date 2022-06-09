package com.csproject.hrm.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxAndInsuranceResponse {
  private Long tax_code;
  private Long insurance_id;
  private String insurance_name;
  private String address;
}