package com.csproject.hrm.services;

import java.time.LocalDate;

public interface SalaryContractService {
  void updateStatusSalaryContract(Boolean status, LocalDate dateCheck);
}
