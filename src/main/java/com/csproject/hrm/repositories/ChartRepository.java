package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Employee;
import com.csproject.hrm.repositories.custom.ChartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartRepository extends JpaRepository<Employee, Long>, ChartRepositoryCustom {}