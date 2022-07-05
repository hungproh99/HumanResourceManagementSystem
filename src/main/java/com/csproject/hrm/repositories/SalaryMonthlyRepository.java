package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.SalaryMonthly;
import com.csproject.hrm.repositories.custom.SalaryMonthlyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryMonthlyRepository
    extends JpaRepository<SalaryMonthly, Long>, SalaryMonthlyRepositoryCustom {}
