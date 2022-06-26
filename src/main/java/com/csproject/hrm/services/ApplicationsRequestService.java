package com.csproject.hrm.services;

import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ListApplicationsRequestResponse;
import com.csproject.hrm.jooq.QueryParam;

public interface ApplicationsRequestService {
  ListApplicationsRequestResponse getAllApplicationRequestReceive(
      QueryParam queryParam, String employeeId);

  ListApplicationsRequestResponse getAllApplicationRequestSend(
      QueryParam queryParam, String employeeId);

  void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest);

  void updateStatusApplicationRequest(UpdateApplicationRequestRequest updateApplicationRequestRequest);
}