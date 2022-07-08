package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface SalaryContractRepositoryCustom {
  void insertNewSalaryContract(
      String employeeId,
      BigDecimal newSalary,
      LocalDate startDate,
      boolean oldStatus,
      boolean newStatus);
}
