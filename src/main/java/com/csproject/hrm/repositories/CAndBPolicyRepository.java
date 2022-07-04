package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.CompensationBenefitPolicy;
import com.csproject.hrm.repositories.custom.CAndBPolicyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CAndBPolicyRepository
    extends JpaRepository<CompensationBenefitPolicy, String>, CAndBPolicyRepositoryCustom {}