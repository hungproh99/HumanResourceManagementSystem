package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.ApplicationsRequest;
import com.csproject.hrm.repositories.custom.ApplicationsRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationsRequestRepository
    extends JpaRepository<ApplicationsRequest, Long>, ApplicationsRequestRepositoryCustom {}
