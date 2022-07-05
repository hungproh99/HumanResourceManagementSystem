package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ApplicationsRequestResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

  void updateStatusApplicationRequest(
      UpdateApplicationRequestRequest updateApplicationRequestRequest, LocalDateTime latestDate);

  int countListApplicationRequestReceive(QueryParam queryParam, String employeeId);

  int countListApplicationRequestSend(QueryParam queryParam, String employeeId);

  void updateBonusSalaryByApplicationRequest(
      String employeeId, String description, BigDecimal bonus);

  void updateDayWorkByApplicationRequest(
      String employeeId,
      LocalDate startDate,
      LocalDate endDate,
      long oldTimekeepingStatus,
      long newTimekeepingStatus);

  void insertDayWorkByApplicationRequest(
      String employeeId, LocalDate startDate, LocalDate endDate, long timekeepingStatus);

  void updateTaxEnrollmentByApplicationRequest(String employeeId, Long taxType, boolean status);

  void insertAdvancePaymentByApplicationRequest(
      String employeeId, LocalDate date, BigDecimal value);

  void insertUpdateCompanyAssetsByApplicationRequest(String employeeId, LocalDate date);

  List<RequestStatusDto> getAllRequestStatus();

  List<RequestTypeDto> getAllRequestType();

  List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID);
}