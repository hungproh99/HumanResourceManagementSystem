package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.*;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.ApplicationsRequestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Objects;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class ApplicationsRequestServiceImpl implements ApplicationsRequestService {

  @Autowired EmployeeRepository employeeRepository;
  @Autowired ApplicationsRequestRepository applicationsRequestRepository;
  @Autowired EmployeeDetailRepository employeeDetailRepository;
  @Autowired PolicyRepository policyRepository;
  @Autowired GeneralFunction generalFunction;

  @Override
  public ListApplicationsRequestResponse getAllApplicationRequestReceive(
      QueryParam queryParam, String employeeId) {
    if (employeeRepository.findById(employeeId).isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    }
    List<ApplicationsRequestResponse> applicationsRequestResponseList =
        applicationsRequestRepository.getListApplicationRequestReceive(queryParam, employeeId);
    int total =
        applicationsRequestRepository.countListApplicationRequestReceive(queryParam, employeeId);
    return ListApplicationsRequestResponse.builder()
        .applicationsRequestResponseList(applicationsRequestResponseList)
        .total(total)
        .build();
  }

  @Override
  public ListApplicationsRequestResponse getAllApplicationRequestSend(
      QueryParam queryParam, String employeeId) {
    if (employeeRepository.findById(employeeId).isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    }
    List<ApplicationsRequestResponse> applicationsRequestResponseList =
        applicationsRequestRepository.getListApplicationRequestSend(queryParam, employeeId);
    int total =
        applicationsRequestRepository.countListApplicationRequestSend(queryParam, employeeId);
    return ListApplicationsRequestResponse.builder()
        .applicationsRequestResponseList(applicationsRequestResponseList)
        .total(total)
        .build();
  }

  @Override
  public void insertApplicationRequest(ApplicationsRequestRequest applicationsRequest) {
    if (employeeRepository.findById(applicationsRequest.getEmployeeId()).isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + applicationsRequest.getEmployeeId());
    }
    if (applicationsRequest.getEmployeeId() == null
        || applicationsRequest.getRequestNameId() == null
        || applicationsRequest.getRequestStatusId() == null
        || applicationsRequest.getFullName() == null
        || applicationsRequest.getDescription() == null
        || applicationsRequest.getApprover() == null
        || applicationsRequest.getIsBookmark()) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    LocalDateTime createdDate = LocalDateTime.now();
    LocalDateTime latestDate = LocalDateTime.now();
    LocalDateTime duration = LocalDateTime.now().plusWeeks(1);
    applicationsRequestRepository.insertApplicationRequest(
        applicationsRequest, createdDate, latestDate, duration);
  }

  @Override
  public void updateStatusApplicationRequest(
      UpdateApplicationRequestRequest updateApplicationRequestRequest) {
    if (updateApplicationRequestRequest.getApplicationRequestId() == null
        || updateApplicationRequestRequest.getRequestStatus() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    LocalDateTime latestDate = LocalDateTime.now();
    applicationsRequestRepository.updateStatusApplicationRequest(
        updateApplicationRequestRequest, latestDate);
  }

  @Override
  public List<RequestStatusDto> getAllRequestStatus() {
    List<RequestStatusDto> requestStatusDtoList =
        applicationsRequestRepository.getAllRequestStatus();
    requestStatusDtoList.forEach(
        requestStatus -> {
          requestStatus.setRequest_status_name(
              ERequestStatus.getLabel(requestStatus.getRequest_status_name()));
        });

    return requestStatusDtoList;
  }

  @Override
  public List<RequestTypeDto> getAllRequestType() {
    List<RequestTypeDto> requestTypeDtoList = applicationsRequestRepository.getAllRequestType();
    requestTypeDtoList.forEach(
        requestTypeDto -> {
          requestTypeDto.setRequest_type_name(
              ERequestType.getLabel(requestTypeDto.getRequest_type_name()));
        });

    return requestTypeDtoList;
  }

  @Override
  public List<RequestNameDto> getAllRequestNameByRequestTypeID(Long requestTypeID) {
    List<RequestNameDto> requestNameDtoList =
        applicationsRequestRepository.getAllRequestNameByRequestTypeID(requestTypeID);
    requestNameDtoList.forEach(
        requestNameDto -> {
          requestNameDto.setRequest_name_name(
              ERequestName.getLabel(requestNameDto.getRequest_name_name()));
        });

    return requestNameDtoList;
  }

  @Override
  public void updateIsRead(Long requestId) {
    boolean isRead = false;
    applicationsRequestRepository.changeIsRead(isRead, requestId);
  }

  @Override
  public void createApplicationsRequest(ApplicationsRequestRequestC applicationsRequest) {
    String createEmployeeId = applicationsRequest.getCreateEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(createEmployeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + createEmployeeId);
    }
    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
    }
    Long requestTypeId = applicationsRequest.getRequestTypeId();
    Long requestNameId = applicationsRequest.getRequestNameId();
    if (requestTypeId == null || requestNameId == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    if (!applicationsRequestRepository.getAllRequestNameByRequestTypeID(requestTypeId).stream()
        .anyMatch(requestNameDto -> requestNameDto.getRequest_name_id().equals(requestNameId))) {
      throw new CustomDataNotFoundException("Request Type or Request Name not existed!");
    }
    if (!applicationsRequestRepository.checkPermissionToCreate(createEmployeeId, requestNameId)) {
      throw new CustomErrorException("You don't have permission to create this request!");
    }

    String approver =
        Objects.requireNonNullElse(
            employeeDetailRepository.getManagerIDByEmployeeID(createEmployeeId), "huynq100");

    switch (requestTypeId.intValue()) {
      case 1:
        {
          switch (requestNameId.intValue()) {
            case 1:
              {
                applicationsRequest =
                    createRequestForWorkingScheduleAndWorkingTime(applicationsRequest, approver);
                break;
              }
            case 2:
              {
                applicationsRequest =
                    createRequestForWorkingScheduleAndOT(applicationsRequest, approver);
              }
              break;
          }
          break;
        }
      case 2:
        {
          switch (requestNameId.intValue()) {
            case 6:
              {
                applicationsRequest = createRequestForPairLeave(applicationsRequest, approver);
                break;
              }
          }
          break;
        }
      case 3:
        {
          //          if (!applicationsRequestRepository.checkPermissionToApprove(
          //              createEmployeeId, requestNameId)) {
          //            throw new CustomErrorException("You don't have permission to approve this
          // request!");
          //          }
          switch (requestNameId.intValue()) {
            case 3:
              {
                applicationsRequest =
                    createRequestForNominationAndPromotion(applicationsRequest, approver);
                break;
              }
            case 4:
            case 5:
              {
                applicationsRequest =
                    createRequestForNominationAndSalaryIncreaseOrBonus(
                        applicationsRequest, approver);
                break;
              }
          }
          break;
        }
      case 4:
      case 5:
      case 6:
        {
          //          if (!applicationsRequestRepository.checkPermissionToApprove(
          //              createEmployeeId, requestNameId)) {
          //            throw new CustomErrorException("You don't have permission to approve this
          // request!");
          //          }
          applicationsRequest = createRequestForPenalise(applicationsRequest, approver);
          break;
        }
      case 7:
        {
          switch (requestNameId.intValue()) {
            case 12:
              {
                applicationsRequest = createRequestForAdvances(applicationsRequest, approver);
                break;
              }
          }
          break;
        }
      case 8:
        {
          switch (requestNameId.intValue()) {
            case 13:
              {
                applicationsRequest = createRequestForTaxEnrollment(applicationsRequest, approver);
                break;
              }
          }
          break;
        }
    }
    applicationsRequest.setApprover(approver);
    applicationsRequest.setRequestStatusId(Long.valueOf(1));
    applicationsRequest.setCreateDate(LocalDate.now());
    applicationsRequest.setLatestDate(LocalDate.now());
    applicationsRequest.setDuration(LocalDate.now().plusDays(3));
    applicationsRequest.setIsBookmark(false);
    applicationsRequest.setIsRemind(false);
    applicationsRequest.setIsRead(false);

    generalFunction.sendEmailCreateRequest(
        createEmployeeId, employeeId, FROM_EMAIL, "hihihd37@gmail.com", "New request");

    applicationsRequestRepository.createApplicationsRequest(applicationsRequest);
  }

  private ApplicationsRequestRequestC setDescriptionAndData(
      ApplicationsRequestRequestC applicationsRequest, String[] valueArray) {
    String data = "";
    String description =
        applicationsRequestRepository.getDescriptionByRequestNameID(
            applicationsRequest.getRequestNameId());
    String[] keyArray = getKeyInDescription(description);
    for (int i = 0; i < keyArray.length; i++) {
      description =
          description.replaceAll(
              "\\[" + keyArray[i] + "\\]", "<p style=\"color:red\">" + valueArray[i] + "</p>");
      data = data + "[" + keyArray[i] + "|" + valueArray[i] + "]";
    }
    applicationsRequest.setData(data);
    applicationsRequest.setDescription(
        description + "<br>P/S: " + applicationsRequest.getDescription());
    return applicationsRequest;
  }

  private ApplicationsRequestRequestC createRequestForWorkingScheduleAndWorkingTime(
      ApplicationsRequestRequestC applicationsRequest, String approver) {
    String date = checkLocalDateNull(applicationsRequest.getDate()).toString();

    String[] valueArray = {approver, date};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForWorkingScheduleAndOT(
      ApplicationsRequestRequestC applicationsRequest, String approver) {
    String startTime = checkLocalTimeNull(applicationsRequest.getStartTime()).toString();
    String endTime = checkLocalTimeNull(applicationsRequest.getEndTime()).toString();
    String startDate = checkLocalDateNull(applicationsRequest.getStartDate()).toString();
    String endDate = checkLocalDateNull(applicationsRequest.getEndDate()).toString();

    String[] valueArray = {approver, startDate, endDate, startTime, endTime};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForPairLeave(
      ApplicationsRequestRequestC applicationsRequest, String approver) {
    String startDate = checkLocalDateNull(applicationsRequest.getStartDate()).toString();
    String endDate = checkLocalDateNull(applicationsRequest.getEndDate()).toString();

    String[] valueArray = {approver, startDate, endDate};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForAdvances(
      ApplicationsRequestRequestC applicationsRequest, String approver) {
    String value = checkStringNull(applicationsRequest.getValue());

    String[] valueArray = {approver, value};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForTaxEnrollment(
      ApplicationsRequestRequestC applicationsRequest, String approver) {
    List<String> taxTypes = applicationsRequest.getTaxType();
    String taxType = StringUtils.join(taxTypes, ",");

    String[] valueArray = {approver, taxType};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForNominationAndPromotion(
      ApplicationsRequestRequestC applicationsRequest, String approver) {

    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(
          NO_EMPLOYEE_WITH_ID + applicationsRequest.getEmployeeId());
    }

    String employeeName = checkStringNull(applicationsRequest.getEmployeeName());
    String currentTitle = checkStringNull(applicationsRequest.getCurrentTitle());
    String desiredTitle = checkStringNull(applicationsRequest.getDesiredTitle());
    String currentArea = checkStringNull(applicationsRequest.getCurrentArea());
    String desiredArea = checkStringNull(applicationsRequest.getDesiredArea());
    String currentOffice = checkStringNull(applicationsRequest.getCurrentOffice());
    String desiredOffice = checkStringNull(applicationsRequest.getDesiredOffice());

    String[] valueArray = {
      approver,
      employeeName + "-" + employeeId,
      currentTitle,
      currentArea,
      currentOffice,
      desiredTitle,
      desiredArea,
      desiredOffice
    };

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForNominationAndSalaryIncreaseOrBonus(
      ApplicationsRequestRequestC applicationsRequest, String approver) {

    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(
          NO_EMPLOYEE_WITH_ID + applicationsRequest.getEmployeeId());
    }

    String employeeName = checkStringNull(applicationsRequest.getEmployeeName());
    String currentTitle = checkStringNull(applicationsRequest.getCurrentTitle());
    String currentArea = checkStringNull(applicationsRequest.getCurrentArea());
    String currentOffice = checkStringNull(applicationsRequest.getCurrentOffice());
    String value = checkStringNull(applicationsRequest.getValue());

    String[] valueArray = {
      approver, employeeName + "-" + employeeId, currentTitle, currentArea, currentOffice, value
    };

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForPenalise(
      ApplicationsRequestRequestC applicationsRequest, String approver) {

    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(
          NO_EMPLOYEE_WITH_ID + applicationsRequest.getEmployeeId());
    }

    String date = checkLocalDateNull(applicationsRequest.getDate()).toString();
    String employeeName = checkStringNull(applicationsRequest.getEmployeeName());
    String currentTitle = checkStringNull(applicationsRequest.getCurrentTitle());
    String currentArea = checkStringNull(applicationsRequest.getCurrentArea());
    String currentOffice = checkStringNull(applicationsRequest.getCurrentOffice());
    String value = checkStringNull(applicationsRequest.getValue());
    PolicyTypeAndNameResponse policyTypeAndName =
        applicationsRequestRepository.getPolicyByRequestNameID(
            applicationsRequest.getRequestNameId());

    String[] valueArray = {
      approver,
      date,
      employeeName + "-" + employeeId,
      currentTitle,
      currentArea,
      currentOffice,
      policyTypeAndName.getPolicy_type(),
      policyTypeAndName.getPolicy_name(),
      value
    };

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private String checkStringNull(String value) {
    if (value == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    } else {
      return value;
    }
  }

  private LocalDate checkLocalDateNull(LocalDate value) {
    if (value == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    } else {
      return value;
    }
  }

  private LocalTime checkLocalTimeNull(LocalTime value) {
    if (value == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    } else {
      return value;
    }
  }

  private String[] getKeyInDescription(String description) {
    return StringUtils.substringsBetween(description, "[", "]");
  }

  @Override
  public List<RequestTypeDto> getAllRequestTypeByEmployeeLevel(String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
    }
    List<RequestTypeDto> requestTypeDtoList =
        applicationsRequestRepository.getAllRequestTypeByEmployeeLevel(employeeId);
    requestTypeDtoList.forEach(
        requestTypeDto -> {
          requestTypeDto.setRequest_type_name(
              ERequestType.getLabel(requestTypeDto.getRequest_type_name()));
        });

    return requestTypeDtoList;
  }

  @Override
  public void createApproveTaxEnrollment(List<String> taxNameList, String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
    }
    if (taxNameList == null || taxNameList.get(0) == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }

    EmployeeTaxDto employeeTaxDto;
    for (String taxName : taxNameList) {
      employeeTaxDto = new EmployeeTaxDto();
      employeeTaxDto.setTaxTypeID(policyRepository.getTaxPolicyTypeIDByTaxName(taxName));
      employeeTaxDto.setEmployeeID(employeeId);
      employeeTaxDto.setTaxStatus(true);
      employeeTaxDto.setTaxTypeName(taxName);
      System.out.println(employeeTaxDto);
      //      applicationsRequestRepository.createApproveTaxEnrollment(employeeTaxDto);
    }
  }
}