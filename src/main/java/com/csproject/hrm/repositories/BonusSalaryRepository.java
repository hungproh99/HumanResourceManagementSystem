package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.BonusSalary;
import com.csproject.hrm.repositories.custom.BonusSalaryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusSalaryRepository
    extends JpaRepository<BonusSalary, Long>, BonusSalaryRepositoryCustom {}
