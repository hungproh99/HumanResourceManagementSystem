package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.EmployeeInsuranceResponse;
import com.csproject.hrm.dto.response.EmployeeTaxResponse;
import com.csproject.hrm.entities.EmployeeInsurance;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeInsuranceRepositoryCustom {
    List<EmployeeInsuranceResponse> getListInsuranceByEmployeeId(String employeeId);
}
