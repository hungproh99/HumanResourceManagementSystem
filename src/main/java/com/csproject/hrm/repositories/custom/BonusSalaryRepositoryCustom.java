package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface BonusSalaryRepositoryCustom {
  void insertBonusSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long bonusType, BigDecimal bonus);
}