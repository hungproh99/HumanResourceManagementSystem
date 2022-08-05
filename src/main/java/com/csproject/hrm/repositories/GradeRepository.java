package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.GradeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<GradeType, Long> {}
