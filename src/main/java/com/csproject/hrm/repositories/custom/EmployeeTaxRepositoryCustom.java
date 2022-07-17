package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.EmployeeTaxResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeTaxRepositoryCustom {
  List<EmployeeTaxResponse> getListTaxByEmployeeId(String employeeId);
}
