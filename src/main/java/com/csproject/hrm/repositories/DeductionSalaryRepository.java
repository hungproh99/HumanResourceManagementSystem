package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.DeductionSalary;
import com.csproject.hrm.repositories.custom.DeductionSalaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeductionSalaryRepository
    extends JpaRepository<DeductionSalary, Long>, DeductionSalaryRepositoryCustom {}
