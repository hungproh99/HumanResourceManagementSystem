package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.AdvanceSalaryResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdvanceSalaryRepositoryCustom {
  void insertAdvanceSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, BigDecimal bonus);

  List<AdvanceSalaryResponse> getListAdvanceBySalaryIdAndMonth(
      Long salaryId, LocalDate startDate, LocalDate endDate);

  BigDecimal sumAdvanceBySalaryIdAndMonth(Long salaryId, LocalDate startDate, LocalDate endDate);
}
