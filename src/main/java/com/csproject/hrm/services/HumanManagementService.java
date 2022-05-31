package com.csproject.hrm.services;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.impl.HumanManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class HumanManagementService implements HumanManagementServiceImpl {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<HrmResponse> getListHumanResource(String limitInput, String pageInput) {
        int limit, page;
        try {
            limit = Integer.parseInt(limitInput);
            page = Integer.parseInt(pageInput);
            long maxPage = employeeRepository.count();
            if (page < 0 || page > maxPage) {
                throw new CustomParameterConstraintException(Constants.PAGE_INVALID + maxPage);
            } else if (limit < 0) {
                throw new CustomParameterConstraintException(Constants.LIMIT_INVALID);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
        List<HrmResponse> employeeResponses = employeeRepository.findAllEmployee(limit, page);
        return employeeResponses;
    }
}
