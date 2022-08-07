package com.csproject.hrm.dto.response;

import com.csproject.hrm.dto.dto.EmployeeInsuranceDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxAndInsuranceResponse {
  private String tax_code;
  private List<EmployeeInsuranceDto> insuranceDtos;
}