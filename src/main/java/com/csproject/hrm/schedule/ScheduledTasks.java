package com.csproject.hrm.schedule;

import com.csproject.hrm.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

@Component
@Slf4j
public class ScheduledTasks {

  @Autowired ApplicationsRequestService applicationsRequestService;
  @Autowired TimekeepingService timekeepingService;
  @Autowired SalaryMonthlyService salaryMonthlyService;
  @Autowired HumanManagementService humanManagementService;
  @Autowired SalaryContractService salaryContractService;

  @Scheduled(cron = "0 59 23 ? * * ")
  public void scheduleTaskSendMailRemind() {
    LocalDateTime currDate = LocalDateTime.now();
    LocalDateTime checkDate = currDate.plus(2, ChronoUnit.DAYS);
    applicationsRequestService.updateAllApplicationRequestRemind(checkDate, currDate);
    salaryMonthlyService.updateAllSalaryRemind(checkDate.toLocalDate(), currDate.toLocalDate());
    humanManagementService.updateWorkingStatusForListEmployee(currDate.toLocalDate());
  }

  @Scheduled(cron = "0 50 23 ? * * ")
  public void scheduleTaskUpdatePointDay() {
    LocalDate currentDate = LocalDate.now();
    timekeepingService.upsertPointPerDay(currentDate);
  }

  @Scheduled(cron = "0 59 23 L * ?")
  public void scheduleTaskCreateSalaryMonthly() {
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.with(firstDayOfMonth());
    salaryMonthlyService.upsertSalaryMonthlyByEmployeeIdList(startDate, endDate);
  }

  @Scheduled(cron = "0 0 1 1  * ?")
  public void updateSalaryContractPerMonth() {
    LocalDate currentDate = LocalDate.now();
    salaryContractService.updateStatusSalaryContract(true, currentDate);
    salaryContractService.updateStatusWorkingContract(true, currentDate);
  }

  @Scheduled(cron = "0 00 07 ? * * ")
  public void logTest() {
    log.info("Morning");
  }
}
