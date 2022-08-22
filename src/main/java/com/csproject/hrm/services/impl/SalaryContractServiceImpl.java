package com.csproject.hrm.services.impl;

import com.csproject.hrm.repositories.SalaryContractRepository;
import com.csproject.hrm.repositories.WorkingContractRepository;
import com.csproject.hrm.services.SalaryContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SalaryContractServiceImpl implements SalaryContractService {

  @Autowired SalaryContractRepository salaryContractRepository;
  @Autowired WorkingContractRepository workingContractRepository;

  @Override
  public void updateStatusSalaryContract(Boolean status, LocalDate dateCheck) {
    salaryContractRepository.updateStatusSalaryContract(status, dateCheck);
  }

  @Override
  public void updateStatusWorkingContract(Boolean status, LocalDate dateCheck) {
    workingContractRepository.updateStatusWorkingContract(status, dateCheck);
  }
}
