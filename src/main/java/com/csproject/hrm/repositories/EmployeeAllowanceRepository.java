package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.EmployeeAllowance;
import com.csproject.hrm.repositories.custom.EmployeeAllowanceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAllowanceRepository
    extends JpaRepository<EmployeeAllowance, Long>, EmployeeAllowanceRepositoryCustom {}
