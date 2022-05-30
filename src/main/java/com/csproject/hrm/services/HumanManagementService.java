package com.csproject.hrm.services;

import com.csproject.hrm.common.constant.Constants;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.exception.CustomParameterConstraintException;
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
    public List<HrmResponse> getListHumanResource(String offsetInput, String limitInput) {
        int offset, limit;
        if (offsetInput == null) {
            offset = 0;
        } else if (limitInput == null) {
            limit = 10;
        }
        try {
            offset = Integer.parseInt(offsetInput);
            limit = Integer.parseInt(limitInput);
            if (offset < 0) {
                throw new CustomParameterConstraintException(Constants.OFFSET_INVALID);
            } else if (limit < 0) {
                throw new CustomParameterConstraintException(Constants.LIMIT_INVALID);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
        List<HrmResponse> employeeResponses = employeeRepository.getListEmployee(offset, limit);
        return employeeResponses;
    }
}
