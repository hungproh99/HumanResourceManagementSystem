package com.csproject.hrm.controllers;

import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.SalaryMonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class SalaryMonthlyController {
  @Autowired SalaryMonthlyService salaryMonthlyService;
  @Autowired JwtUtils jwtUtils;

  @GetMapping(URI_GET_ALL_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getListAllSalaryMonthly(
      HttpServletRequest request, @RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    String headerAuth = request.getHeader(AUTHORIZATION);
    boolean isManager = false;
    if (request.isUserInRole("MANAGER")) {
      isManager = true;
    }
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      return ResponseEntity.ok(
          salaryMonthlyService.getAllSalaryMonthly(queryParam, employeeId, isManager));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, UNAUTHORIZED_ERROR);
  }

  @GetMapping(URI_GET_SALARY_MONTHLY_DETAIL)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getSalaryMonthlyDetail(@RequestParam Long salaryId) {
    if (salaryId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NULL_PARAMETER);
    }
    return ResponseEntity.ok(
        salaryMonthlyService.getSalaryMonthlyDetailBySalaryMonthlyId(salaryId));
  }

  @PostMapping(value = URI_DOWNLOAD_CSV_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadCsvSalaryMonthly(
      HttpServletResponse servletResponse,
      @RequestBody List<Long> listId,
      @RequestParam Map<String, String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("text/csv; charset=UTF-8");
    servletResponse.addHeader(
        "Content-Disposition",
        "attachment; filename=\"Salary_Monthly_" + timestamp.getTime() + ".csv\"");
    salaryMonthlyService.exportSalaryMonthlyToCsv(servletResponse.getWriter(), queryParam, listId);

    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadExcelSalaryMonthly(
      HttpServletResponse servletResponse,
      @RequestBody List<Long> listId,
      @RequestParam Map<String, String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("application/octet-stream");
    servletResponse.addHeader(
        "Content-Disposition", "attachment; filename=employees_" + timestamp.getTime() + ".xlsx");
    salaryMonthlyService.exportSalaryMonthlyExcel(servletResponse, queryParam, listId);

    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @GetMapping("/test")
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getTest() {
    LocalDate startDate = LocalDate.of(2022,10,01);
    LocalDate endDate = LocalDate.of(2022,10,31);
    salaryMonthlyService.upsertSalaryMonthlyByEmployeeIdList(startDate, endDate);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }
}
