package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.ApplicationRequestDto;
import com.csproject.hrm.dto.dto.RequestNameDto;
import com.csproject.hrm.dto.dto.RequestStatusDto;
import com.csproject.hrm.dto.dto.RequestTypeDto;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ApplicationsRequestResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationsRequestRepositoryCustom {
  List<ApplicationsRequestResponse> getListApplicationRequestReceive(
      QueryParam queryParam, String employeeId);

  List<ApplicationsRequestResponse> getListApplicationRequestSend(
      QueryParam queryParam, String employeeId);

  void insertApplicationRequest(
      ApplicationsRequestRequest applicationsRequest,
      LocalDateTime createdDate,
      LocalDateTime latestDate,
      LocalDateTime duration);

  void updateCheckedApplicationRequest(
      UpdateApplicationRequestRequest updateApplicationRequestRequest,
      String employeeId,
      LocalDateTime latestDate);

  int countListApplicationRequestReceive(QueryParam queryParam, String employeeId);

  int countListApplicationRequestSend(QueryParam queryParam, String employeeId);

  void updateTaxEnrollmentByApplicationRequest(String employeeId, Long taxType, boolean status);

  void insertUpdateCompanyAssetsByApplicationRequest(String employeeId, LocalDate date);

  List<RequestStatusDto> getAllRequestStatus();

  List<RequestTypeDto> getAllRequestType();

  List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID);

  void changeIsRead(boolean isRead, Long requestId);

  Optional<ApplicationRequestDto> getApplicationRequestDtoByRequestId(Long requestId);

  List<ApplicationsRequestResponse> getListApplicationRequestReceiveByListId(
      QueryParam queryParam, String employeeId, List<Long> list);

  List<ApplicationsRequestResponse> getListApplicationRequestSendByListId(
      QueryParam queryParam, String employeeId, List<Long> list);
}
