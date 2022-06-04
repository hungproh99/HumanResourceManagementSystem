package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Employee;
import com.csproject.hrm.repositories.Custom.EmployeeDetailCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDetailRepository extends JpaRepository<Employee, String>, EmployeeDetailCustom {

}