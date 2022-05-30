package com.csproject.hrm.repositories;

import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
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

    @Query(value = "select new com.csproject.hrm.dto.response.HrmResponse(e.employee_id,e.full_name,e.company_email,e.work_status,e.phone_number,e.gender," +
            "e.birth_date,j.position as job,o.name as office,a.name as area,ct.name as contract," +
            "concat(year(curdate())-year(wc.start_date),' year ',month(curdate()-wc.start_date)," +
            "' month ',day(curdate()-wc.start_date), ' day') as seniority,wc.start_date) " +
            "from employee e " +
            "left join working_contract wc on e.employee_id = wc.employee_id " +
            "left join contract_type ct on ct.type_id = wc.type_id " +
            "left join working_place wp on wp.working_contract_id = wc.working_contract_id " +
            "left join area a on a.area_id = wp.area_id " +
            "left join office o on o.office_id = wp.office_id " +
            "left join job j on j.job_id = wp.job_id limit ?1,?2")
    List<HrmResponse> getListEmployee(int offset, int limit);
}
