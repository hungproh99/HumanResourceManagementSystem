package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.EmployeeTax;
import com.csproject.hrm.repositories.custom.EmployeeTaxRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTaxRepository
    extends JpaRepository<EmployeeTax, Long>, EmployeeTaxRepositoryCustom {}
