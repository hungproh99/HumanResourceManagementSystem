package com.csproject.hrm.schedule;

import com.csproject.hrm.services.ApplicationsRequestService;
import com.csproject.hrm.services.TimekeepingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class ScheduledTasks {

  @Autowired ApplicationsRequestService applicationsRequestService;
  @Autowired TimekeepingService timekeepingService;

  @Scheduled(cron = "0 59 23 ? * * ")
  public void scheduleTaskSendMailRemind() {
    LocalDateTime checkDate = LocalDateTime.now().plus(3, ChronoUnit.DAYS);
    applicationsRequestService.updateAllApplicationRequestRemind(checkDate);
  }

  @Scheduled(cron = "0 50 23 ? * * ")
  public void scheduleTaskUpdatePointDay() {
    LocalDate currentDate = LocalDate.now();
    timekeepingService.upsertPointPerDay(currentDate);
  }

  //  @Scheduled(cron = "0 59 23 1 * ?")
  //  public void scheduleTaskCreateSalaryMonthly() {
  //    List<String> employeeIdList = employeeRepository.getAllEmployeeIdActive();
  //    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
  //    LocalDate currentDate = LocalDate.now();
  //    LocalDate lastDateOfMonth = currentDate.minusMonths(1);
  //    Double standardPoint = salaryCalculator.getStandardPointPerMonth(currentDate,
  // lastDateOfMonth);
  //
  //    salaryMonthlyRepository.insertSalaryMonthlyByEmployee(salaryMonthlyDtoList);
  //  }
}
