package com.csproject.hrm.controllers;

import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.services.HumanManagementService;
import com.csproject.hrm.services.TimekeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
@Validated
public class AddDataController {

  @Autowired HumanManagementService humanManagementService;
  @Autowired TimekeepingService timekeepingService;
  @Autowired SalaryCalculator salaryCalculator;

  @GetMapping("/add_data_timekeeping")
  public ResponseEntity<?> getListApplicationsRequestReceive() {

    LocalDate startDate = LocalDate.parse("2019-01-01");
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
        timekeepingService.insertTimekeepingCheckInCheckOut("HuyNQ1", date, LocalTime.of(8, 0, 0));
        timekeepingService.insertTimekeepingCheckInCheckOut("HuyNQ1", date, LocalTime.of(12, 0, 0));
        timekeepingService.insertTimekeepingCheckInCheckOut("HuyNQ1", date, LocalTime.of(13, 0, 0));
        timekeepingService.insertTimekeepingCheckInCheckOut(
            "HuyNQ1", date, LocalTime.of(17, 30, 0));
      }
      check = false;
    }

    return ResponseEntity.ok("Ok");
  }
}
