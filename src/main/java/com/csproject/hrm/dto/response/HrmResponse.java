package com.csproject.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrmResponse {
    private String employee_id;
    private String fullName;
    private String email;
    private String workStatus;
    private String phone;
    private String gender;
    private String birthDate;
    private String job;
    private String office;
    private String area;
    private String contract;
    private String seniority;
    private String startDate;
}
