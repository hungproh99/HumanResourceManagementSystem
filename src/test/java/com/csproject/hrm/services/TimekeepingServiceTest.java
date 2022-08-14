package com.csproject.hrm.services;

import com.csproject.hrm.common.excel.ExcelExportTimekeeping;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.dto.dto.TimekeepingDto;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.impl.TimekeepingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.csproject.hrm.common.sample.DataSample.*;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TimekeepingServiceTest {
  @Autowired @Mock TimekeepingRepository timekeepingRepository;
  @Autowired @Mock EmployeeDetailRepository employeeDetailRepository;
  @Autowired @Mock EmployeeRepository employeeRepository;
  @Autowired @Mock OvertimeRepository overtimeRepository;
  @Autowired @Mock GeneralFunction generalFunction;
  @Autowired @Mock SalaryCalculator salaryCalculator;
  @Autowired @Mock SalaryContractRepository salaryContractRepository;
  @Autowired @Mock CheckInCheckOutRepository checkInCheckOutRepository;
  @Autowired @Mock ListTimekeepingStatusRepository listTimekeepingStatusRepository;
  @Autowired @Mock Writer writer;
  @Autowired ExcelExportTimekeeping excelExportTimekeeping;
  @Autowired @Mock HttpServletResponse httpServletResponse;
  @Autowired @Mock ServletOutputStream outputStream;
  @InjectMocks TimekeepingServiceImpl timekeepingService;

  @Test
  void test_getListTimekeepingByManagement() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeDetailRepository.getAllEmployeeByManagerID(employeeId))
        .thenReturn(new ArrayList<>());
    timekeepingService.getListTimekeepingByManagement(QueryParam.defaultParam(), employeeId);
  }

  @Test
  void test_getListTimekeepingByManagement_IdNotExist() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            timekeepingService.getListTimekeepingByManagement(
                QueryParam.defaultParam(), employeeId));
  }

  @Test
  void test_exportTimekeepingToCsv() {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    when(employeeDetailRepository.checkEmployeeIDIsExists("huynq100")).thenReturn(true);
    when(timekeepingRepository.getListTimekeepingToExport(QueryParam.defaultParam(), list))
        .thenReturn(TIMEKEEPING_RESPONSES);
    timekeepingService.exportTimekeepingToCsv(writer, QueryParam.defaultParam(), list);
  }

  @Test
  void test_exportTimekeepingToCsv_NullList() {
    List<String> list = new ArrayList<>();
    assertThrows(
        CustomDataNotFoundException.class,
        () -> timekeepingService.exportTimekeepingToCsv(writer, QueryParam.defaultParam(), list));
  }

  @Test
  void test_exportTimekeepingToCsv_IdNotExist() {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    when(employeeDetailRepository.checkEmployeeIDIsExists("huynq100")).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () -> timekeepingService.exportTimekeepingToCsv(writer, QueryParam.defaultParam(), list));
  }

  @Test
  void test_exportTimekeepingToExcel() throws IOException {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    when(employeeDetailRepository.checkEmployeeIDIsExists("huynq100")).thenReturn(true);
    doReturn(TIMEKEEPING_RESPONSES)
        .when(timekeepingRepository)
        .getListTimekeepingToExport(QueryParam.defaultParam(), list);
    when(httpServletResponse.getOutputStream()).thenReturn(outputStream);
    timekeepingService.exportTimekeepingToExcel(
        httpServletResponse, QueryParam.defaultParam(), list);
  }

  @Test
  void test_exportTimekeepingToExcel_NullList() {
    List<String> list = new ArrayList<>();
    assertThrows(
        CustomDataNotFoundException.class,
        () ->
            timekeepingService.exportTimekeepingToExcel(
                httpServletResponse, QueryParam.defaultParam(), list));
  }

  @Test
  void test_exportTimekeepingToExcel_IdNotExist() {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    when(employeeDetailRepository.checkEmployeeIDIsExists("huynq100")).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            timekeepingService.exportTimekeepingToExcel(
                httpServletResponse, QueryParam.defaultParam(), list));
  }

  @Test
  void test_upsertPointPerDay_Exist() {
    LocalDate currentDate = LocalDate.now();
    String employeeId = "huynq100";
    List<String> list = new ArrayList<>();
    list.add(employeeId);
    when(employeeRepository.getAllEmployeeIdActive()).thenReturn(list);
    when(timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId))
        .thenReturn(true);
    when(generalFunction.readWorkingTimeData()).thenReturn(WORKING_TIME_DATA_DTO);
    when(generalFunction.readOvertimeData()).thenReturn(OVERTIME_DATA_DTO);
    when(salaryContractRepository.getSalaryContractByEmployeeId(employeeId))
        .thenReturn(Optional.of(SALARY_CONTRACT_DTO_FULL_TIME));
    when(overtimeRepository.getOvertimeByEmployeeIdAndDate(currentDate, employeeId))
        .thenReturn(Optional.of(OVERTIME_DTO));
    when(timekeepingRepository.getFirstTimeCheckInByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now());
    when(timekeepingRepository.getLastTimeCheckOutByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now());

    timekeepingService.upsertPointPerDay(LocalDate.now());
  }

  @Test
  void test_upsertPointPerDay_NotExist() {
    LocalDate currentDate = LocalDate.now();
    String employeeId = "huynq100";
    List<String> list = new ArrayList<>();
    list.add(employeeId);
    List<LocalDate> listDate = new ArrayList<>();
    listDate.add(currentDate);
    TimekeepingDto timekeepingDto = new TimekeepingDto(0D, 0D, currentDate, employeeId);
    when(employeeRepository.getAllEmployeeIdActive()).thenReturn(list);
    when(timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId))
        .thenReturn(false);
    when(generalFunction.readWorkingTimeData()).thenReturn(WORKING_TIME_DATA_DTO);
    when(generalFunction.readOvertimeData()).thenReturn(OVERTIME_DATA_DTO);
    when(salaryCalculator.getAllHolidayByRange(
            currentDate.with(firstDayOfMonth()), currentDate.with(lastDayOfMonth())))
        .thenReturn(listDate);
    when(salaryCalculator.getAllWeekendByRange(
            currentDate.with(firstDayOfMonth()), currentDate.with(lastDayOfMonth())))
        .thenReturn(listDate);

    timekeepingService.upsertPointPerDay(LocalDate.now());
  }

  @Test
  void test_upsertPointPerDay_NotExist_NotHolidayAndWeekend() {
    LocalDate currentDate = LocalDate.now();
    String employeeId = "huynq100";
    List<String> list = new ArrayList<>();
    list.add(employeeId);
    List<LocalDate> listDate = new ArrayList<>();
    listDate.add(currentDate);
    TimekeepingDto timekeepingDto = new TimekeepingDto(0D, 0D, currentDate, employeeId);
    when(employeeRepository.getAllEmployeeIdActive()).thenReturn(list);
    when(timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId))
        .thenReturn(false);
    when(generalFunction.readWorkingTimeData()).thenReturn(WORKING_TIME_DATA_DTO);
    when(generalFunction.readOvertimeData()).thenReturn(OVERTIME_DATA_DTO);
    when(salaryCalculator.getAllHolidayByRange(
            currentDate.with(firstDayOfMonth()), currentDate.with(lastDayOfMonth())))
        .thenReturn(new ArrayList<>());
    when(salaryCalculator.getAllWeekendByRange(
            currentDate.with(firstDayOfMonth()), currentDate.with(lastDayOfMonth())))
        .thenReturn(new ArrayList<>());

    timekeepingService.upsertPointPerDay(LocalDate.now());
  }

  @Test
  void test_upsertPointPerDay_PartTime() {
    LocalDate currentDate = LocalDate.now();
    String employeeId = "huynq100";
    List<String> list = new ArrayList<>();
    list.add(employeeId);
    when(employeeRepository.getAllEmployeeIdActive()).thenReturn(list);
    when(timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId))
        .thenReturn(true);
    when(generalFunction.readWorkingTimeData()).thenReturn(WORKING_TIME_DATA_DTO);
    when(generalFunction.readOvertimeData()).thenReturn(OVERTIME_DATA_DTO);
    when(salaryContractRepository.getSalaryContractByEmployeeId(employeeId))
        .thenReturn(Optional.of(SALARY_CONTRACT_DTO_PART_TIME));
    when(overtimeRepository.getOvertimeByEmployeeIdAndDate(currentDate, employeeId))
        .thenReturn(Optional.of(OVERTIME_DTO));
    when(timekeepingRepository.getFirstTimeCheckInByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now());
    when(timekeepingRepository.getLastTimeCheckOutByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now());

    timekeepingService.upsertPointPerDay(LocalDate.now());
  }

  @Test
  void test_upsertPointPerDay_LeaveSoon() {
    LocalDate currentDate = LocalDate.now();
    String employeeId = "huynq100";
    List<String> list = new ArrayList<>();
    list.add(employeeId);
    when(employeeRepository.getAllEmployeeIdActive()).thenReturn(list);
    when(timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId))
        .thenReturn(true);
    when(generalFunction.readWorkingTimeData()).thenReturn(WORKING_TIME_DATA_DTO);
    when(generalFunction.readOvertimeData()).thenReturn(OVERTIME_DATA_DTO);
    when(salaryContractRepository.getSalaryContractByEmployeeId(employeeId))
        .thenReturn(Optional.of(SALARY_CONTRACT_DTO_PART_TIME));
    when(overtimeRepository.getOvertimeByEmployeeIdAndDate(currentDate, employeeId))
        .thenReturn(Optional.of(OVERTIME_DTO));
    when(timekeepingRepository.getFirstTimeCheckInByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now());
    when(timekeepingRepository.getLastTimeCheckOutByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now().minus(1, ChronoUnit.HOURS));

    timekeepingService.upsertPointPerDay(LocalDate.now());
  }

  @Test
  void test_upsertPointPerDay_SalaryContractEmpty() {
    LocalDate currentDate = LocalDate.now();
    String employeeId = "huynq100";
    List<String> list = new ArrayList<>();
    list.add(employeeId);
    when(employeeRepository.getAllEmployeeIdActive()).thenReturn(list);
    when(timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId))
        .thenReturn(true);
    when(generalFunction.readWorkingTimeData()).thenReturn(WORKING_TIME_DATA_DTO);
    when(generalFunction.readOvertimeData()).thenReturn(OVERTIME_DATA_DTO);
    when(salaryContractRepository.getSalaryContractByEmployeeId(employeeId))
        .thenReturn(Optional.empty());
    assertThrows(
        CustomErrorException.class, () -> timekeepingService.upsertPointPerDay(LocalDate.now()));
  }

  @Test
  void test_upsertPointPerDay_MaxPoint() {
    LocalDate currentDate = LocalDate.now();
    String employeeId = "huynq100";
    List<String> list = new ArrayList<>();
    list.add(employeeId);
    when(employeeRepository.getAllEmployeeIdActive()).thenReturn(list);
    when(timekeepingRepository.checkExistDateInTimekeeping(currentDate, employeeId))
        .thenReturn(true);
    when(generalFunction.readWorkingTimeData()).thenReturn(WORKING_TIME_DATA_DTO_MAX_POINT);
    when(generalFunction.readOvertimeData()).thenReturn(OVERTIME_DATA_DTO);
    when(salaryContractRepository.getSalaryContractByEmployeeId(employeeId))
        .thenReturn(Optional.of(SALARY_CONTRACT_DTO_FULL_TIME));
    when(overtimeRepository.getOvertimeByEmployeeIdAndDate(currentDate, employeeId))
        .thenReturn(Optional.of(OVERTIME_DTO));
    when(timekeepingRepository.getFirstTimeCheckInByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now());
    when(timekeepingRepository.getLastTimeCheckOutByTimekeeping(currentDate, employeeId))
        .thenReturn(LocalTime.now());
    timekeepingService.upsertPointPerDay(LocalDate.now());
  }

}
