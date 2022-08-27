package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface WorkingContractRepositoryCustom {
  BigDecimal getBaseSalaryByEmployeeID(String employeeID);

  void updateStatusWorkingContract(Boolean status, LocalDate dateCheck);
}