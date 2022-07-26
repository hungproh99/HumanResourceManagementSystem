package com.csproject.hrm.controllers;

import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.csproject.hrm.common.constant.Constants.AUTHORIZATION;
import static com.csproject.hrm.common.constant.Constants.BEARER;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class ChartController {
  @Autowired ChartService chartService;
  @Autowired JwtUtils jwtUtils;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_general_data_chart")
  public ResponseEntity<?> getGeneralEmployeeDataForChart(HttpServletRequest request) {
    String areaName = "";
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      areaName =
          Objects.requireNonNullElse(chartService.getAreaNameByEmployeeID(employeeId), areaName);
    }

    return ResponseEntity.ok(chartService.getGeneralEmployeeDataForChartByAreaName(areaName));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_organizational")
  public ResponseEntity<?> getOrganizational() {
    return ResponseEntity.ok(chartService.getOrganizational());
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_leave_company_reason_chart")
  public ResponseEntity<?> getLeaveCompanyReasonByYear(
      HttpServletRequest request, @RequestParam Integer year) {
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
      HttpServletRequest request, @RequestParam Integer year) {
    String areaName = "";
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      areaName =
          Objects.requireNonNullElse(chartService.getAreaNameByEmployeeID(employeeId), areaName);
    }

    return ResponseEntity.ok(chartService.getPaidLeaveReasonByYearAndAreaName(year, areaName));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_salary_structure_chart")
  public ResponseEntity<?> getSalaryStructureByDate(
      HttpServletRequest request, @RequestParam String date) {
    LocalDate date1;
    try {
      date1 = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (Exception e) {
      throw new CustomErrorException("\"" + date + "\" must in format yyyy-MM-dd!");
    }
    String headerAuth = request.getHeader(AUTHORIZATION);
    String employeeId = "";
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      employeeId = jwtUtils.getIdFromJwtToken(jwt);
    }

    return ResponseEntity.ok(chartService.getSalaryStructureByDateAndEmployeeID(date1, employeeId));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(
      value = "get_salary_history_chart",
      params = {"type", "date"})
  public ResponseEntity<?> getSalaryHistoryByDateAndType(
      HttpServletRequest request, @RequestParam String type, @RequestParam String date) {
    LocalDate date1;
    try {
      date1 = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (Exception e) {
      throw new CustomErrorException(date + "must have format yyyy-MM-dd!");
    }
    String headerAuth = request.getHeader(AUTHORIZATION);
    String employeeId = null;
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      employeeId = jwtUtils.getIdFromJwtToken(jwt);
    }

    return ResponseEntity.ok(
        chartService.getSalaryHistoryByDateAndEmployeeIDAndType(date1, employeeId, type));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(
      value = "get_salary_history_chart",
      params = {"type"})
  public ResponseEntity<?> getSalaryHistoryByDateAndType(
      HttpServletRequest request, @RequestParam String type) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    String employeeId = null;
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      employeeId = jwtUtils.getIdFromJwtToken(jwt);
    }

    return ResponseEntity.ok(
        chartService.getSalaryHistoryByDateAndEmployeeIDAndType(
            chartService.getStartDateOfContract(employeeId), employeeId, type));
  }
}