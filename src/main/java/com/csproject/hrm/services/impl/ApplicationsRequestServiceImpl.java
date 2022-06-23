package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.ApplicationsRequestRepository;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.ApplicationsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.csproject.hrm.common.constant.Constants.NOT_EXIST_USER_WITH;

@Service
public class ApplicationsRequestServiceImpl implements ApplicationsRequestService {

  @Autowired EmployeeRepository employeeRepository;
  @Autowired ApplicationsRequestRepository applicationsRequestRepository;

  @Override
  public List<ApplicationsRequestRespone> getAllApplicationRequestForEmployeeId(
      QueryParam queryParam, String employeeId) {
    if (employeeRepository.findById(employeeId).isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    }
    return applicationsRequestRepository.getListApplicationRequestByEmployeeId(
        queryParam, employeeId);
  }
}
