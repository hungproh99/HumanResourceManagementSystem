package com.csproject.hrm.services;

import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface ApplicationsRequestService {
  List<ApplicationsRequestRespone> getAllApplicationRequestForEmployeeId(
      QueryParam queryParam, String employeeId);
}
