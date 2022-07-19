package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.ETimekeepingStatus;
import com.csproject.hrm.common.excel.ExcelExportTimekeeping;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.TimekeepingService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.csproject.hrm.common.constant.Constants.*;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class TimekeepingServiceImpl implements TimekeepingService {
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired EmployeeDetailRepository employeeDetailRepository;
  @Autowired EmployeeRepository employeeRepository;
  @Autowired PolicyRepository policyRepository;
  @Autowired OvertimeRepository overtimeRepository;
  @Autowired GeneralFunction generalFunction;
  @Autowired SalaryCalculator salaryCalculator;

  @Override
  public TimekeepingResponsesList getListAllTimekeeping(QueryParam queryParam) {
    List<TimekeepingResponses> timekeepingResponsesList =
        timekeepingRepository.getListAllTimekeeping(queryParam);
    int total = timekeepingRepository.countListAllTimekeeping(queryParam);

    return TimekeepingResponsesList.builder()
        .timekeepingResponsesList(timekeepingResponsesList)
        .total(total)
        .build();
  }

  @Override
  public void exportTimekeepingToCsv(Writer writer, QueryParam queryParam, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<TimekeepingResponses> timekeepingResponses =
          timekeepingRepository.getListTimekeepingToExport(queryParam, list);
      try (CSVPrinter csvPrinter =
          new CSVPrinter(
              writer,
              CSVFormat.DEFAULT.withHeader(
                  "Full Name",
                  "Position",
                  "Grade",
                  "Current Date",
                  "Timekeeping Status",
                  "First Check In",
                  "Last Check Out"))) {

        for (TimekeepingResponses timekeepingResponseList : timekeepingResponses) {
          for (TimekeepingResponse timekeepingResponse :
              timekeepingResponseList.getTimekeepingResponses()) {
            csvPrinter.printRecord(
                timekeepingResponseList.getFull_name(),
                timekeepingResponseList.getPosition(),
                timekeepingResponseList.getGrade(),
                timekeepingResponse.getCurrent_date(),
                timekeepingResponse.getTimekeeping_status(),
                timekeepingResponse.getFirst_check_in(),
                timekeepingResponse.getLast_check_out());
          }
        }
        csvPrinter.flush();
      } catch (IOException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, CAN_NOT_WRITE_CSV);
      }
    }
  }

  @Override
  public void exportTimekeepingToExcel(
      HttpServletResponse response, QueryParam queryParam, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      try {
        List<TimekeepingResponses> timekeepingResponses =
            timekeepingRepository.getListTimekeepingToExport(queryParam, list);
        ExcelExportTimekeeping excelExportTimekeeping =
            new ExcelExportTimekeeping(timekeepingResponses);
        excelExportTimekeeping.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, String date) {
    if (employeeID == null || date == null) {
      throw new NullPointerException("Had param is null!");
    }
    LocalDate localDate;
    try {
      localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException e) {
      throw new CustomErrorException("Date must have format yyyy-MM-dd!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      Optional<TimekeepingDetailResponse> list =
          timekeepingRepository.getTimekeepingByEmployeeIDAndDate(employeeID, localDate);
      if (list.isEmpty()) {
        return null;
      }
      Long timekeepingID = list.get().getTimekeeping_id();
      list.get()
          .setCheck_in_check_outs(
              timekeepingRepository.getCheckInCheckOutByTimekeepingID(timekeepingID));
      List<ListTimekeepingStatusResponse> listTimekeepingStatusResponse =
          timekeepingRepository.getListTimekeepingStatus(timekeepingID);
      listTimekeepingStatusResponse.stream()
          .forEach(
              listTimekeepingStatusResponse1 ->
                  listTimekeepingStatusResponse1.setTimekeeping_status(
                      ETimekeepingStatus.getLabel(
                          listTimekeepingStatusResponse1.getTimekeeping_status())));
      list.get().setTimekeeping_status(listTimekeepingStatusResponse);
      return list;
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }

  @Override
  public void upsertPointPerDay(LocalDate currentDate) {
    List<String> employeeIdList = employeeRepository.getAllEmployeeIdActive();
    List<String> listUpdate = new ArrayList<>();
    List<String> listInsert = new ArrayList<>();
    employeeIdList.forEach(
        employeeId -> {
          if (timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId)) {
            listUpdate.add(employeeId);
          } else {
            listInsert.add(employeeId);
          }
        });
    updatePointPerDay(listUpdate, currentDate);
    insertTimekeeping(listInsert, currentDate);
  }

  private void updatePointPerDay(List<String> employeeIdList, LocalDate currentDate) {
    WorkingTimeDataDto workingTimeDataDto = generalFunction.readWorkingTimeData();
    List<TimekeepingDto> timekeepingDtoList = new ArrayList<>();
    OvertimeDataDto overtimeDataDto = generalFunction.readOvertimeData();
    Double standardHourWork =
        Double.parseDouble(
            String.valueOf(
                MINUTES.between(workingTimeDataDto.getStartTime(), workingTimeDataDto.getEndTime())
                    / 60));

    employeeIdList.forEach(
        employeeId -> {
          Double pointPerDay = salaryCalculator.getPointOfWorkByEmployeeId(employeeId);
          Double pointOT = 0D;
          List<OvertimePoint> overtimePointList = overtimeDataDto.getOvertimePointList();
          Optional<OvertimeDto> overtimeDto =
              overtimeRepository.getOvertimeByEmployeeIdAndDate(currentDate, employeeId);
          String otType = null;
          if (overtimeDto.isPresent()) {
            otType = overtimeDto.get().getOvertime_type();
          }
          if (otType != null) {
            for (OvertimePoint point : overtimePointList) {
              if (point.getType().equalsIgnoreCase(otType)) {
                pointOT =
                    Double.parseDouble(
                            String.valueOf(
                                MINUTES.between(
                                        overtimeDto.get().getStart_time(),
                                        overtimeDto.get().getEnd_time())
                                    / standardHourWork))
                        * point.getPoint();
              }
            }
          }

          LocalTime firstTimeCheckIn =
              timekeepingRepository.getFirstTimeCheckInByTimekeeping(currentDate, employeeId);
          LocalTime lastTimeCheckOut =
              timekeepingRepository.getLastTimeCheckInByTimekeeping(currentDate, employeeId);
          if (firstTimeCheckIn.isAfter(workingTimeDataDto.getStartTime())) {
            Long rangeTime = MINUTES.between(firstTimeCheckIn, workingTimeDataDto.getStartTime());
            for (RangePolicy rangePolicy : workingTimeDataDto.getListRange()) {
              if (rangePolicy.getMin() < rangeTime && rangePolicy.getMax() > rangeTime) {
                pointPerDay -=
                    Double.parseDouble(String.valueOf(rangePolicy.getValue())) * pointPerDay;
              }
            }
          } else if (lastTimeCheckOut.isBefore(workingTimeDataDto.getEndTime())) {
            Long rangeTime = MINUTES.between(lastTimeCheckOut, workingTimeDataDto.getEndTime());
            for (RangePolicy rangePolicy : workingTimeDataDto.getListRange()) {
              if (rangePolicy.getMin() < rangeTime && rangePolicy.getMax() > rangeTime) {
                pointPerDay -=
                    Double.parseDouble(String.valueOf(rangePolicy.getValue())) * pointPerDay;
              }
            }
          }
          timekeepingDtoList.add(new TimekeepingDto(pointPerDay, pointOT, currentDate, employeeId));
        });
    timekeepingRepository.updatePointPerDay(timekeepingDtoList);
  }

  public void insertTimekeeping(List<String> employeeIdList, LocalDate currentDate) {
    List<LocalDate> holidayList = salaryCalculator.getAllHolidayByYear(currentDate);
    List<LocalDate> weekendList = salaryCalculator.getAllWeekendByYear(currentDate);
    List<LocalDate> holidayAndWeekendList =
        Stream.of(holidayList, weekendList).flatMap(x -> x.stream()).collect(Collectors.toList());
    boolean isHolidayOrWeekend = false;
    for (LocalDate date : holidayAndWeekendList) {
      if (currentDate.equals(date)) {
        isHolidayOrWeekend = true;
        break;
      }
    }
    if (!isHolidayOrWeekend) {
      for (String employee : employeeIdList) {
        TimekeepingDto timekeepingDto = new TimekeepingDto(0D, 0D, currentDate, employee);
        timekeepingRepository.insertTimekeeping(timekeepingDto, ETimekeepingStatus.DAY_OFF.name());
      }
    }
  }
}