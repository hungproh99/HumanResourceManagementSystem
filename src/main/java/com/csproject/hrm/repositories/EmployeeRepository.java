package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByCompanyEmail(String companyEmail);

}
