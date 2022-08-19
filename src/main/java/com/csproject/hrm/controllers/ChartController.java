package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.HolidayCalendarRequest;
import com.csproject.hrm.dto.response.EmployeeNameAndID;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
@Validated
public class ChartController {
  @Autowired ChartService chartService;
  @Autowired EmployeeDetailService employeeDetailService;
  @Autowired JwtUtils jwtUtils;
  @Autowired HolidayCalenderService holidayCalenderService;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @GetMapping("get_general_data_chart")
  public ResponseEntity<?> getGeneralEmployeeDataForChart(HttpServletRequest request) {
    String areaName = "";
    String headerAuth = request.getHeader(AUTHORIZATION);
    String employeeId = "huynq1";
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      employeeId = jwtUtils.getIdFromJwtToken(jwt);
      areaName =
          Objects.requireNonNullElse(chartService.getAreaNameByEmployeeID(employeeId), areaName);
    }

    return ResponseEntity.ok(
        chartService.getGeneralEmployeeDataForChartByAreaName(employeeId, areaName));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_organizational")
  public ResponseEntity<?> getOrganizational() {
    return ResponseEntity.ok(chartService.getOrganizational());
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @GetMapping("get_leave_company_reason_chart")
  public ResponseEntity<?> getLeaveCompanyReasonByYear(
      HttpServletRequest request,
      @Positive(message = "Year must be a positive number!") @RequestParam Integer year) {
    String areaName = "";
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      areaName =
          Objects.requireNonNullElse(chartService.getAreaNameByEmployeeID(employeeId), areaName);
    }

    return ResponseEntity.ok(chartService.getLeaveCompanyReasonByYearAndAreaName(year, areaName));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_paid_leave_reason_chart")
  public ResponseEntity<?> getPaidLeaveReasonByYear(
      HttpServletRequest request,
      @Positive(message = "Year must be a positive number!") @RequestParam Integer year,
      @NotBlank(message = "EmployeeID must not be blank!") @RequestParam String employeeId) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    return ResponseEntity.ok(
        chartService.getPaidLeaveReasonByYearAndManagerID(headerAuth, year, employeeId.trim()));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_salary_structure_chart")
  public ResponseEntity<?> getSalaryStructureByDate(
      HttpServletRequest request,
      @NotBlank(message = "Date must not be blank!") @RequestParam String date,
      @NotBlank(message = "EmployeeID must not be blank!") @RequestParam String employeeId) {
    LocalDate date1;
    try {
      date1 = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (Exception e) {
      throw new CustomErrorException("\"" + date + "\" must in format yyyy-MM-dd!");
    }
    String headerAuth = request.getHeader(AUTHORIZATION);
    if ("".equals(employeeId.trim())) {
      if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
        String jwt = headerAuth.substring(7);
        employeeId = jwtUtils.getIdFromJwtToken(jwt);
      }
    }

    return ResponseEntity.ok(chartService.getSalaryStructureByDateAndEmployeeID(date1, employeeId));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(
      value = "get_salary_history_chart",
      params = {"type", "date", "employeeId"})
  public ResponseEntity<?> getSalaryHistoryByDateAndType(
      HttpServletRequest request,
      @NotBlank(message = "Type must not be blank!") @RequestParam String type,
      @NotBlank(message = "Date must not be blank!") @RequestParam String date,
      @NotBlank(message = "EmployeeID must not be blank!") @RequestParam String employeeId) {
    LocalDate date1;
    try {
      date1 = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (Exception e) {
      throw new CustomErrorException("\"" + date + "\" must in format yyyy-MM-dd!");
    }
    String headerAuth = request.getHeader(AUTHORIZATION);
    if ("".equals(employeeId.trim())) {
      if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
        String jwt = headerAuth.substring(7);
        employeeId = jwtUtils.getIdFromJwtToken(jwt);
      }
    }

    return ResponseEntity.ok(
        chartService.getSalaryHistoryByDateAndEmployeeIDAndType(date1, employeeId, type));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(
      value = "get_salary_history_chart",
      params = {"type", "employeeId"})
  public ResponseEntity<?> getSalaryHistoryByDateAndType(
      HttpServletRequest request,
      @NotBlank(message = "Type must not be blank!") @RequestParam String type,
      @NotBlank(message = "EmployeeID must not be blank!") @RequestParam String employeeId) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    if ("".equals(employeeId.trim())) {
      if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
        String jwt = headerAuth.substring(7);
        employeeId = jwtUtils.getIdFromJwtToken(jwt);
      }
    }

    return ResponseEntity.ok(
        chartService.getSalaryHistoryByDateAndEmployeeIDAndType(
            chartService.getStartDateOfContract(employeeId), employeeId, type));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @GetMapping(value = "get_employee_by_manager")
  public ResponseEntity<?> getAllEmployeeByManagerID(HttpServletRequest request) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    String employeeId = "";
    //    LinkedHashMap userDetails = new LinkedHashMap<>();
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      employeeId = jwtUtils.getIdFromJwtToken(jwt);
      //      userDetails = jwtUtils.getClaimFromJwtToken(jwt);
    }
    //    EmployeeNameAndID employee =
    //        new EmployeeNameAndID((String) userDetails.get("id"), (String)
    // userDetails.get("fullName"));

    List<EmployeeNameAndID> employeeList =
        employeeDetailService.getAllEmployeeByManagerID(employeeId);

    //    employeeList.add(0, employee);
    return ResponseEntity.ok(employeeList);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(value = "get_all_holiday")
  public ResponseEntity<?> getAllHolidayByYear(
      @Positive(message = "Year must be a positive number!") @RequestParam Integer year) {
    LocalDate date = LocalDate.of(year, 1, 1);
    return ResponseEntity.ok(holidayCalenderService.getAllHolidayByYear(date));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PostMapping(value = "insert_holiday")
  public ResponseEntity<?> insertHolidayByYear(
      @Valid @RequestBody HolidayCalendarRequest holidayCalendarRequest) {
    holidayCalenderService.insertHolidayCalendar(holidayCalendarRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }
}