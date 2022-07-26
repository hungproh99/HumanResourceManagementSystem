package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.WorkingContract;
import com.csproject.hrm.repositories.custom.WorkingContractRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingContractRepository
    extends JpaRepository<WorkingContract, Long>, WorkingContractRepositoryCustom {}