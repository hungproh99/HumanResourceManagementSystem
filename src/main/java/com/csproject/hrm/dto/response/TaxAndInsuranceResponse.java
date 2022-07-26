package com.csproject.hrm.dto.response;

import com.csproject.hrm.dto.dto.EmployeeInsuranceDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxAndInsuranceResponse {
  private Long tax_code;
  private List<EmployeeInsuranceDto> insuranceDtos;
}