package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.BonusSalaryDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyInfoDto;
import com.csproject.hrm.dto.response.BonusSalaryResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BonusSalaryRepositoryCustom {
  void insertBonusSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long bonusType, BigDecimal bonus);

  List<BonusSalaryResponse> getListBonusMonthlyBySalaryMonthlyId(Long salaryId);

  BigDecimal sumListBonusMonthlyBySalaryMonthlyId(Long salaryId);

  void updateBonusSalaryByBonusSalaryId(BonusSalaryDto bonusSalaryDto);

  void deleteBonusSalaryByBonusSalaryId(Long bonusSalaryId);

  Optional<SalaryMonthlyInfoDto> getSalaryMonthlyInfoByBonusSalary(Long bonusSalaryId);

  boolean checkExistBonusSalary(Long bonusSalaryId);
}