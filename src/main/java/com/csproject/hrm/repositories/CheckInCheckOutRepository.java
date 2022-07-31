package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.CheckInCheckOut;
import com.csproject.hrm.repositories.custom.CheckInCheckOutRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInCheckOutRepository
    extends JpaRepository<CheckInCheckOut, Long>, CheckInCheckOutRepositoryCustom {}
