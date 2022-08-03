package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WorkingContractRepositoryCustom {
  BigDecimal getBaseSalaryByEmployeeID(String employeeID);
}