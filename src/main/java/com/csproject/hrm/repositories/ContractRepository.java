package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.WorkingContract;
import com.csproject.hrm.repositories.custom.ContractRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository
    extends JpaRepository<WorkingContract, Long>, ContractRepositoryCustom {}
