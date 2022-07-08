package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.AdvancesSalary;
import com.csproject.hrm.repositories.custom.AdvanceSalaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvanceSalaryRepository
    extends JpaRepository<AdvancesSalary, Long>, AdvanceSalaryRepositoryCustom {}
