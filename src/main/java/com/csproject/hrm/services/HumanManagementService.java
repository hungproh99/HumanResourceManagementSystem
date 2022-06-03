package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.impl.HumanManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumanManagementService implements HumanManagementServiceImpl {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<HrmResponse> getListHumanResource(QueryParam queryParam) {
        return employeeRepository.findAllEmployee(queryParam);
    }
}
