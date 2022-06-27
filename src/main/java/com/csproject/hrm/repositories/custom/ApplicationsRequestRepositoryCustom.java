package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationsRequestRepositoryCustom {
  List<ApplicationsRequestRespone> getListApplicationRequestReceive(
      QueryParam queryParam, String employeeId);

  List<ApplicationsRequestRespone> getListApplicationRequestSend(
      QueryParam queryParam, String employeeId);

  void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest);

  List<RequestStatusDto> getAllRequestStatus();

  List<RequestTypeDto> getAllRequestType();

  List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID);
}