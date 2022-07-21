package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.AdvanceSalaryDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyInfoDto;
import com.csproject.hrm.dto.response.AdvanceSalaryResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvanceSalaryRepositoryCustom {
  void insertAdvanceSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, BigDecimal bonus);

  List<AdvanceSalaryResponse> getListAdvanceMonthlyBySalaryMonthlyId(Long salaryId);

  BigDecimal sumListAdvanceMonthlyBySalaryMonthlyId(Long salaryId);

  void updateAdvanceSalaryByAdvanceId(AdvanceSalaryDto advanceSalaryDto);

  void deleteAdvanceSalaryByAdvanceId(Long advanceId);

  Optional<SalaryMonthlyInfoDto> getSalaryMonthlyInfoByAdvanceSalary(Long advanceSalaryId);

  boolean checkExistAdvanceSalary(Long advanceSalaryId);
}
