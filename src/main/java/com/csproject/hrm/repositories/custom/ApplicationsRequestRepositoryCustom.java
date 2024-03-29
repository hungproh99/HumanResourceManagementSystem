package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.ApplicationsRequestCreateRequest;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.RejectApplicationRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ApplicationRequestRemindResponse;
import com.csproject.hrm.dto.response.ApplicationsRequestResponse;
import com.csproject.hrm.dto.response.PolicyTypeAndNameResponse;
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
      boolean isRemind,
      LocalDateTime latestDate);

  void updateRejectApplicationRequest(
      RejectApplicationRequestRequest rejectApplicationRequestRequest, LocalDateTime latestDate);

  void updateStatusApplication(Long requestId, String status, LocalDateTime latestDate);

  int countListApplicationRequestReceive(QueryParam queryParam, String employeeId);

  int countListApplicationRequestSend(QueryParam queryParam, String employeeId);

  void updateTaxEnrollmentByApplicationRequest(String employeeId, Long taxType, boolean status);

  void insertUpdateCompanyAssetsByApplicationRequest(String employeeId, LocalDate date);

  List<RequestStatusDto> getAllRequestStatus();

  List<RequestTypeDto> getAllRequestType();

  List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID);

  //  void changeIsRead(boolean isRead, Long requestId);

  Optional<ApplicationRequestDto> getApplicationRequestDtoByRequestId(Long requestId);

  List<ApplicationsRequestResponse> getListApplicationRequestReceiveByListId(
      QueryParam queryParam, String employeeId, List<Long> list);

  List<ApplicationsRequestResponse> getListApplicationRequestSendByListId(
      QueryParam queryParam, String employeeId, List<Long> list);

  void createApplicationsRequest(ApplicationsRequestCreateRequest applicationsRequest);

  List<RequestTypeDto> getAllRequestTypeByEmployeeLevel(String employeeId);

  String getDescriptionByRequestNameID(Long requestNameID);

  PolicyTypeAndNameResponse getPolicyByRequestNameID(Long requestNameID);

  Boolean checkPermissionToCreate(String employeeId, Long requestNameId);

  Boolean checkPermissionToApprove(String employeeId, Long requestNameId);

  void createApproveTaxEnrollment(EmployeeTaxDto employeeTaxDto);

  List<ApplicationRequestRemindResponse> getAllApplicationRequestToRemind(LocalDateTime checkDate);

  //  void updateAllApplicationRequestRemind(Long requestId, boolean isRemind);

  String getDataOfPolicy(Long requestNameId);

  boolean checkExistRequestId(Long requestId);

  boolean checkAlreadyApproveOrReject(Long requestId);
}