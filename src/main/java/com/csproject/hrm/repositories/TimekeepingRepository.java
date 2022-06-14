package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Timekeeping;
import com.csproject.hrm.repositories.custom.TimekeepingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimekeepingRepository
    extends JpaRepository<Timekeeping, Long>, TimekeepingRepositoryCustom {}
