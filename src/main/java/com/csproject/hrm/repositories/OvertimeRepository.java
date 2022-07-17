package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Overtime;
import com.csproject.hrm.repositories.custom.OvertimeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvertimeRepository
    extends JpaRepository<Overtime, Long>, OvertimeRepositoryCustom {}
