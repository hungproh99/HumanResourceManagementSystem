package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.EmployeeInsuranceResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeInsuranceRepositoryCustom {
    List<EmployeeInsuranceResponse> getListInsuranceByEmployeeId(String employeeId);
}
