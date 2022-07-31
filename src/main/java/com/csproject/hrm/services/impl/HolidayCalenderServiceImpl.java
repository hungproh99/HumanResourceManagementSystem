package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.repositories.HolidayCalenderRepository;
import com.csproject.hrm.services.HolidayCalenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class HolidayCalenderServiceImpl implements HolidayCalenderService {
  @Autowired HolidayCalenderRepository holidayCalenderRepository;

  @Override
  public List<LocalDate> getAllHolidayByYear(LocalDate currDate) {
    LocalDate firstDate = currDate.with(firstDayOfYear());
    LocalDate lastDate = currDate.with(lastDayOfYear());
    List<LocalDate> localDateList = new ArrayList<>();
    List<HolidayCalenderDto> holidayCalenderDtoList =
        holidayCalenderRepository.getAllHolidayInYear(firstDate, lastDate);

    holidayCalenderDtoList.forEach(
        holidayCalenderDto -> {
          List<LocalDate> holidayList =
              holidayCalenderDto
                  .getStart_date()
                  .datesUntil(holidayCalenderDto.getEnd_date())
                  .collect(Collectors.toList());
          holidayList.add(holidayCalenderDto.getEnd_date());
          for (LocalDate date : holidayList) {
            localDateList.add(date);
          }
        });
    return localDateList;
  }
}