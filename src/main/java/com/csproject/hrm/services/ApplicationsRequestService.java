package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.jooq.QueryParam;

import java.util.List;

public interface ApplicationsRequestService {
  List<ApplicationsRequestRespone> getAllApplicationRequestReceive(
      QueryParam queryParam, String employeeId);

  List<ApplicationsRequestRespone> getAllApplicationRequestSend(
      QueryParam queryParam, String employeeId);

  void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest);

  List<RequestStatusDto> getAllRequestStatus();

  List<RequestTypeDto> getAllRequestType();

  List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID);
}