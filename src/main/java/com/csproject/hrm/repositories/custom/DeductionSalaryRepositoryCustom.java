package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.DeductionSalaryResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeductionSalaryRepositoryCustom {
  void insertDeductionSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long deductionType, BigDecimal bonus);

  List<DeductionSalaryResponse> getListDeductionMonthlyBySalaryMonthlyId(Long salaryId);

  BigDecimal sumListDeductionMonthlyBySalaryMonthlyId(Long salaryId);
}