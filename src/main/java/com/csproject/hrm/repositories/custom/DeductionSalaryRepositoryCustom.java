package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.DeductionSalaryDto;
import com.csproject.hrm.dto.dto.DeductionTypeDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyInfoDto;
import com.csproject.hrm.dto.response.DeductionSalaryResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeductionSalaryRepositoryCustom {
  void insertDeductionSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long deductionType, BigDecimal bonus);

  List<DeductionSalaryResponse> getListDeductionMonthlyBySalaryMonthlyId(Long salaryId);

  BigDecimal sumListDeductionMonthlyBySalaryMonthlyId(Long salaryId);

  List<DeductionTypeDto> getListDeductionTypeDto();

  void updateDeductionSalaryByDeductionSalaryId(DeductionSalaryDto deductionSalaryDto);

  void deleteDeductionSalaryByDeductionSalaryId(Long deductionSalaryId);

  Optional<SalaryMonthlyInfoDto> getSalaryMonthlyInfoByDeductionSalary(Long deductionSalaryId);

  boolean checkExistDeductionSalary(Long deductionSalaryId);
}
