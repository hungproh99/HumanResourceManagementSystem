package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.ETimekeepingStatus;
import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.dto.dto.TimekeepingDto;
import com.csproject.hrm.dto.request.HolidayCalendarRequest;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.repositories.HolidayCalenderRepository;
import com.csproject.hrm.repositories.ListTimekeepingStatusRepository;
import com.csproject.hrm.repositories.TimekeepingRepository;
import com.csproject.hrm.services.HolidayCalenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class HolidayCalenderServiceImpl implements HolidayCalenderService {
  @Autowired HolidayCalenderRepository holidayCalenderRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired EmployeeRepository employeeRepository;
  @Autowired ListTimekeepingStatusRepository listTimekeepingStatusRepository;

  @Override
  public List<HolidayCalenderDto> getAllHolidayByYear(LocalDate currDate) {
    LocalDate firstDate = currDate.with(firstDayOfYear());
    LocalDate lastDate = currDate.with(lastDayOfYear());

    return holidayCalenderRepository.getAllHolidayInYear(firstDate, lastDate);
  }

  @Override
  public void insertHolidayCalendar(HolidayCalendarRequest holidayCalendarRequest) {
    holidayCalenderRepository.insertHoliday(holidayCalendarRequest);
    List<LocalDate> holidayList =
        holidayCalendarRequest
            .getStart_date()
            .datesUntil(holidayCalendarRequest.getEnd_date())
            .collect(Collectors.toList());
    holidayList.add(holidayCalendarRequest.getEnd_date());
    List<String> employeeIdList = employeeRepository.getAllEmployeeIdActive();
    for (String employeeId : employeeIdList) {
      if (employeeId.equalsIgnoreCase("admin")) {
        continue;
      }
      for (LocalDate date : holidayList) {
        Long timekeepingId =
            timekeepingRepository.insertTimekeeping(new TimekeepingDto(0D, 0D, date, employeeId));
        listTimekeepingStatusRepository.insertListTimekeepingStatus(
            timekeepingId, ETimekeepingStatus.HOLIDAY.name());
      }
    }
  }
}