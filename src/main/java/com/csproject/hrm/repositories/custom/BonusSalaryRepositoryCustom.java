package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface BonusSalaryRepositoryCustom {
  void insertBonusSalaryByEmployeeId(
      String employeeId, LocalDate date, String description, BigDecimal bonus);
}
