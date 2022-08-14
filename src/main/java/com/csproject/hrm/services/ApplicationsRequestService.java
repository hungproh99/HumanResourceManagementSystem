package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.RequestNameDto;
import com.csproject.hrm.dto.dto.RequestStatusDto;
import com.csproject.hrm.dto.dto.RequestTypeDto;
import com.csproject.hrm.dto.request.ApplicationsRequestCreateRequest;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.RejectApplicationRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ListApplicationsRequestResponse;
import com.csproject.hrm.jooq.QueryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationsRequestService {
  ListApplicationsRequestResponse getAllApplicationRequestReceive(
      QueryParam queryParam, String employeeId);

  ListApplicationsRequestResponse getAllApplicationRequestSend(
      QueryParam queryParam, String employeeId);

  void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest);

  void updateCheckedApplicationRequest(
      UpdateApplicationRequestRequest updateApplicationRequestRequest, String employeeId);

  List<RequestStatusDto> getAllRequestStatus();

  List<RequestTypeDto> getAllRequestType();

  List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID);

  //  void updateIsRead(Long requestId);

  void updateApproveApplicationRequest(Long requestId);

  void updateRejectApplicationRequest(
      RejectApplicationRequestRequest rejectApplicationRequestRequest);

  void exportApplicationRequestReceiveByExcel(
      HttpServletResponse response, QueryParam queryParam, String employeeId, List<Long> list);

  void exportApplicationRequestSendByExcel(
      HttpServletResponse response, QueryParam queryParam, String employeeId, List<Long> list);

  void exportApplicationRequestReceiveToCsv(
      Writer writer, QueryParam queryParam, String employeeId, List<Long> list);

  void exportApplicationRequestSendToCsv(
      Writer writer, QueryParam queryParam, String employeeId, List<Long> list);

  void createApplicationsRequest(ApplicationsRequestCreateRequest applicationsRequest);

  int getPaidLeaveDayRemaining(String employeeId);

  Integer[] getOTTimeRemaining(String employeeId);

  List<RequestTypeDto> getAllRequestTypeByEmployeeLevel(String employeeId);

  void updateAllApplicationRequestRemind(LocalDateTime checkDate, LocalDateTime currDate);
}