package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.common.excel.ExcelExportApplicationRequest;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.common.general.SalaryCalculator;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.csproject.hrm.common.constant.Constants.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

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
  @Autowired WorkingContractRepository workingContractRepository;
  @Autowired GeneralFunction generalFunction;
  @Autowired SalaryCalculator salaryCalculator;
  @Autowired ChartRepository chartRepository;
  @Autowired RequestStatusRepository requestStatusRepository;
  @Autowired RequestNameRepository requestNameRepository;

  @Override
  public ListApplicationsRequestResponse getAllApplicationRequestReceive(
      QueryParam queryParam, String employeeId) {
    if (employeeRepository.findById(employeeId).isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee Id \"" + employeeId + "\" not exist");
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
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee Id \"" + employeeId + "\" not exist");
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
          HttpStatus.BAD_REQUEST,
          "Employee Id \"" + applicationsRequest.getEmployeeId() + "\" not exist");
    } else if (employeeRepository.findById(applicationsRequest.getApprover()).isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Approver \"" + applicationsRequest.getApprover() + "\" not exist");
    } else if (!requestStatusRepository.existsById(applicationsRequest.getRequestStatusId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Request Status \"" + applicationsRequest.getRequestStatusId() + "\" not exist");
    } else if (!requestNameRepository.existsById(applicationsRequest.getRequestNameId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Request Name \"" + applicationsRequest.getRequestNameId() + "\" not exist");
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
    if (!applicationsRequestRepository.checkExistRequestId(
        updateApplicationRequestRequest.getApplicationRequestId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Request Id \""
              + updateApplicationRequestRequest.getApplicationRequestId()
              + "\" not exist!");
    } else if (!employeeDetailRepository.checkEmployeeIDIsExists(
        updateApplicationRequestRequest.getApproverId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Approver Id \"" + updateApplicationRequestRequest.getApproverId() + "\" not exist!");
    } else if (!requestStatusRepository.existsById(
        ERequestStatus.getValue(updateApplicationRequestRequest.getRequestStatus()))) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Request Status \""
              + updateApplicationRequestRequest.getRequestStatus()
              + "\" not exist!");
    }
    LocalDateTime latestDate = LocalDateTime.now();
    applicationsRequestRepository.updateCheckedApplicationRequest(
        updateApplicationRequestRequest, employeeId, Boolean.FALSE, latestDate);
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

  //  @Override
  //  public void updateIsRead(Long requestId) {
  //    if (requestId == null) {
  //      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
  //    }
  //    boolean isRead = false;
  //    applicationsRequestRepository.changeIsRead(isRead, requestId);
  //  }

  @Override
  public void updateApproveApplicationRequest(Long requestId) {
    if (requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    } else if (!applicationsRequestRepository.checkExistRequestId(requestId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Request Id \"" + requestId + "\"  not exist");
    } else if (applicationsRequestRepository.checkAlreadyApproveOrReject(requestId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Request Id \"" + requestId + "\"  already approve or reject");
    }
    Optional<ApplicationRequestDto> applicationRequestDto =
        applicationsRequestRepository.getApplicationRequestDtoByRequestId(requestId);
    if (applicationRequestDto.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    }
    updateApproveInformation(applicationRequestDto.get(), requestId);
  }

  @Override
  public void updateRejectApplicationRequest(
      RejectApplicationRequestRequest rejectApplicationRequestRequest) {
    if (!applicationsRequestRepository.checkExistRequestId(
        rejectApplicationRequestRequest.getRequestId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Request Id \"" + rejectApplicationRequestRequest.getRequestId() + "\" not exist");
    } else if (applicationsRequestRepository.checkAlreadyApproveOrReject(
        rejectApplicationRequestRequest.getRequestId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Request Id \""
              + rejectApplicationRequestRequest.getRequestId()
              + "\"  already approve or reject");
    }
    Optional<ApplicationRequestDto> applicationRequestDto =
        applicationsRequestRepository.getApplicationRequestDtoByRequestId(
            rejectApplicationRequestRequest.getRequestId());
    if (applicationRequestDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          NO_DATA + "with " + rejectApplicationRequestRequest.getRequestId());
    }
    String employeeId = applicationRequestDto.get().getEmployeeId();
    String employeeName = employeeRepository.getEmployeeNameByEmployeeId(employeeId);
    String approveId = applicationRequestDto.get().getApproveId();
    String approverName = employeeRepository.getEmployeeNameByEmployeeId(approveId);
    String requestTitle =
        applicationRequestDto.get().getRequestType()
            + " - "
            + applicationRequestDto.get().getRequestName();
    String requestStatus = ERequestStatus.getLabel(ERequestStatus.REJECTED.name());
    String createEmail = employeeRepository.getEmployeeEmailByEmployeeId(employeeId);
    applicationsRequestRepository.updateRejectApplicationRequest(
        rejectApplicationRequestRequest, LocalDateTime.now());
    generalFunction.sendEmailUpdateRequest(
        employeeName,
        requestTitle,
        requestStatus,
        applicationRequestDto.get().getComment(),
        applicationRequestDto.get().getLatestDate().toString(),
        approverName,
        applicationRequestDto.get().getRequestId().toString(),
        FROM_EMAIL,
        createEmail,
        "Reject Request");
  }

  @Override
  public void exportApplicationRequestReceiveByExcel(
      HttpServletResponse response, QueryParam queryParam, String employeeId, List<Long> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else if (!employeeRepository.existsById(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee \"" + employeeId + "\" not exist");
    } else {
      try {
        for (Long requestId : list) {
          if (!applicationsRequestRepository.existsById(requestId)) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "Request Id \"" + requestId + "\" not exist");
          }
        }
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
    } else if (!employeeRepository.existsById(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee \"" + employeeId + "\" not exist");
    } else {
      try {
        for (Long requestId : list) {
          if (!applicationsRequestRepository.existsById(requestId)) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "Request Id \"" + requestId + "\" not exist");
          }
        }
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
    } else if (!employeeRepository.existsById(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee \"" + employeeId + "\" not exist");
    } else {
      for (Long requestId : list) {
        if (!applicationsRequestRepository.existsById(requestId)) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "RequestId \"" + requestId + "\" not exist");
        }
      }
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
              applicationsRequestResponse.getRequest_status_name(),
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
    } else if (!employeeRepository.existsById(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee \"" + employeeId + "\" not exist");
    } else {
      for (Long requestId : list) {
        if (!applicationsRequestRepository.existsById(requestId)) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "RequestId \"" + requestId + "\" not exist");
        }
      }
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
              applicationsRequestResponse.getRequest_status_name(),
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
    String employeeName = employeeRepository.getEmployeeNameByEmployeeId(employeeId);
    String approveId = requestDto.getApproveId();
    String approverName = employeeRepository.getEmployeeNameByEmployeeId(approveId);
    String createEmail = employeeRepository.getEmployeeEmailByEmployeeId(employeeId);
    String requestTitle = requestDto.getRequestType() + " - " + requestDto.getRequestName();
    String requestStatus = ERequestStatus.getLabel(ERequestStatus.APPROVED.name());
    LocalDate currentDate = LocalDate.now();
    LocalDate startDate = null, endDate = null, date = null;
    LocalTime startTime = null, endTime = null;
    Long deductionType = null,
        bonusType = null,
        desiredPosition = null,
        desiredArea = null,
        desiredOffice = null,
        desiredGrade = null,
        reason = null;
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
          //        case "Desired_Position":
          //          desiredPosition = Long.parseLong(i.getValue());
          //          break;
          //        case "Desired_Area":
          //          desiredArea = Long.parseLong(i.getValue());
          //          break;
          //        case "Desired_Office":
          //          desiredOffice = Long.parseLong(i.getValue());
          //          break;
          //        case "Desired_Grade":
          //          desiredGrade = Long.parseLong(i.getValue());
          //          break;
        case "Value":
          value = BigDecimal.valueOf(Double.parseDouble(i.getValue()));
          break;
        case "Bonus_Type":
          bonusType = Long.parseLong(i.getValue());
          break;
          //        case "Deduction_Type":
          //          deductionType = Long.parseLong(i.getValue());
          //          break;
        case "Description":
          description = i.getValue();
          break;
        case "Reason":
          reason = Long.parseLong(i.getValue());
          break;
      }
    }
    switch (requestName) {
      case "LEAVE_SOON":
      case "WORK_LATE":
        updateWorkingTime(date, employeeId, requestName, requestId);
        break;
      case "OT":
        updateOvertime(startDate, endDate, startTime, endTime, employeeId, requestId);
        break;
      case "PAID_LEAVE":
        updatePaidLeave(startDate, endDate, employeeId, requestName, requestId, reason);
        break;
        //      case "PROMOTION":
        //        updatePromotion(
        //            desiredArea,
        //            desiredOffice,
        //            desiredPosition,
        //            desiredGrade,
        //            startDate,
        //            value,
        //            employeeId,
        //            requestId);
        //        break;
      case "SALARY_INCREMENT":
        updateSalaryIncrement(startDate, value, employeeId, requestId);
        break;
      case "BONUS":
        updateBonusSalary(date, value, employeeId, bonusType, requestId);
        break;
        //      case "CONFLICT_CUSTOMER":
        //      case "LEAK_INFORMATION":
        //        updateConflictAndLeakInfo(date, description, value, employeeId, deductionType,
        // requestId);
        //        break;
      case "ADVANCES":
        updateAdvanceRequest(date, value, employeeId, requestId);
        break;
    }
    generalFunction.sendEmailUpdateRequest(
        employeeName,
        requestTitle,
        requestStatus,
        requestDto.getComment(),
        requestDto.getLatestDate().toString(),
        approverName,
        requestDto.getRequestId().toString(),
        FROM_EMAIL,
        createEmail,
        "Approve Request");
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
      String employeeId,
      Long requestId) {
    if (startDate == null
        || endDate == null
        || startTime == null
        || endTime == null
        || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    List<LocalDate> holidayList = salaryCalculator.getAllHolidayByRange(startDate, endDate);
    List<LocalDate> weekendList = salaryCalculator.getAllWeekendByRange(startDate, endDate);
    List<TimekeepingIdOvertimeTypeDto> timekeepingIdOvertimeTypeDtoList =
        timekeepingRepository.getListTimekeepingIdOvertimeTypeDto(employeeId, startDate, endDate);

    timekeepingIdOvertimeTypeDtoList.forEach(
        timekeepingIdOvertimeTypeDto -> {
          boolean isHoliday = false;
          boolean isWeekend = false;
          for (LocalDate date : holidayList) {
            if (timekeepingIdOvertimeTypeDto.getCurrDate().equals(date)) {
              isHoliday = true;
              break;
            }
          }
          for (LocalDate date : weekendList) {
            if (timekeepingIdOvertimeTypeDto.getCurrDate().equals(date)) {
              isWeekend = true;
              break;
            }
          }
          if ((isHoliday && !isWeekend) || (isHoliday && isWeekend)) {
            timekeepingIdOvertimeTypeDto.setOtType(EOvertime.getValue(EOvertime.HOLIDAY.name()));
          } else if (isWeekend && !isHoliday) {
            timekeepingIdOvertimeTypeDto.setOtType(EOvertime.getValue(EOvertime.WEEKEND.name()));
          } else {
            timekeepingIdOvertimeTypeDto.setOtType(EOvertime.getValue(EOvertime.IN_WEEK.name()));
          }
        });

    List<LocalDate> localDateList = new ArrayList<>();
    if (startDate.equals(endDate)) {
      localDateList.add(startDate);
    } else {
      List<LocalDate> rangeDates = startDate.datesUntil(endDate).collect(Collectors.toList());
      for (LocalDate date : rangeDates) {
        if (!timekeepingRepository.checkExistDateInTimekeeping(date, employeeId)) {
          localDateList.add(date);
        }
      }
    }
    timekeepingRepository.insertTimekeepingByEmployeeId(employeeId, localDateList);

    timekeepingRepository.upsertTimekeepingStatusByEmployeeIdAndRangeDate(
        employeeId,
        startDate,
        endDate,
        null,
        ETimekeepingStatus.getValue(ETimekeepingStatus.OVERTIME.name()),
        ETimekeepingStatus.getValue(ETimekeepingStatus.OVERTIME.name()));

    timekeepingRepository.insertOvertimeByEmployeeIdAndRangeDate(
        timekeepingIdOvertimeTypeDtoList, startTime, endTime);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private void updatePaidLeave(
      LocalDate startDate,
      LocalDate endDate,
      String employeeId,
      String requestName,
      Long requestId,
      Long reason) {
    if (startDate == null || endDate == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    List<LocalDate> localDateList = new ArrayList<>();
    if (startDate.equals(endDate)) {
      localDateList.add(startDate);
    } else {
      List<LocalDate> rangeDates = startDate.datesUntil(endDate).collect(Collectors.toList());
      rangeDates.add(endDate);
      for (LocalDate date : rangeDates) {
        if (!timekeepingRepository.checkExistDateInTimekeeping(date, employeeId)) {
          localDateList.add(date);
        }
      }
    }
    timekeepingRepository.insertTimekeepingByEmployeeId(employeeId, localDateList);

    timekeepingRepository.upsertTimekeepingStatusByEmployeeIdAndRangeDate(
        employeeId,
        startDate,
        endDate,
        reason,
        ETimekeepingStatus.getValue("DAY_OFF"),
        ETimekeepingStatus.getValue(requestName));

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  //  private void updatePromotion(
  //      Long desiredArea,
  //      Long desiredOffice,
  //      Long desiredPosition,
  //      Long desiredGrade,
  //      LocalDate startDate,
  //      BigDecimal value,
  //      String employeeId,
  //      Long requestId) {
  //    if (desiredArea == null
  //        || desiredOffice == null
  //        || desiredPosition == null
  //        || desiredGrade == null
  //        || startDate == null
  //        || value == null
  //        || requestId == null) {
  //      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
  //    }
  //    workingPlaceRepository.insertNewWorkingPlace(
  //        employeeId,
  //        desiredArea,
  //        desiredOffice,
  //        desiredGrade,
  //        desiredPosition,
  //        startDate,
  //        false,
  //        true);
  //    salaryContractRepository.insertNewSalaryContract(employeeId, value, startDate, false, true);
  //
  //    applicationsRequestRepository.updateStatusApplication(
  //        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  //  }

  private void updateSalaryIncrement(
      LocalDate startDate, BigDecimal value, String employeeId, Long requestId) {
    if (startDate == null || value == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryContractRepository.insertNewSalaryContractByIncreaseSalary(
        employeeId, value, startDate, false);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  private void updateBonusSalary(
      LocalDate date, BigDecimal value, String employeeId, Long bonusType, Long requestId) {
    if (date == null || bonusType == null || value == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    LocalDate startDate = date.with(firstDayOfMonth());
    LocalDate lastDate = date.with(lastDayOfMonth());
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(
            startDate, lastDate, employeeId);
    actualWorkingPoint = actualWorkingPoint != null ? actualWorkingPoint : 0D;
    Long salaryId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, lastDate, actualWorkingPoint, ESalaryMonthly.PENDING.name());
    bonusSalaryRepository.insertBonusSalaryByEmployeeId(salaryId, date, bonusType, value);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  //  private void updateConflictAndLeakInfo(
  //      LocalDate date,
  //      String description,
  //      BigDecimal value,
  //      String employeeId,
  //      Long deductionType,
  //      Long requestId) {
  //    if (date == null
  //        || description == null
  //        || deductionType == null
  //        || value == null
  //        || requestId == null) {
  //      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
  //    }
  //    LocalDate startDate = date.with(firstDayOfMonth());
  //    LocalDate lastDate = date.with(lastDayOfMonth());
  //    Double actualWorkingPoint =
  //        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(
  //            startDate, lastDate, employeeId);
  //    actualWorkingPoint = actualWorkingPoint != null ? actualWorkingPoint : 0D;
  //    Long salaryId =
  //        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
  //            employeeId, startDate, lastDate, actualWorkingPoint, ESalaryMonthly.PENDING.name());
  //    deductionSalaryRepository.insertDeductionSalaryByEmployeeId(
  //        salaryId, date, description, deductionType, value);
  //    if (deductionType.equals(EDeduction.getValue("FIRE")))
  //      employeeRepository.updateStatusEmployee(employeeId, false);
  //
  //    applicationsRequestRepository.updateStatusApplication(
  //        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  //  }

  private void updateAdvanceRequest(
      LocalDate date, BigDecimal value, String employeeId, Long requestId) {
    if (date == null || value == null || requestId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    LocalDate startDate = date.with(firstDayOfMonth());
    LocalDate lastDate = date.with(lastDayOfMonth());
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(
            startDate, lastDate, employeeId);
    actualWorkingPoint = actualWorkingPoint != null ? actualWorkingPoint : 0D;
    Long salaryId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, lastDate, actualWorkingPoint, ESalaryMonthly.PENDING.name());
    advanceSalaryRepository.insertAdvanceSalaryByEmployeeId(salaryId, date, value);

    applicationsRequestRepository.updateStatusApplication(
        requestId, ERequestStatus.APPROVED.name(), LocalDateTime.now());
  }

  @Override
  @Transactional
  public void createApplicationsRequest(ApplicationsRequestCreateRequest applicationsRequest) {
    String createEmployeeId = applicationsRequest.getCreateEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(createEmployeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + createEmployeeId);
    }
    Long requestTypeId = applicationsRequest.getRequestTypeId();
    Long requestNameId = applicationsRequest.getRequestNameId();
    if (requestTypeId == null || requestNameId == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    }
    if (applicationsRequestRepository.getAllRequestNameByRequestTypeID(requestTypeId).stream()
        .noneMatch(requestNameDto -> requestNameDto.getRequest_name_id().equals(requestNameId))) {
      throw new CustomDataNotFoundException("Request Type or Request Name does not existed!");
    }

    String approver =
        Objects.requireNonNullElse(
            employeeDetailRepository.getManagerByEmployeeID(createEmployeeId),
            "Nguyen Quang Huy - huynq1");

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
                Integer[] otTimeRemaining =
                    getOTTimeRemaining(applicationsRequest.getCreateEmployeeId());
                if (otTimeRemaining[0] < 1) {
                  throw new CustomErrorException("Your OT time remaining in this year is over!");
                }
                if (otTimeRemaining[1] < 1) {
                  throw new CustomErrorException("Your OT time remaining in this month is over!");
                }
                applicationsRequest = createRequestForWorkingScheduleAndOT(applicationsRequest);
              }
              break;
          }
          break;
        }
      case 2:
        {
          if (requestNameId.intValue() == 6) {
            if (getPaidLeaveDayRemaining(applicationsRequest.getCreateEmployeeId()) < 1) {
              throw new CustomErrorException("Your paid leave remaining is over!");
            }
            applicationsRequest = createRequestForPaidLeave(applicationsRequest);
          }
          break;
        }
      case 3:
        {
          switch (requestNameId.intValue()) {
            case 3:
              {
                //                applicationsRequest =
                // createRequestForNominationAndPromotion(applicationsRequest);
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
          //          applicationsRequest = createRequestForPenalise(applicationsRequest);
          break;
        }
      case 7:
        {
          if (requestNameId.intValue() == 12) {
            applicationsRequest = createRequestForAdvances(applicationsRequest);
          }
          break;
        }
      case 8:
        {
          if (requestNameId.intValue() == 13) {
            //            applicationsRequest = createRequestForTaxEnrollment(applicationsRequest);
          }
          break;
        }
      default:
        {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Request Type ID is not existed!");
        }
    }

    applicationsRequest.setApprover(applicationsRequest.getApprover().split("-")[1].trim());

    applicationsRequest.setRequestStatusId(1L);
    applicationsRequest.setCreateDate(LocalDateTime.now(ZoneId.of("UTC+07")));
    applicationsRequest.setLatestDate(LocalDateTime.now(ZoneId.of("UTC+07")));
    applicationsRequest.setDuration(LocalDateTime.now(ZoneId.of("UTC+07")).plusDays(3));
    applicationsRequest.setIsBookmark(false);
    applicationsRequest.setIsRemind(false);

    applicationsRequestRepository.createApplicationsRequest(applicationsRequest);

    generalFunction.sendEmailCreateRequest(
        createEmployeeId,
        approver,
        FROM_EMAIL,
        applicationsRequest.getApprover() + "@thegorf.tk",
        "New request");
  }

  private void checkLevelAndValueToApprove(
      ApplicationsRequestCreateRequest applicationsRequest, String type) {
    String data =
        applicationsRequestRepository.getDataOfPolicy(applicationsRequest.getRequestNameId());

    HashMap<String, String> splitData = generalFunction.splitData(data);

    String employeeId = applicationsRequest.getCreateEmployeeId().trim();
    int level = employeeDetailRepository.getLevelByEmployeeID(employeeId);
    String value = applicationsRequest.getValue();
    Set<Map.Entry<String, String>> map = splitData.entrySet();
    for (Map.Entry<String, String> entry : map) {
      if (type.equalsIgnoreCase(entry.getKey())) {
        List<RangePolicy> splitRange = generalFunction.splitRange(entry.getValue());
        for (RangePolicy rangePolicy : splitRange) {
          if (Integer.parseInt(rangePolicy.getMin()) <= level
              && Integer.parseInt(rangePolicy.getMax()) >= level) {
            String msg = "";
            if ("salary".equalsIgnoreCase(entry.getKey())) {
              msg = "Salary Increment";
              Long currSalary =
                  Long.valueOf(
                      Objects.requireNonNullElse(
                              employeeDetailRepository
                                  .findWorkingInfo(applicationsRequest.getEmployeeId())
                                  .getFinal_salary(),
                              "0")
                          .split("\\.")[0]);
              Long newSalary = Long.parseLong(value);
              if (newSalary <= currSalary) {
                throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST, "New salary must be greater than the current salary!");
              }
              Long upSalary = newSalary - currSalary;
              if (upSalary.compareTo(Long.valueOf(rangePolicy.getValue())) > 0) {
                throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    "You don't have permission to create "
                        + msg
                        + " "
                        + "request have value greater than "
                        + rangePolicy.getValue()
                        + ".");
              }
            } else {
              msg = "Bonus";
              if (Long.valueOf(value).compareTo(Long.valueOf(rangePolicy.getValue())) > 0) {
                throw new CustomErrorException(
                    HttpStatus.BAD_REQUEST,
                    "You don't have permission to create "
                        + msg
                        + " "
                        + "request have value greater than "
                        + rangePolicy.getValue()
                        + ".");
              }
            }
          }
        }
      }
    }
  }

  private ApplicationsRequestCreateRequest setDescription(
      ApplicationsRequestCreateRequest applicationsRequest, String[] valueArray) {
    String description =
        applicationsRequestRepository.getDescriptionByRequestNameID(
            applicationsRequest.getRequestNameId());
    String[] keyArray = getKeyInDescription(description);
    for (int i = 0; i < keyArray.length; i++) {
      description = description.replaceAll("\\[" + keyArray[i] + "\\]", "[" + valueArray[i] + "]");
    }

    applicationsRequest.setDescription(
        description + "P/S: " + applicationsRequest.getDescription());
    return applicationsRequest;
  }

  private ApplicationsRequestCreateRequest setData(
      ApplicationsRequestCreateRequest applicationsRequest, String[] valueArray) {
    StringBuilder data = new StringBuilder();
    String description =
        applicationsRequestRepository.getDescriptionByRequestNameID(
            applicationsRequest.getRequestNameId());
    String[] keyArray = getKeyInDescription(description);
    for (int i = 1; i < keyArray.length - 1; i++) {
      data.append("[").append(keyArray[i]).append("|").append(valueArray[i]).append("]");
    }

    applicationsRequest.setData(data.toString());
    return applicationsRequest;
  }

  private ApplicationsRequestCreateRequest createRequestForWorkingTime(
      ApplicationsRequestCreateRequest applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    String date = checkLocalDateNull(applicationsRequest.getDate()).toString();
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    String[] valueArray = {approver, date, employee};

    return setDescription(setData(applicationsRequest, valueArray), valueArray);
  }

  private ApplicationsRequestCreateRequest createRequestForWorkingScheduleAndOT(
      ApplicationsRequestCreateRequest applicationsRequest) {
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

    return setDescription(setData(applicationsRequest, valueArray), valueArray);
  }

  private ApplicationsRequestCreateRequest createRequestForPaidLeave(
      ApplicationsRequestCreateRequest applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    String startDate = checkLocalDateNull(applicationsRequest.getStartDate()).toString();
    String endDate = checkLocalDateNull(applicationsRequest.getEndDate()).toString();
    String reasonName = "";
    Long reasonID = checkLongNull(applicationsRequest.getReason());

    List<PaidLeaveReasonDto> list = chartRepository.getAllPaidLeaveReason();
    for (PaidLeaveReasonDto reasonDto : list) {
      if (reasonDto.getReason_id().equals(reasonID)) {
        reasonName = EPaidLeaveReason.getLabel(reasonDto.getReason_name());
      }
    }
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    if (applicationsRequest.getStartDate().isAfter(applicationsRequest.getEndDate())) {
      throw new CustomErrorException("Please choose end date after start date!");
    }

    String[] valueArray = {approver, startDate, endDate, reasonName, employee};

    return setDescription(
        setData(
            applicationsRequest,
            new String[] {approver, startDate, endDate, String.valueOf(reasonID), employee}),
        valueArray);
  }

  private ApplicationsRequestCreateRequest createRequestForAdvances(
      ApplicationsRequestCreateRequest applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    String value = checkStringNull(applicationsRequest.getValue());
    LocalDate date = checkLocalDateNull(applicationsRequest.getDate());
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    Long currSalary =
        Long.valueOf(
            Objects.requireNonNullElse(
                    employeeDetailRepository
                        .findWorkingInfo(applicationsRequest.getEmployeeId())
                        .getFinal_salary(),
                    "0")
                .split("\\.")[0]);
    Long advance = Long.parseLong(value);
    if (advance > currSalary) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Advance value must be less than the current salary!");
    }

    String[] valueArray = {approver, value, String.valueOf(date), employee};

    return setDescription(setData(applicationsRequest, valueArray), valueArray);
  }

  private ApplicationsRequestCreateRequest createRequestForTaxEnrollment(
      ApplicationsRequestCreateRequest applicationsRequest) {
    String approver = applicationsRequest.getApprover();
    List<String> taxTypes = applicationsRequest.getTaxType();
    String taxType = StringUtils.join(taxTypes, ",");
    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    String[] valueArray = {approver, taxType, employee};

    return setDescription(setData(applicationsRequest, valueArray), valueArray);
  }

  private ApplicationsRequestCreateRequest createRequestForNominationAndPromotion(
      ApplicationsRequestCreateRequest applicationsRequest) {
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

    return setDescription(setData(applicationsRequest, valueArray), valueArray);
  }

  private ApplicationsRequestCreateRequest createRequestForNominationAndSalaryIncreaseOrBonus(
      ApplicationsRequestCreateRequest applicationsRequest) {
    String approver = applicationsRequest.getApprover();

    String employeeId = applicationsRequest.getEmployeeId();
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
    }

    String employeeName = checkStringNull(applicationsRequest.getEmployeeName());
    String currentTitle = checkStringNull(applicationsRequest.getCurrentTitle());
    String currentArea = checkStringNull(applicationsRequest.getCurrentArea());
    BigDecimal value = checkBigDecimalNull(applicationsRequest.getValue());
    LocalDate startDate = checkLocalDateNull(applicationsRequest.getDate());
    if (applicationsRequest.getRequestNameId().intValue() == 4) {
      BigDecimal currSalary = workingContractRepository.getBaseSalaryByEmployeeID(employeeId);
      if (value.compareTo(currSalary) < 0) {
        throw new CustomErrorException(
            HttpStatus.BAD_REQUEST, "Increase salary must larger current salary!");
      }
    }

    String employee =
        checkStringNull(
            employeeDetailRepository.getEmployeeInfoByEmployeeID(
                applicationsRequest.getCreateEmployeeId()));

    String type = checkStringNull(applicationsRequest.getType());
    String typeName = applicationsRequest.getType();
    if (applicationsRequest.getRequestNameId().intValue() == 5) {
      BonusTypeDto bonusTypeDto =
          bonusSalaryRepository.getBonusTypeDtoByID(Long.valueOf(applicationsRequest.getType()));
      typeName = EBonus.getLabel(bonusTypeDto.getBonus_type_name());
    }

    String[] valueArray = {
      approver,
      employeeName + " - " + employeeId,
      currentTitle,
      currentArea,
      typeName,
      String.valueOf(value),
      String.valueOf(startDate),
      employee
    };
    return setDescription(
        setData(
            applicationsRequest,
            new String[] {
              approver,
              employeeName + " - " + employeeId,
              currentTitle,
              currentArea,
              type,
              String.valueOf(value),
              String.valueOf(startDate),
              employee
            }),
        valueArray);
  }

  private ApplicationsRequestCreateRequest createRequestForPenalise(
      ApplicationsRequestCreateRequest applicationsRequest) {
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

    return setDescription(setData(applicationsRequest, valueArray), valueArray);
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

  private Long checkLongNull(String value) {
    if (value == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    } else {
      try {
        return Long.parseLong(value);
      } catch (Exception e) {
        throw new CustomErrorException("Please input number only!");
      }
    }
  }

  private BigDecimal checkBigDecimalNull(String value) {
    if (value == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    } else {
      try {
        return new BigDecimal(value);
      } catch (Exception e) {
        throw new CustomErrorException("Please input number only!");
      }
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
  public Integer[] getOTTimeRemaining(String employeeId) {
    Integer currentOTInYear = timekeepingRepository.countOvertimeOfEmployeeByYear(employeeId);
    Integer currentOTInMonth = timekeepingRepository.countOvertimeOfEmployeeByMonth(employeeId);
    OvertimeDataDto dataDto = generalFunction.readOvertimeData();

    return new Integer[] {
      dataDto.getYear() - currentOTInYear, dataDto.getMonth() - currentOTInMonth
    };
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
  public void updateAllApplicationRequestRemind(LocalDateTime checkDate, LocalDateTime currDate) {
    List<ApplicationRequestRemindResponse> applicationRequestRemindResponses =
        applicationsRequestRepository.getAllApplicationRequestToRemind(checkDate);

    applicationRequestRemindResponses.forEach(
        applicationRequestRemindResponse -> {
          String createName =
              applicationRequestRemindResponse.getFull_name()
                  + "-"
                  + applicationRequestRemindResponse.getEmployee_id();

          String approveName =
              employeeRepository.getEmployeeNameByEmployeeId(
                  applicationRequestRemindResponse.getApprover());

          String approveEmail =
              employeeRepository.getEmployeeEmailByEmployeeId(
                  applicationRequestRemindResponse.getApprover());

          if (applicationRequestRemindResponse
                  .getDuration()
                  .toLocalDate()
                  .isEqual(currDate.toLocalDate())
              || applicationRequestRemindResponse
                  .getDuration()
                  .toLocalDate()
                  .isBefore(currDate.toLocalDate())) {
            applicationsRequestRepository.updateRejectApplicationRequest(
                new RejectApplicationRequestRequest(
                    applicationRequestRemindResponse.getApplication_request_id(),
                    "Out of duration"),
                LocalDateTime.now());
          } else {
            generalFunction.sendEmailRemindRequest(
                approveName,
                createName,
                applicationRequestRemindResponse
                    .getCreate_date()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                applicationRequestRemindResponse.getChecked_by(),
                applicationRequestRemindResponse.getApplication_request_id().toString(),
                applicationRequestRemindResponse
                    .getDuration()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                applicationRequestRemindResponse.getDuration().compareTo(currDate) + "",
                FROM_EMAIL,
                approveEmail,
                "Remind Request");
          }
        });
  }
}