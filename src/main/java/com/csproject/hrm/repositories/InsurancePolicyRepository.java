package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.WorkingPolicy;
import com.csproject.hrm.repositories.custom.InsurancePolicyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsurancePolicyRepository
    extends JpaRepository<WorkingPolicy, String>, InsurancePolicyRepositoryCustom {}