package com.csproject.hrm.common.general;

import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryCalculator {
  @Autowired HolidayCalenderRepository holidayCalenderRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired SalaryContractRepository salaryContractRepository;
  @Autowired PolicyRepository policyRepository;

  @Autowired OvertimeRepository overtimeRepository;

  //  public Double getStandardPointPerMonth(LocalDate currentDate, LocalDate lastDateOfMonth) {
  //    List<LocalDate> totalDate =
  //        lastDateOfMonth.datesUntil(currentDate).collect(Collectors.toList());
  //    List<LocalDate> holidayAndWeekendList =
  //        Stream.of(getAllHolidayByYear(currentDate), getAllWeekendByYear(currentDate))
  //            .flatMap(x -> x.stream())
  //            .collect(Collectors.toList());
  //    Set<LocalDate> setDate = new LinkedHashSet<>(totalDate);
  //    setDate.addAll(holidayAndWeekendList);
  //    List<LocalDate> standardDateList = new ArrayList<>(setDate);
  //    return Double.parseDouble(String.valueOf(standardDateList.size()));
  //  }

  public List<LocalDate> getAllHolidayByRange(LocalDate firstDate, LocalDate lastDate) {
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

  public List<LocalDate> getAllWeekendByRange(LocalDate firstDate, LocalDate lastDate) {
    List<LocalDate> localDateList = new ArrayList<>();
    for (LocalDate date = firstDate; date.isBefore(lastDate.plusDays(1)); date = date.plusDays(1)) {
      if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
        localDateList.add(date);
      }
    }
    return localDateList;
  }
}
