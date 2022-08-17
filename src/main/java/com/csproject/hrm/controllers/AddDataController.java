package com.csproject.hrm.controllers;

import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.services.HumanManagementService;
import com.csproject.hrm.services.SalaryMonthlyService;
import com.csproject.hrm.services.TimekeepingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.csproject.hrm.common.constant.Constants.REQUEST_SUCCESS;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
@Validated
public class AddDataController {

  @Autowired HumanManagementService humanManagementService;
  @Autowired TimekeepingService timekeepingService;
  @Autowired SalaryCalculator salaryCalculator;
  @Autowired SalaryMonthlyService salaryMonthlyService;

  @GetMapping("/add_data_timekeeping")
  public ResponseEntity<?> getListApplicationsRequestReceive() {
    List<TimeKeepingDataDto> employeeIdList = new ArrayList<>();
    employeeIdList.add(new TimeKeepingDataDto("HuyNQ1", 1));
    employeeIdList.add(new TimeKeepingDataDto("LienPT1", 1));
    employeeIdList.add(new TimeKeepingDataDto("QuangNM1", 1));
    employeeIdList.add(new TimeKeepingDataDto("VietHQ1", 1));

    LocalDate startDate = LocalDate.parse("2022-06-01");
    LocalDate endDate = LocalDate.parse("2022-08-16");
    List<LocalDate> getListHoliday = salaryCalculator.getAllHolidayByRange(startDate, endDate);
    List<LocalDate> getListWeekend = salaryCalculator.getAllWeekendByRange(startDate, endDate);
    List<LocalDate> addList =
        Stream.of(getListHoliday, getListWeekend)
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());
    List<LocalDate> newList = startDate.datesUntil(endDate).collect(Collectors.toList());
    newList.add(endDate);
    boolean check = false;
    for (LocalDate date : newList) {
      for (LocalDate date1 : addList) {
        if (date.isEqual(date1)) {
          check = true;
          break;
        }
      }
      if (!check) {
        employeeIdList.forEach(
            employeeId -> {
              if (employeeId.type == 1) {
                timekeepingService.insertTimekeepingCheckInCheckOut(
                    employeeId.getEmployeeId(), date, 1D, LocalTime.of(8, 0, 0));
                timekeepingService.insertTimekeepingCheckInCheckOut(
                    employeeId.getEmployeeId(), date, 1D, LocalTime.of(12, 0, 0));
                timekeepingService.insertTimekeepingCheckInCheckOut(
                    employeeId.getEmployeeId(), date, 1D, LocalTime.of(13, 0, 0));
                timekeepingService.insertTimekeepingCheckInCheckOut(
                    employeeId.getEmployeeId(), date, 1D, LocalTime.of(17, 30, 0));
              } else if (employeeId.type == 0) {
                timekeepingService.insertTimekeepingCheckInCheckOut(
                    employeeId.getEmployeeId(), date, 0.5D, LocalTime.of(8, 0, 0));
                timekeepingService.insertTimekeepingCheckInCheckOut(
                    employeeId.getEmployeeId(), date, 0.5D, LocalTime.of(12, 0, 0));
              }
            });
      }
      check = false;
    }
    return ResponseEntity.ok("Ok");
  }

  @GetMapping("/test")
  public ResponseEntity<?> getTest() {
    salaryMonthlyService.upsertSalaryMonthlyByEmployeeIdList(
        LocalDate.of(2022, 6, 1), LocalDate.of(2022, 6, 30));
    salaryMonthlyService.upsertSalaryMonthlyByEmployeeIdList(
        LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 31));

    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class TimeKeepingDataDto {
    private String employeeId;
    private int type;
  }
}
