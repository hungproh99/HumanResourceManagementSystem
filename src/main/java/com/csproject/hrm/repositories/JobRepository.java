package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {}
