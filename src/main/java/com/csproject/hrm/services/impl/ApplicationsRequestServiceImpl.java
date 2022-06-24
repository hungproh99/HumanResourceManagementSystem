package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.exception.*;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.ApplicationsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class ApplicationsRequestServiceImpl implements ApplicationsRequestService {

  @Autowired EmployeeRepository employeeRepository;
  @Autowired ApplicationsRequestRepository applicationsRequestRepository;
  @Autowired EmployeeDetailRepository employeeDetailRepository;

  @Override
  public List<ApplicationsRequestRespone> getAllApplicationRequestForEmployeeId(
      QueryParam queryParam, String employeeId) {
    if (employeeRepository.findById(employeeId).isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    }
    return applicationsRequestRepository.getListApplicationRequestByEmployeeId(
        queryParam, employeeId);
  }

  @Override
  public void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest) {
    if (applicationsRequest.getEmployee_id() == null
        || applicationsRequest.getRequest_type_id() == null
        || applicationsRequest.getRequest_name_id() == null
        || applicationsRequest.getDescription() == null
        || applicationsRequest.getApprover() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(applicationsRequest.getEmployee_id())) {
      applicationsRequestRepository.insertApplicationRequest(applicationsRequest);
    } else {
      throw new CustomDataNotFoundException(
          NO_EMPLOYEE_WITH_ID + applicationsRequest.getEmployee_id());
    }
  }
}