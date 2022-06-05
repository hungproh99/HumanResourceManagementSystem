package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrmRequest {
    private String fullName;
    private String role;
    private String phone;
    private String gender;
    private LocalDate birthDate;
    private String job;
    private String office;
    private String area;
    private BigDecimal baseSalary;
    private String contractName;
}
