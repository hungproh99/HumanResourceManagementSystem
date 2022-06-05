package com.csproject.hrm.repositories.Impl;

import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepositoryCustom {
    List<HrmResponse> findAllEmployee(QueryParam queryParam);

    void insertEmployee(HrmRequest hrmRequest, String employeeId, String companyEmail, String password,
                        String workStatus, String companyName, String contractStatus, LocalDate startDate);

    int countEmployeeSameStartName(String fullName);

    int countAllEmployeeByCondition(QueryParam queryParam);
}