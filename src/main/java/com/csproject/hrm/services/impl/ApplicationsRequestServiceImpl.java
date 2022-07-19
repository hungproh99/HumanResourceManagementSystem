package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.common.excel.ExcelExportApplicationRequest;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.ApplicationsRequestRequestC;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ApplicationRequestRemindResponse;
import com.csproject.hrm.dto.response.ApplicationsRequestResponse;
import com.csproject.hrm.dto.response.ListApplicationsRequestResponse;
import com.csproject.hrm.dto.response.PolicyTypeAndNameResponse;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.ApplicationsRequestService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.csproject.hrm.common.constant.Constants.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
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
  @Autowired SalaryCalculator salaryCalculator;

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
    if (requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    }
    boolean isRead = false;
    applicationsRequestRepository.changeIsRead(isRead, requestId);
  }

  @Override
  public void updateApproveApplicationRequest(Long requestId) {
    if (requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    }
    Optional<ApplicationRequestDto> applicationRequestDto =
        applicationsRequestRepository.getApplicationRequestDtoByRequestId(requestId);
    if (applicationRequestDto.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    }
    updateApproveInformation(applicationRequestDto.get(), requestId);
  }

  @Override
  public void updateRejectApplicationRequest(Long requestId) {
    if (requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    }
    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.REJECTED.name(), LocalDateTime.now());
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
                  "Request Type",
                  "Request Name",
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
              applicationsRequestResponse.getRequest_type(),
              applicationsRequestResponse.getRequest_name(),
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
                  "Request Type",
                  "Request Name",
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
              applicationsRequestResponse.getRequest_type(),
              applicationsRequestResponse.getRequest_name(),
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
    Set<Map.Entry<String, String>> hashMap =
        generalFunction.splitData(requestDto.getData()).entrySet();
    String requestName = requestDto.getRequestName();
    String employeeId = requestDto.getEmployeeId();
    LocalDate currentDate = LocalDate.now();
    LocalDate startDate = null, endDate = null, date = null;
    LocalTime startTime = null, endTime = null;
    Long deductionType = null,
        bonusType = null,
        desiredPosition = null,
        desiredArea = null,
        desiredOffice = null,
        desiredGrade = null;
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
        updateBonusSalary(date, description, value, employeeId, bonusType, requestId);
        break;
      case "CONFLICT_CUSTOMER":
      case "LEAK_INFORMATION":
        updateConflictAndLeakInfo(date, description, value, employeeId, deductionType, requestId);
        break;
      case "ADVANCE":
        updateAdvanceRequest(date, description, value, employeeId, requestId);
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
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private void updateOvertime(
      LocalDate startDate,
      LocalDate endDate,
      LocalTime startTime,
      LocalTime endTime,
      LocalDate currentDate,
      String employeeId,
      String requestName,
      Long requestId) {
    if (startDate == null
        || endDate == null
        || startTime == null
        || endTime == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    List<LocalDate> holidayList = salaryCalculator.getAllHolidayByYear(currentDate);
    List<LocalDate> weekendList = salaryCalculator.getAllWeekendByYear(currentDate);
    boolean isHoliday = false;
    boolean isWeekend = false;
    Long overtimeType = null;
    for (LocalDate date : holidayList) {
      if (currentDate.equals(date)) {
        isHoliday = true;
        break;
      }
    }
    for (LocalDate date : weekendList) {
      if (currentDate.equals(date)) {
        isWeekend = true;
        break;
      }
    }
    if (isHoliday) {
      overtimeType = EOvertime.getValue(EOvertime.HOLIDAY.name());
    } else if (isWeekend) {
      overtimeType = EOvertime.getValue(EOvertime.WEEKEND.name());
    } else {
      overtimeType = EOvertime.getValue(EOvertime.IN_WEEK.name());
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
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
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
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
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
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private void updateSalaryIncrement(
      LocalDate startDate, BigDecimal value, String employeeId, Long requestId) {
    if (startDate == null || value == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryContractRepository.insertNewSalaryContract(employeeId, value, startDate, false, true);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private void updateBonusSalary(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long bonusType,
      Long requestId) {
    if (date == null
        || description == null
        || bonusType == null
        || value == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    LocalDate startDate = date.with(firstDayOfMonth());
    LocalDate lastDate = date.with(lastDayOfMonth());
    Long salaryId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, lastDate, ESalaryMonthly.PENDING.name());
    bonusSalaryRepository.insertBonusSalaryByEmployeeId(
        salaryId, date, description, bonusType, value);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private void updateConflictAndLeakInfo(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long deductionType,
      Long requestId) {
    if (date == null
        || description == null
        || deductionType == null
        || value == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    LocalDate startDate = date.with(firstDayOfMonth());
    LocalDate lastDate = date.with(lastDayOfMonth());
    Long salaryId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, lastDate, ESalaryMonthly.PENDING.name());
    deductionSalaryRepository.insertDeductionSalaryByEmployeeId(
        salaryId, date, description, deductionType, value);
    if (deductionType.equals(EDeduction.getValue("FIRE")))
      employeeRepository.updateStatusEmployee(employeeId, false);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private void updateAdvanceRequest(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long requestId) {
    if (date == null || description == null || value == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    LocalDate startDate = date.with(firstDayOfMonth());
    LocalDate lastDate = date.with(lastDayOfMonth());
    Long salaryId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, lastDate, ESalaryMonthly.PENDING.name());
    advanceSalaryRepository.insertAdvanceSalaryByEmployeeId(salaryId, date, description, value);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private boolean isInvalidSplit(String[] split) {
    return split.length != TWO_NUMBER || isBlank(split[ZERO_NUMBER]) || isBlank(split[ONE_NUMBER]);
  }

  @Override
  @Transactional
  public void createApplicationsRequest(ApplicationsRequestRequestC applicationsRequest) {
    String createEmployeeId = applicationsRequest.getCreateEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(createEmployeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + createEmployeeId);
    }
    Long requestTypeId = applicationsRequest.getRequestTypeId();
    Long requestNameId = applicationsRequest.getRequestNameId();
    if (requestTypeId == null || requestNameId == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    if (!applicationsRequestRepository.getAllRequestNameByRequestTypeID(requestTypeId).stream()
        .anyMatch(requestNameDto -> requestNameDto.getRequest_name_id().equals(requestNameId))) {
      throw new CustomDataNotFoundException("Request Type or Request Name does not existed!");
    }
    if (!applicationsRequestRepository.checkPermissionToCreate(createEmployeeId, requestNameId)) {
      throw new CustomErrorException("You don't have permission to create this request!");
    }

    String approver =
        Objects.requireNonNullElse(
            employeeDetailRepository.getManagerByEmployeeID(createEmployeeId),
            "Nguyen Quang Huy - huynq100");

    applicationsRequest.setApprover(approver);

    switch (requestTypeId.intValue()) {
      case 1:
        {
          switch (requestNameId.intValue()) {
            case 1:
            case 14:
              {
                applicationsRequest = createRequestForWorkingTime(applicationsRequest);
                break;
              }
            case 2:
              {
                applicationsRequest = createRequestForWorkingScheduleAndOT(applicationsRequest);
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
                if (getPaidLeaveDayRemaining(applicationsRequest.getCreateEmployeeId()) < 1) {
                  throw new CustomErrorException("Your paid leave remaining is over!");
                }
                applicationsRequest = createRequestForPairLeave(applicationsRequest);
                break;
              }
          }
          break;
        }
      case 3:
        {
          switch (requestNameId.intValue()) {
            case 3:
              {
                applicationsRequest = createRequestForNominationAndPromotion(applicationsRequest);
                break;
              }
            case 4:
              {
                checkLevelAndValueToApprove(applicationsRequest, "salary");
                applicationsRequest =
                    createRequestForNominationAndSalaryIncreaseOrBonus(applicationsRequest);
                break;
              }
            case 5:
              {
                checkLevelAndValueToApprove(applicationsRequest, "bonus");
                applicationsRequest =
                    createRequestForNominationAndSalaryIncreaseOrBonus(applicationsRequest);
                break;
              }
          }
          break;
        }
      case 4:
      case 5:
      case 6:
        {
          applicationsRequest = createRequestForPenalise(applicationsRequest);
          break;
        }
      case 7:
        {
          switch (requestNameId.intValue()) {
            case 12:
              {
                applicationsRequest = createRequestForAdvances(applicationsRequest);
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
                applicationsRequest = createRequestForTaxEnrollment(applicationsRequest);
                break;
              }
          }
          break;
        }
    }

    applicationsRequest.setApprover(applicationsRequest.getApprover().split("-")[1].trim());

    applicationsRequest.setRequestStatusId(1L);
    applicationsRequest.setCreateDate(LocalDateTime.now());
    applicationsRequest.setLatestDate(LocalDateTime.now());
    applicationsRequest.setDuration(LocalDateTime.now().plusDays(3));
    applicationsRequest.setIsBookmark(false);
    applicationsRequest.setIsRemind(false);
    applicationsRequest.setIsRead(false);

    applicationsRequestRepository.createApplicationsRequest(applicationsRequest);

    generalFunction.sendEmailCreateRequest(
        createEmployeeId, approver, FROM_EMAIL, "hihihd37@gmail.com", "New request");
  }

  private void checkLevelAndValueToApprove(
      ApplicationsRequestRequestC applicationsRequest, String type) {
    String data =
        applicationsRequestRepository.getDataOfPolicy(applicationsRequest.getRequestNameId());
    String[] splitData = getKeyInDescription(data);

    String employeeId = applicationsRequest.getCreateEmployeeId().trim();
    long value = Long.parseLong(applicationsRequest.getValue().trim());
    int level = employeeDetailRepository.getLevelByEmployeeID(employeeId);

    for (String data1 : splitData) {
      if (data1.contains(type)) {
        String[] data2 = getBonusAndSalary(data1);
        switch (data2[1].length()) {
          case 1:
            {
              if (Integer.parseInt(data2[1]) >= level) {
                if (value <= Long.parseLong(data2[2])) {
                  return;
                } else {
                  throw new CustomErrorException(
                      "Please input " + type + " value under " + data2[2] + "!");
                }
              }
              break;
            }
          case 2:
            {
              String[] data4 = data2[1].split("");
              if (Integer.parseInt(data4[1]) < level) {
                if (value <= Long.parseLong(data2[2])) {
                  return;
                } else {
                  throw new CustomErrorException(
                      "Please input " + type + " value under " + data2[2] + "!");
                }
              }
              break;
            }
          case 3:
            {
              String[] data4 = data2[1].split("");
              if ((Integer.parseInt(data4[0]) <= level && Integer.parseInt(data4[2]) >= level)) {
                if (value <= Long.parseLong(data2[2])) {
                  return;
                } else {
                  throw new CustomErrorException(
                      "Please input " + type + " value under " + data2[2] + "!");
                }
              }
              break;
            }
          default:
            throw new CustomErrorException("You don't have permission to create request!");
        }
      }
    }
    throw new CustomErrorException("You don't have permission to create request!");
  }

  private ApplicationsRequestRequestC setDescriptionAndData(
      ApplicationsRequestRequestC applicationsRequest, String[] valueArray) {
    String data = "";
    String description =
        applicationsRequestRepository.getDescriptionByRequestNameID(
            applicationsRequest.getRequestNameId());
    String[] keyArray = getKeyInDescription(description);
    for (int i = 0; i < keyArray.length; i++) {
      description = description.replaceAll("\\[" + keyArray[i] + "\\]", "[" + valueArray[i] + "]");
    }
    for (int i = 1; i < keyArray.length - 1; i++) {
      data = data + "[" + keyArray[i] + "|" + valueArray[i] + "]";
    }

    System.out.println(description);
    System.out.println(data);

    applicationsRequest.setData(data);
    applicationsRequest.setDescription(
        description + "P/S: " + applicationsRequest.getDescription());
    return applicationsRequest;
  }

  private ApplicationsRequestRequestC createRequestForWorkingTime(
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    String date = checkLocalDateNull(applicationsRequest.getDate()).toString();
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    String[] valueArray = {approver, date, employee};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForWorkingScheduleAndOT(
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    String startTime = checkLocalTimeNull(applicationsRequest.getStartTime()).toString();
    String endTime = checkLocalTimeNull(applicationsRequest.getEndTime()).toString();
    String startDate = checkLocalDateNull(applicationsRequest.getStartDate()).toString();
    String endDate = checkLocalDateNull(applicationsRequest.getEndDate()).toString();
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    if (applicationsRequest.getStartDate().isAfter(applicationsRequest.getEndDate())) {
      throw new CustomErrorException("Please choose end date after start date!");
    }
    if (applicationsRequest.getStartTime().isAfter(applicationsRequest.getEndTime())) {
      throw new CustomErrorException("Please choose end time after start time!");
    }

    String[] valueArray = {approver, startDate, endDate, startTime, endTime, employee};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForPairLeave(
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    String startDate = checkLocalDateNull(applicationsRequest.getStartDate()).toString();
    String endDate = checkLocalDateNull(applicationsRequest.getEndDate()).toString();
    String reason = checkStringNull(applicationsRequest.getReason());
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    if (applicationsRequest.getStartDate().isAfter(applicationsRequest.getEndDate())) {
      throw new CustomErrorException("Please choose end date after start date!");
    }

    String[] valueArray = {approver, startDate, endDate, reason, employee};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForAdvances(
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    String value = checkStringNull(applicationsRequest.getValue());
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    String[] valueArray = {approver, value, employee};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForTaxEnrollment(
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    List<String> taxTypes = applicationsRequest.getTaxType();
    String taxType = StringUtils.join(taxTypes, ",");
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    String[] valueArray = {approver, taxType, employee};

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForNominationAndPromotion(
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();

    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
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
      employeeName + " - " + employeeId,
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
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();

    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
    }

    String employeeName = checkStringNull(applicationsRequest.getEmployeeName());
    String currentTitle = checkStringNull(applicationsRequest.getCurrentTitle());
    String currentArea = checkStringNull(applicationsRequest.getCurrentArea());
    String value = checkStringNull(applicationsRequest.getValue());
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    String type = checkStringNull(applicationsRequest.getType());

    String[] valueArray = {
      approver, employeeName + " - " + employeeId, currentTitle, currentArea, value, type, employee
    };

    return setDescriptionAndData(applicationsRequest, valueArray);
  }

  private ApplicationsRequestRequestC createRequestForPenalise(
      ApplicationsRequestRequestC applicationsRequest) {
    String approver = applicationsRequest.getApprover();

    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
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
      employeeName + " - " + employeeId,
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

  @Override
  public int getPaidLeaveDayRemaining(String employeeId) {
    int current = timekeepingRepository.countPaidLeaveOfEmployeeByYear(employeeId);

    String data = applicationsRequestRepository.getDataOfPolicy(6L);
    String[] splitData = getKeyInDescription(data);

    return Integer.parseInt(splitData[0].split("\\|")[1]) - current;
  }

  @Override
  public int getOTDTimeRemaining(String employeeId) {
    //    int current = timekeepingRepository.countOvertimeOfEmployeeByYear(employeeId);
    //
    //    String data = applicationsRequestRepository.getDataOfPolicy(2L);
    //    String[] splitData = getKeyInDescription(data);
    //
    //    return Integer.parseInt(splitData[1].split("\\|")[1]) - current;
    return 0;
  }

  private String[] getKeyInDescription(String description) {
    return StringUtils.substringsBetween(description, "[", "]");
  }

  private String[] getBonusAndSalary(String data) {
    return data.split("\\|");
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

  @Override
  public void updateAllApplicationRequestRemind(LocalDateTime checkDate) {
    List<ApplicationRequestRemindResponse> applicationRequestRemindResponses =
        applicationsRequestRepository.getAllApplicationRequestToRemind(checkDate);

    applicationRequestRemindResponses.forEach(
        applicationRequestRemindResponse -> {
          String createName =
              applicationRequestRemindResponse.getFull_name()
                  + " "
                  + applicationRequestRemindResponse.getEmployee_id();

          String approveName =
              employeeRepository.getEmployeeNameByEmployeeId(
                  applicationRequestRemindResponse.getApprover());

          String approveEmail =
              employeeRepository.getEmployeeEmailByEmployeeId(
                  applicationRequestRemindResponse.getApprover());

          generalFunction.sendEmailRemindRequest(
              approveName,
              createName,
              applicationRequestRemindResponse.getCreate_date().toString(),
              applicationRequestRemindResponse.getChecked_by(),
              applicationRequestRemindResponse.getApplication_request_id().toString(),
              FROM_EMAIL,
              TO_EMAIL,
              "Remind Request");

          applicationsRequestRepository.updateAllApplicationRequestRemind(
              applicationRequestRemindResponse.getApplication_request_id(), Boolean.TRUE);
        });
  }
}