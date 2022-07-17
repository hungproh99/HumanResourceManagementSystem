package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.EmployeeInsurance;
import com.csproject.hrm.repositories.custom.EmployeeInsuranceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeInsuranceRepository
    extends JpaRepository<EmployeeInsurance, Long>, EmployeeInsuranceRepositoryCustom {}
