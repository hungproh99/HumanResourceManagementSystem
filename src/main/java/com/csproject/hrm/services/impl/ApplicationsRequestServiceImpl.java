package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.common.excel.ExcelExportApplicationRequest;
import com.csproject.hrm.dto.dto.ApplicationRequestDto;
import com.csproject.hrm.dto.dto.RequestNameDto;
import com.csproject.hrm.dto.dto.RequestStatusDto;
import com.csproject.hrm.dto.dto.RequestTypeDto;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.dto.response.ApplicationsRequestResponse;
import com.csproject.hrm.dto.response.ListApplicationsRequestResponse;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.repositories.custom.impl.BonusSalaryRepositoryImpl;
import com.csproject.hrm.repositories.custom.impl.SalaryMonthlyRepositoryImpl;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  @Autowired SalaryMonthlyRepositoryImpl salaryMonthlyRepository;
  @Autowired BonusSalaryRepositoryImpl bonusSalaryRepository;
  @Autowired DeductionSalaryRepository deductionSalaryRepository;
  @Autowired AdvanceSalaryRepository advanceSalaryRepository;

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
  public void updateApplicationRequest(Long requestId) {
    Optional<ApplicationRequestDto> applicationRequestDto =
        applicationsRequestRepository.getApplicationRequestDtoByRequestId(requestId);
    if (applicationRequestDto.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + "with " + requestId);
    }
    updateTimekeepingInformation(applicationRequestDto.get());
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

  private void updateTimekeepingInformation(ApplicationRequestDto requestDto) {
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
        updateWorkingTime(date, employeeId, requestName);
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
            requestName);
        break;
      case "PAID_LEAVE":
        updatePaidLeave(startDate, endDate, employeeId, requestName);
        break;
      case "PROMOTION":
        updatePromotion(
            desiredArea,
            desiredOffice,
            desiredPosition,
            desiredGrade,
            startDate,
            value,
            employeeId);
        break;
      case "SALARY_INCREMENT":
        updateSalaryIncrement(startDate, value, employeeId);
        break;
      case "BONUS":
        updateBonusSalary(date, description, value, employeeId, salaryId, bonusType);
        break;
      case "CONFLICT_CUSTOMER":
      case "LEAK_INFORMATION":
        updateConflictAndLeakInfo(date, description, value, employeeId, salaryId, deductionType);
        break;
      case "ADVANCE":
        updateAdvanceRequest(date, description, value, employeeId, salaryId);
        break;
    }
  }

  private void updateWorkingTime(LocalDate date, String employeeId, String requestName) {
    if (date == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    timekeepingRepository.deleteTimekeepingStatusByEmployeeIdAndDate(
        employeeId, date, ETimekeepingStatus.getValue(requestName));
  }

  private void updateOvertime(
      LocalDate startDate,
      LocalDate endDate,
      LocalTime startTime,
      LocalTime endTime,
      Long overtimeType,
      LocalDate currentDate,
      String employeeId,
      String requestName) {
    if (startDate == null
        || endDate == null
        || startTime == null
        || endTime == null
        || overtimeType == null) {
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
  }

  private void updatePaidLeave(
      LocalDate startDate, LocalDate endDate, String employeeId, String requestName) {
    if (startDate == null || endDate == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    timekeepingRepository.upsertTimekeepingStatusByEmployeeIdAndRangeDate(
        employeeId,
        startDate,
        endDate,
        ETimekeepingStatus.getValue("DAY_OFF"),
        ETimekeepingStatus.getValue(requestName));
  }

  private void updatePromotion(
      Long desiredArea,
      Long desiredOffice,
      Long desiredPosition,
      Long desiredGrade,
      LocalDate startDate,
      BigDecimal value,
      String employeeId) {
    if (desiredArea == null
        || desiredOffice == null
        || desiredPosition == null
        || desiredGrade == null
        || startDate == null
        || value == null) {
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
  }

  private void updateSalaryIncrement(LocalDate startDate, BigDecimal value, String employeeId) {
    if (startDate == null || value == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryContractRepository.insertNewSalaryContract(employeeId, value, startDate, false, true);
  }

  private void updateBonusSalary(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long salaryId,
      Long bonusType) {
    if (date == null || description == null || bonusType == null || value == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryId = salaryMonthlyRepository.getSalaryIdByEmployeeIdAndDate(employeeId, date);
    bonusSalaryRepository.insertBonusSalaryByEmployeeId(
        salaryId, date, description, bonusType, value);
  }

  private void updateConflictAndLeakInfo(
      LocalDate date,
      String description,
      BigDecimal value,
      String employeeId,
      Long salaryId,
      Long deductionType) {
    if (date == null || description == null || deductionType == null || value == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryId = salaryMonthlyRepository.getSalaryIdByEmployeeIdAndDate(employeeId, date);
    deductionSalaryRepository.insertDeductionSalaryByEmployeeId(
        salaryId, date, description, deductionType, value);
    if (deductionType.equals(EDeduction.getValue("FIRE")))
      employeeRepository.updateStatusEmployee(employeeId, false);
  }

  private void updateAdvanceRequest(
      LocalDate date, String description, BigDecimal value, String employeeId, Long salaryId) {
    if (date == null || description == null || value == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not enough data to update");
    }
    salaryId = salaryMonthlyRepository.getSalaryIdByEmployeeIdAndDate(employeeId, date);
    advanceSalaryRepository.insertAdvanceSalaryByEmployeeId(salaryId, date, description, value);
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
}
