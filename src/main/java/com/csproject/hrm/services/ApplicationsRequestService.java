package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ListApplicationsRequestResponse;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface ApplicationsRequestService {
  ListApplicationsRequestResponse getAllApplicationRequestReceive(
      QueryParam queryParam, String employeeId);

  ListApplicationsRequestResponse getAllApplicationRequestSend(
      QueryParam queryParam, String employeeId);

  void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest);

  void updateStatusApplicationRequest(
      UpdateApplicationRequestRequest updateApplicationRequestRequest);

  List<RequestStatusDto> getAllRequestStatus();

  List<RequestTypeDto> getAllRequestType();

  List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID);
}