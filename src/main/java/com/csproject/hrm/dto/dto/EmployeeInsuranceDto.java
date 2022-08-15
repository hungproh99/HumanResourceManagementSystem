package com.csproject.hrm.dto.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInsuranceDto {
	private Long insuranceID;
	private String insuranceName;
	private String address;
	private String policyNameID;
}