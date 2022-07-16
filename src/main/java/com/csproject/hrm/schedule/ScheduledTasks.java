package com.csproject.hrm.schedule;

import com.csproject.hrm.common.enums.EPolicyType;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.PolicyDto;
import com.csproject.hrm.dto.dto.RangePunishPolicy;
import com.csproject.hrm.dto.dto.SalaryContractDto;
import com.csproject.hrm.dto.response.ApplicationRequestRemindResponse;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.csproject.hrm.common.constant.Constants.*;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Slf4j
public class ScheduledTasks {

  @Autowired ApplicationsRequestRepository applicationsRequestRepository;
  @Autowired EmployeeRepository employeeRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired PolicyRepository policyRepository;
  @Autowired GeneralFunction generalFunction;
  @Autowired SalaryContractRepository salaryContractRepository;

  @Scheduled(cron = "0 59 23 ? * * ")
  public void scheduleTaskSendMailRemind() {
    LocalDateTime checkDate = LocalDateTime.now().plus(2, ChronoUnit.DAYS);
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

  @Scheduled(cron = "0 59 23 ? * * ")
  public void scheduleTaskUpdatePointDay() {
    List<String> employeeIdList = employeeRepository.getAllEmployeeIdActive();
    LocalDate currentDate = LocalDate.now();
    Optional<PolicyDto> policyDto =
        policyRepository.getPolicyDtoByPolicyType(EPolicyType.WORKING_TIME.name());
    if (policyDto.isEmpty()) {
      log.error("Error Schedule Task Update Point Day");
    }
    Set<Map.Entry<String, String>> hashMap = splitData(policyDto.get().getData()).entrySet();
    LocalTime startTime = null, endTime = null;
    List<RangePunishPolicy> listRange = new ArrayList<>();
    Long maxTimePunish = null, minTimePunish = null;

    String punish = null;
    for (Map.Entry<String, String> i : hashMap) {
      switch (i.getKey()) {
        case "Start_Time":
          startTime = LocalTime.parse(i.getValue());
          break;
        case "End_Time":
          endTime = LocalTime.parse(i.getValue());
          break;
        case "Punish":
          punish = i.getValue();
          listRange = splitRange(punish);
          break;
      }
    }
    LocalTime finalStartTime = startTime;
    LocalTime finalEndTime = endTime;
    List<RangePunishPolicy> finalListRange = listRange;
    employeeIdList.forEach(
        employeeId -> {
          BigDecimal minusMoney = BigDecimal.ZERO;
          Optional<SalaryContractDto> salaryContractDto =
              salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
          if (salaryContractDto.isEmpty()) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "Error with contract salary of " + employeeId);
          }

          LocalTime firstTimeCheckIn =
              timekeepingRepository.getFirstTimeCheckInByTimekeeping(currentDate, employeeId);
          LocalTime lastTimeCheckOut =
              timekeepingRepository.getLastTimeCheckInByTimekeeping(currentDate, employeeId);
          if (firstTimeCheckIn.isAfter(finalStartTime)) {
            Long rangeTime = MINUTES.between(firstTimeCheckIn, finalStartTime);
            for (RangePunishPolicy rangePunishPolicy : finalListRange) {
              if (rangePunishPolicy.getMinTime() < rangeTime
                  && rangePunishPolicy.getMaxTime() > rangeTime) {
                minusMoney = minusMoney.subtract(rangePunishPolicy.getMinus());
              }
            }
          } else if (lastTimeCheckOut.isBefore(finalEndTime)) {
            Long rangeTime = MINUTES.between(lastTimeCheckOut, finalEndTime);
            for (RangePunishPolicy rangePunishPolicy : finalListRange) {
              if (rangePunishPolicy.getMinTime() < rangeTime
                  && rangePunishPolicy.getMaxTime() > rangeTime) {
                minusMoney = minusMoney.subtract(rangePunishPolicy.getMinus());
              }
            }
          }

          //          timekeepingRepository.updatePointPerDay();
        });
  }

  @Scheduled(cron = "0 59 23 2 * ?")
  public void scheduleTaskCreateSalaryMonthly() {}

  private HashMap<String, String> splitData(String data) {
    HashMap<String, String> hashMap = new HashMap<>();
    if (!isBlank(data)) {
      String[] splitBracket = StringUtils.substringsBetween(data, "[", "]");
      for (String split : splitBracket) {
        String[] splitSeparator = split.split(SEPARATOR, TWO_NUMBER);
        if (isInvalidSplit(splitSeparator)
            || splitSeparator[ZERO_NUMBER] == null
            || splitSeparator[ONE_NUMBER] == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
        hashMap.put(splitSeparator[ZERO_NUMBER], splitSeparator[ONE_NUMBER]);
      }
    } else {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    return hashMap;
  }

  private List<RangePunishPolicy> splitRange(String data) {
    List<RangePunishPolicy> rangePunishPolicyList = new ArrayList<>();
    if (!isBlank(data)) {
      String[] splitBracket = StringUtils.substringsBetween(data, "[", "]");
      for (String split : splitBracket) {
        RangePunishPolicy rangePunishPolicy = null;
        String[] splitSeparator = split.split(SEPARATOR, TWO_NUMBER);
        if (isInvalidSplit(splitSeparator)
            || splitSeparator[ZERO_NUMBER] == null
            || splitSeparator[ONE_NUMBER] == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
        rangePunishPolicy.setMinus(BigDecimal.valueOf(Long.parseLong(splitSeparator[ONE_NUMBER])));
        String[] splitDash = splitSeparator[ZERO_NUMBER].split(DASH_CHARACTER, TWO_NUMBER);
        if (isInvalidSplit(splitDash)
            || splitDash[ZERO_NUMBER] == null
            || splitDash[ONE_NUMBER] == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Invalid Data");
        }
        rangePunishPolicy.setMinTime(Long.parseLong(splitDash[ZERO_NUMBER]));
        rangePunishPolicy.setMaxTime(Long.parseLong(splitDash[ONE_NUMBER]));
      }
    } else {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    return rangePunishPolicyList;
  }

  private boolean isInvalidSplit(String[] split) {
    return split.length != TWO_NUMBER || isBlank(split[ZERO_NUMBER]) || isBlank(split[ONE_NUMBER]);
  }
}
