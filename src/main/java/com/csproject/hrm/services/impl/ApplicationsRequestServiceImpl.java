package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.common.excel.ExcelExportApplicationRequest;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.*;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.ApplicationsRequestService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class ApplicationsRequestServiceImpl implements ApplicationsRequestService {

  @Autowired EmployeeRepository employeeRepository;
  @Autowired ApplicationsRequestRepository applicationsRequestRepository;
  @Autowired EmployeeDetailRepository employeeDetailRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired WorkingPlaceRepository workingPlaceRepository;
  @Autowired SalaryContractRepository salaryContractRepository;
  @Autowired SalaryMonthlyRepository salaryMonthlyRepository;
  @Autowired BonusSalaryRepository bonusSalaryRepository;
  @Autowired DeductionSalaryRepository deductionSalaryRepository;
  @Autowired AdvanceSalaryRepository advanceSalaryRepository;
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
  public void updateCheckedApplicationRequest(
      UpdateApplicationRequestRequest updateApplicationRequestRequest, String employeeId) {
    if (updateApplicationRequestRequest.getApplicationRequestId() == null
        || updateApplicationRequestRequest.getRequestStatus() == null
        || updateApplicationRequestRequest.getApproverId() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    LocalDateTime latestDate = LocalDateTime.now();
    applicationsRequestRepository.updateCheckedApplicationRequest(
        updateApplicationRequestRequest, employeeId, latestDate);
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
  public void updateApproveApplicationRequest(Long requestId) {
    Optional<ApplicationRequestDto> applicationRequestDto =
        applicationsRequestRepository.getApplicationRequestDtoByRequestId(requestId);
    if (applicationRequestDto.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    }
    updateApproveInformation(applicationRequestDto.get(), requestId);
  }

  @Override
  public void exportApplicationRequestReceiveByExcel(
      HttpServletResponse response, QueryParam queryParam, String employeeId, List<Long> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      try {
        List<ApplicationsRequestResponse> applicationsRequestResponseList =
            applicationsRequestRepository.getListApplicationRequestReceiveByListId(
                queryParam, employeeId, list);
        ExcelExportApplicationRequest excelExportApplicationRequest =
            new ExcelExportApplicationRequest(applicationsRequestResponseList);
        excelExportApplicationRequest.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void exportApplicationRequestSendByExcel(
      HttpServletResponse response, QueryParam queryParam, String employeeId, List<Long> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      try {
        List<ApplicationsRequestResponse> applicationsRequestResponseList =
            applicationsRequestRepository.getListApplicationRequestSendByListId(
                queryParam, employeeId, list);
        ExcelExportApplicationRequest excelExportApplicationRequest =
            new ExcelExportApplicationRequest(applicationsRequestResponseList);
        excelExportApplicationRequest.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void exportApplicationRequestReceiveToCsv(
      Writer writer, QueryParam queryParam, String employeeId, List<Long> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<ApplicationsRequestResponse> applicationsRequestResponseList =
          applicationsRequestRepository.getListApplicationRequestReceiveByListId(
              queryParam, employeeId, list);
      try (CSVPrinter csvPrinter =
          new CSVPrinter(
              writer,
              CSVFormat.DEFAULT.withHeader(
                  "Employee Id",
                  "Full Name",
                  "Create Date",
                  "Request Title",
                  "Description",
                  "Request Status",
                  "Latest Change",
                  "Duration",
                  "Approver",
                  "Checked By"))) {

        for (ApplicationsRequestResponse applicationsRequestResponse :
            applicationsRequestResponseList) {
          String checkBy = null;
          for (int i = 0; i < applicationsRequestResponse.getChecked_by().size(); i++) {
            if (i == applicationsRequestResponse.getChecked_by().size() - 1) {
              checkBy += applicationsRequestResponse.getChecked_by().get(i);
            }
            checkBy += applicationsRequestResponse.getChecked_by().get(i) + ", ";
          }
          csvPrinter.printRecord(
              applicationsRequestResponse.getEmployee_id(),
              applicationsRequestResponse.getFull_name(),
              applicationsRequestResponse.getCreate_date(),
              applicationsRequestResponse.getRequest_title(),
              applicationsRequestResponse.getDescription(),
              applicationsRequestResponse.getRequest_status(),
              applicationsRequestResponse.getChange_status_time(),
              applicationsRequestResponse.getDuration(),
              applicationsRequestResponse.getApprover(),
              checkBy);
        }
        csvPrinter.flush();
      } catch (IOException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, CAN_NOT_WRITE_CSV);
      }
    }
  }

  @Override
  public void exportApplicationRequestSendToCsv(
      Writer writer, QueryParam queryParam, String employeeId, List<Long> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<ApplicationsRequestResponse> applicationsRequestResponseList =
          applicationsRequestRepository.getListApplicationRequestSendByListId(
              queryParam, employeeId, list);
      try (CSVPrinter csvPrinter =
          new CSVPrinter(
              writer,
              CSVFormat.DEFAULT.withHeader(
                  "Employee Id",
                  "Full Name",
                  "Create Date",
                  "Request Title",
                  "Description",
                  "Request Status",
                  "Latest Change",
                  "Duration",
                  "Approver",
                  "Checked By"))) {

        for (ApplicationsRequestResponse applicationsRequestResponse :
            applicationsRequestResponseList) {
          String checkBy = null;
          for (int i = 0; i < applicationsRequestResponse.getChecked_by().size(); i++) {
            if (i == applicationsRequestResponse.getChecked_by().size() - 1) {
              checkBy += applicationsRequestResponse.getChecked_by().get(i);
            }
            checkBy += applicationsRequestResponse.getChecked_by().get(i) + ", ";
          }
          csvPrinter.printRecord(
              applicationsRequestResponse.getEmployee_id(),
              applicationsRequestResponse.getFull_name(),
              applicationsRequestResponse.getCreate_date(),
              applicationsRequestResponse.getRequest_title(),
              applicationsRequestResponse.getDescription(),
              applicationsRequestResponse.getRequest_status(),
              applicationsRequestResponse.getChange_status_time(),
              applicationsRequestResponse.getDuration(),
              applicationsRequestResponse.getApprover(),
              checkBy);
        }
        csvPrinter.flush();
      } catch (IOException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, CAN_NOT_WRITE_CSV);
      }
    }
  }

  private void updateApproveInformation(ApplicationRequestDto requestDto, Long requestId) {
    Set<Map.Entry<String, String>> hashMap = splitData(requestDto.getData()).entrySet();
    String requestName = requestDto.getRequestName();
    String employeeId = requestDto.getEmployeeId();
    LocalDate currentDate = LocalDate.now();
    LocalDate startDate = null, endDate = null, date = null;
    LocalTime startTime = null, endTime = null;
    Long overtimeType = null,
        deductionType = null,
        bonusType = null,
        desiredPosition = null,
        desiredArea = null,
        desiredOffice = null,
        desiredGrade = null,
        salaryId = null;
    BigDecimal value = BigDecimal.ZERO;
    String description = null;
    if (employeeId == null || requestName == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data in request");
    }
    for (Map.Entry<String, String> i : hashMap) {
      switch (i.getKey()) {
        case "Start_Date":
          startDate = LocalDate.parse(i.getValue());
          break;
        case "End_Date":
          endDate = LocalDate.parse(i.getValue());
          break;
        case "Start_Time":
          startTime = LocalTime.parse(i.getValue());
          break;
        case "End_Time":
          endTime = LocalTime.parse(i.getValue());
          break;
        case "Date":
          date = LocalDate.parse(i.getValue());
          break;
        case "Overtime_Type":
          overtimeType = Long.parseLong(i.getValue());
          break;
        case "Desired_Position":
          desiredPosition = Long.parseLong(i.getValue());
          break;
        case "Desired_Area":
          desiredArea = Long.parseLong(i.getValue());
          break;
        case "Desired_Office":
          desiredOffice = Long.parseLong(i.getValue());
          break;
        case "Desired_Grade":
          desiredGrade = Long.parseLong(i.getValue());
          break;
        case "Value":
          value = BigDecimal.valueOf(Long.parseLong(i.getValue()));
          break;
        case "Bonus_Type":
          bonusType = Long.parseLong(i.getValue());
          break;
        case "Deduction_Type":
          deductionType = Long.parseLong(i.getValue());
          break;
        case "Description":
          description = i.getValue();
          break;
      }
    }
    switch (requestName) {
      case "LEAVE_SOON":
      case "WORK_LATE":
        updateWorkingTime(date, employeeId, requestName, requestId);
        break;
      case "OVERTIME":
        updateOvertime(
            startDate,
            endDate,
            startTime,
            endTime,
            overtimeType,
            currentDate,
            employeeId,
            requestName,
            requestId);
        break;
      case "PAID_LEAVE":
        updatePaidLeave(startDate, endDate, employeeId, requestName, requestId);
        break;
      case "PROMOTION":
        updatePromotion(
            desiredArea,
            desiredOffice,
            desiredPosition,
            desiredGrade,
            startDate,
            value,
            employeeId,
            requestId);
        break;
      case "SALARY_INCREMENT":
        updateSalaryIncrement(startDate, value, employeeId, requestId);
        break;
      case "BONUS":
        updateBonusSalary(date, description, value, employeeId, salaryId, bonusType, requestId);
        break;
      case "CONFLICT_CUSTOMER":
      case "LEAK_INFORMATION":
        updateConflictAndLeakInfo(
            date, description, value, employeeId, salaryId, deductionType, requestId);
        break;
      case "ADVANCE":
        updateAdvanceRequest(date, description, value, employeeId, salaryId, requestId);
        break;
    }
  }

  private void updateWorkingTime(
      LocalDate date, String employeeId, String requestName, Long requestId) {
    if (date == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    timekeepingRepository.deleteTimekeepingStatusByEmployeeIdAndDate(
        employeeId, date, ETimekeepingStatus.getValue(requestName));

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private void updateOvertime(
      LocalDate startDate,
      LocalDate endDate,
      LocalTime startTime,
      LocalTime endTime,
      Long overtimeType,
      LocalDate currentDate,
      String employeeId,
      String requestName,
      Long requestId) {
    if (startDate == null
        || endDate == null
        || startTime == null
        || endTime == null
        || overtimeType == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    if (endDate.isAfter(currentDate)) {
      timekeepingRepository.insertTimekeepingByEmployeeId(employeeId, currentDate, endDate);
    }
    timekeepingRepository.upsertTimekeepingStatusByEmployeeIdAndRangeDate(
        employeeId,
        startDate,
        endDate,
        ETimekeepingStatus.getValue(requestName),
        ETimekeepingStatus.getValue(requestName));

    timekeepingRepository.insertOvertimeByEmployeeIdAndRangeDate(
        employeeId, startDate, endDate, startTime, endTime, overtimeType);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private void updatePaidLeave(
      LocalDate startDate,
      LocalDate endDate,
      String employeeId,
      String requestName,
      Long requestId) {
    if (startDate == null || endDate == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    timekeepingRepository.upsertTimekeepingStatusByEmployeeIdAndRangeDate(
        employeeId,
        startDate,
        endDate,
        ETimekeepingStatus.getValue("DAY_OFF"),
        ETimekeepingStatus.getValue(requestName));

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private void updatePromotion(
      Long desiredArea,
      Long desiredOffice,
      Long desiredPosition,
      Long desiredGrade,
      LocalDate startDate,
      BigDecimal value,
      String employeeId,
      Long requestId) {
    if (desiredArea == null
        || desiredOffice == null
        || desiredPosition == null
        || desiredGrade == null
        || startDate == null
        || value == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    workingPlaceRepository.insertNewWorkingPlace(
        employeeId,
        desiredArea,
        desiredOffice,
        desiredGrade,
        desiredPosition,
        startDate,
        false,
        true);
    salaryContractRepository.insertNewSalaryContract(employeeId, value, startDate, false, true);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private void updateSalaryIncrement(
      LocalDate startDate, BigDecimal value, String employeeId, Long requestId) {
    if (startDate == null || value == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryContractRepository.insertNewSalaryContract(employeeId, value, startDate, false, true);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private void updateBonusSalary(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long salaryId,
      Long bonusType,
      Long requestId) {
    if (date == null
        || description == null
        || bonusType == null
        || value == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryId = salaryMonthlyRepository.getSalaryIdByEmployeeIdAndDate(employeeId, date);
    bonusSalaryRepository.insertBonusSalaryByEmployeeId(
        salaryId, date, description, bonusType, value);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private void updateConflictAndLeakInfo(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long salaryId,
      Long deductionType,
      Long requestId) {
    if (date == null
        || description == null
        || deductionType == null
        || value == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryId = salaryMonthlyRepository.getSalaryIdByEmployeeIdAndDate(employeeId, date);
    deductionSalaryRepository.insertDeductionSalaryByEmployeeId(
        salaryId, date, description, deductionType, value);
    if (deductionType.equals(EDeduction.getValue("FIRE")))
      employeeRepository.updateStatusEmployee(employeeId, false);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private void updateAdvanceRequest(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long salaryId,
      Long requestId) {
    if (date == null || description == null || value == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryId = salaryMonthlyRepository.getSalaryIdByEmployeeIdAndDate(employeeId, date);
    advanceSalaryRepository.insertAdvanceSalaryByEmployeeId(salaryId, date, description, value);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name());
  }

  private HashMap<String, String> splitData(String data) {
    HashMap<String, String> hashMap = new HashMap<>();
    if (!isBlank(data)) {
      String[] splitBracket = StringUtils.substringsBetween(data, "[", "]");
      for (String split : splitBracket) {
        String[] splitSeparator = split.split(SEPARATOR, TWO_NUMBER);
        if (isInvalidSplit(splitSeparator)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
        hashMap.put(splitSeparator[ZERO_NUMBER], splitSeparator[ONE_NUMBER]);
      }
    } else {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    return hashMap;
  }

  private boolean isInvalidSplit(String[] split) {
    return split.length != TWO_NUMBER || isBlank(split[ZERO_NUMBER]) || isBlank(split[ONE_NUMBER]);
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
    applicationsRequest.setCreateDate(LocalDateTime.now());
    applicationsRequest.setLatestDate(LocalDateTime.now());
    applicationsRequest.setDuration(LocalDateTime.now().plusDays(3));
    applicationsRequest.setIsBookmark(false);
    applicationsRequest.setIsRemind(false);
    applicationsRequest.setIsRead(false);

    applicationsRequestRepository.createApplicationsRequest(applicationsRequest);

    generalFunction.sendEmailCreateRequest(
        createEmployeeId, employeeId, FROM_EMAIL, "hihihd37@gmail.com", "New request");
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