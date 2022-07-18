package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.BonusSalaryResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BonusSalaryRepositoryCustom {
  void insertBonusSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long bonusType, BigDecimal bonus);

  List<BonusSalaryResponse> getListBonusMonthlyBySalaryMonthlyId(Long salaryId);

  BigDecimal sumListBonusMonthlyBySalaryMonthlyId(Long salaryId);
}