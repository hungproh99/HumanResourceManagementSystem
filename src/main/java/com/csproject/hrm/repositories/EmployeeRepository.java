package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Employee;
import com.csproject.hrm.repositories.Impl.EmployeeRepositoryCustom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String>, EmployeeRepositoryCustom {
	Optional<Employee> findById(String id);
	
	@Query(value = "SELECT employee_id FROM employee e WHERE e.company_email = ?1", nativeQuery = true)
	String findIdByCompanyEmail(String companyEmail);
	
	@Query(value = "SELECT password FROM employee e WHERE e.employee_id = ?1", nativeQuery = true)
	String findPasswordById(String id);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE employee SET password = ?1 WHERE employee_id = ?2", nativeQuery = true)
	int updatePassword(String password, String id);
	
	Boolean existsByCompanyEmail(String companyEmail);
	
	@Query(value = "select e from Employee e")
	Employee getById(String s);
}