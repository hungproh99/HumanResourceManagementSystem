package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.SalaryContractDto;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalaryContractRepositoryCustom {
  void insertNewSalaryContract(
      String employeeId,
      BigDecimal newSalary,
      LocalDate startDate,
      boolean oldStatus,
      boolean newStatus);
	
	void updateSalaryContract(String employeeId, BigDecimal newSalary, LocalDate startDate, boolean status);
	
	Optional<SalaryContractDto> getSalaryContractByEmployeeId(String employeeId);
}