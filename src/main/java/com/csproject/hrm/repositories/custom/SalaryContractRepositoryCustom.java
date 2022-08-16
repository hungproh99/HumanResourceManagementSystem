package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.SalaryContractDto;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SalaryContractRepositoryCustom {
  void insertNewSalaryContractByIncreaseSalary(
      String employeeId, BigDecimal newSalary, LocalDate startDate, boolean status);

	void updateSalaryContract(String employeeId, BigDecimal newSalary, LocalDate startDate, boolean status);
	
	Optional<SalaryContractDto> getSalaryContractByEmployeeId(String employeeId);

  void updateStatusSalaryContract(Boolean status, LocalDate dateCheck);
}