package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.TimekeepingDto;
import com.csproject.hrm.dto.dto.TimekeepingIdOvertimeTypeDto;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimekeepingRepositoryCustom {
  List<TimekeepingResponses> getListTimekeepingByManagement(
      QueryParam queryParam, List<EmployeeNameAndID> employeeNameAndIDList);

  List<TimekeepingResponses> getListTimekeepingByEmployeeID(QueryParam queryParam);

  List<TimekeepingResponses> getListTimekeepingToExport(QueryParam queryParam, List<String> list);

  Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, LocalDate date);

  List<ListTimekeepingStatusResponse> getListTimekeepingStatus(Long timekeepingID);

  List<CheckInCheckOutResponse> getCheckInCheckOutByTimekeepingID(Long timekeepingID);

  int countListTimekeepingByManagement(
      QueryParam queryParam, List<EmployeeNameAndID> employeeNameAndIDList);

  void insertTimekeepingByEmployeeId(String employeeId, LocalDate startDate, LocalDate endDate);

  void upsertTimekeepingStatusByEmployeeIdAndRangeDate(
      String employeeId,
      LocalDate startDate,
      LocalDate endDate,
      Long reason,
      long oldTimekeepingStatus,
      long newTimekeepingStatus);

  void deleteTimekeepingStatusByEmployeeIdAndDate(
      String employeeId, LocalDate date, long oldTimekeepingStatus);

  void insertOvertimeByEmployeeIdAndRangeDate(
      List<TimekeepingIdOvertimeTypeDto> timekeepingIdOvertimeTypeDtoList,
      LocalTime startTime,
      LocalTime endTime);

  Long insertTimekeeping(TimekeepingDto timekeepingDto);

  LocalTime getFirstTimeCheckInByTimekeeping(LocalDate date, String employeeId);

  LocalTime getLastTimeCheckOutByTimekeeping(LocalDate date, String employeeId);

  int countPaidLeaveOfEmployeeByYear(String employeeID);

  Integer countOvertimeOfEmployeeByMonth(String employeeID);

  Integer countOvertimeOfEmployeeByYear(String employeeID);

  void updatePointPerDay(List<TimekeepingDto> timekeepingDtoList);

  boolean checkExistDateInTimekeeping(LocalDate date, String employeeId);

  int countTimeDayOffPerMonthByEmployeeId(
      LocalDate firstDate, LocalDate lastDate, String employeeId);

  int countTimePaidLeavePerMonthByEmployeeId(
      LocalDate firstDate, LocalDate lastDate, String employeeId);

  Double countPointDayWorkPerMonthByEmployeeId(
      LocalDate firstDate, LocalDate lastDate, String employeeId);

  //  boolean checkExistTimekeepingByTimekeepingId(Long timekeepingId);

  List<TimekeepingIdOvertimeTypeDto> getListTimekeepingIdOvertimeTypeDto(
      String employeeId, LocalDate startDate, LocalDate endDate);
}