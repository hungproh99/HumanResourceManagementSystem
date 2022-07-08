package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface AdvanceSalaryRepositoryCustom {
  void insertAdvanceSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, BigDecimal bonus);
}
