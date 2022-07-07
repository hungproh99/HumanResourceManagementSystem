package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.SalaryContract;
import com.csproject.hrm.repositories.custom.SalaryContractRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryContractRepository
    extends JpaRepository<SalaryContract, Long>, SalaryContractRepositoryCustom {}
