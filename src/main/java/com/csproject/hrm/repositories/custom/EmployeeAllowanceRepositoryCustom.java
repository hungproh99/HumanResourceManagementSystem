package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.EmployeeAllowanceResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeAllowanceRepositoryCustom {
  List<EmployeeAllowanceResponse> getListAllowanceByEmployeeId(String employeeId);
}
