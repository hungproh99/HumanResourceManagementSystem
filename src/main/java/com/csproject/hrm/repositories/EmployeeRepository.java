package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findById(String id);

    @Query(value = "SELECT employee_id FROM employee e WHERE e.company_email = ?1", nativeQuery = true)
    String findIdByCompanyEmail(String companyEmail);

    Boolean existsByCompanyEmail(String companyEmail);

}
