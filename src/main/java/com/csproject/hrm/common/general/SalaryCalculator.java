package com.csproject.hrm.common.general;

import com.csproject.hrm.common.enums.EWorkingType;
import com.csproject.hrm.dto.dto.HolidayCalenderDto;
import com.csproject.hrm.dto.dto.SalaryContractDto;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.*;

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

  public Double getStandardPointPerMonth(LocalDate currentDate, LocalDate lastDateOfMonth) {
    List<LocalDate> totalDate =
        lastDateOfMonth.datesUntil(currentDate).collect(Collectors.toList());
    List<LocalDate> holidayAndWeekendList =
        Stream.of(getAllHolidayByYear(currentDate), getAllWeekendByYear(currentDate))
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());
    Set<LocalDate> setDate = new LinkedHashSet<>(totalDate);
    setDate.addAll(holidayAndWeekendList);
    List<LocalDate> standardDateList = new ArrayList<>(setDate);
    return Double.parseDouble(String.valueOf(standardDateList.size()));
  }

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

  public List<LocalDate> getAllWeekendByYear(LocalDate currDate) {
    LocalDate firstDate = currDate.with(firstDayOfYear());
    List<LocalDate> localDateList = new ArrayList<>();
    LocalDate firstSunday = firstDate.with(firstInMonth(DayOfWeek.SUNDAY));
    LocalDate firstSaturday = firstDate.with(firstInMonth(DayOfWeek.SATURDAY));

    while (firstSunday.getYear() == currDate.getYear()) {
      localDateList.add(firstSunday);
      firstSunday = firstSunday.plus(Period.ofDays(7));
    }

    while (firstSaturday.getYear() == currDate.getYear()) {
      localDateList.add(firstSaturday);
      firstSaturday = firstSaturday.plus(Period.ofDays(7));
    }
    return localDateList;
  }

//  public BigDecimal getMoneyPerDay(LocalDate firstDate, LocalDate lastDate, String employeeId) {
//    Optional<SalaryContractDto> salaryContractDto =
//        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
//    if (salaryContractDto.isEmpty()) {
//      throw new CustomErrorException(
//          HttpStatus.BAD_REQUEST, "Error with contract salary of " + employeeId);
//    }
//    Integer actualWorkDate =
//        timekeepingRepository.countActualWorkPerMonthByEmployee(firstDate, lastDate, employeeId);
//    if (actualWorkDate == 0) {
//      throw new CustomErrorException(
//          HttpStatus.BAD_REQUEST, "Invalid actual work data of " + employeeId);
//    }
//    return salaryContractDto
//        .get()
//        .getAdditional_salary()
//        .divide(BigDecimal.valueOf(actualWorkDate));
//  }

  public Double getPointOfWorkByEmployeeId(String employeeId) {
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    if (salaryContractDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Error with contract salary of " + employeeId);
    }
    if (salaryContractDto.get().getWorking_type().equalsIgnoreCase(EWorkingType.FULL_TIME.name())) {
      return 1D;
    } else if (salaryContractDto
        .get()
        .getWorking_type()
        .equalsIgnoreCase(EWorkingType.PART_TIME.name())) {
      return 0.5D;
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Error working type of " + employeeId);
  }
}
