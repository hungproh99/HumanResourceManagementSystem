package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Policy;
import com.csproject.hrm.repositories.custom.PolicyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, String>, PolicyRepositoryCustom {}