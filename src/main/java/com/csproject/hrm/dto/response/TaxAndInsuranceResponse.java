package com.csproject.hrm.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaxAndInsuranceResponse {
  private Long tax_code;
  private Long employee_insurance_id;
  private String policy_type;
  private Long policy_type_id;
  private Long policy_category_id;
  private String address;
}