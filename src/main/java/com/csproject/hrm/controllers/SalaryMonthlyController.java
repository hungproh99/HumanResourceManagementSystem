package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
@Validated
public class SalaryMonthlyController {
  @Autowired SalaryMonthlyService salaryMonthlyService;
  @Autowired JwtUtils jwtUtils;

  @GetMapping(URI_GET_ALL_PERSONAL_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getListAllPersonalSalaryMonthly(
      HttpServletRequest request,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      return ResponseEntity.ok(
          salaryMonthlyService.getAllSalaryMonthlyForPersonal(queryParam, employeeId));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, UNAUTHORIZED_ERROR);
  }

  @GetMapping(URI_GET_ALL_MANAGEMENT_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getListAllManagementSalaryMonthly(
      HttpServletRequest request,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      return ResponseEntity.ok(
          salaryMonthlyService.getAllSalaryMonthlyForManagement(queryParam, employeeId));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, UNAUTHORIZED_ERROR);
  }

  @GetMapping(URI_GET_SALARY_MONTHLY_DETAIL)
  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getSalaryMonthlyDetail(
      @Positive(message = "salaryId must be a positive number!") @RequestParam Long salaryId) {
    return ResponseEntity.ok(
        salaryMonthlyService.getSalaryMonthlyDetailBySalaryMonthlyId(salaryId));
  }

  @PostMapping(value = URI_DOWNLOAD_CSV_PERSONAL_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadCsvPersonalSalaryMonthly(
      HttpServletRequest request,
      HttpServletResponse servletResponse,
      @RequestBody List<Long> listId,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("text/csv; charset=UTF-8");
    servletResponse.addHeader(
        "Content-Disposition",
        "attachment; filename=\"Salary_Monthly_" + timestamp.getTime() + ".csv\"");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      salaryMonthlyService.exportPersonalSalaryMonthlyToCsv(
          servletResponse.getWriter(), queryParam, listId, employeeId);
      return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Can't export CSV");
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_PERSONAL_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadExcelPersonalSalaryMonthly(
      HttpServletRequest request,
      HttpServletResponse servletResponse,
      @RequestBody List<Long> listId,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("application/octet-stream");
    servletResponse.addHeader(
        "Content-Disposition", "attachment; filename=employees_" + timestamp.getTime() + ".xlsx");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      salaryMonthlyService.exportPersonalSalaryMonthlyExcel(
          servletResponse, queryParam, listId, employeeId);
      return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Can't export Excel");
  }

  @PostMapping(value = URI_DOWNLOAD_CSV_MANAGEMENT_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadCsvManagementSalaryMonthly(
      HttpServletRequest request,
      HttpServletResponse servletResponse,
      @RequestBody List<Long> listId,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("text/csv; charset=UTF-8");
    servletResponse.addHeader(
        "Content-Disposition",
        "attachment; filename=\"Salary_Monthly_" + timestamp.getTime() + ".csv\"");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      salaryMonthlyService.exportManagementSalaryMonthlyToCsv(
          servletResponse.getWriter(), queryParam, listId, employeeId);
      return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Can't export CSV");
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_MANAGEMENT_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadExcelManagementSalaryMonthly(
      HttpServletRequest request,
      HttpServletResponse servletResponse,
      @RequestBody List<Long> listId,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("application/octet-stream");
    servletResponse.addHeader(
        "Content-Disposition", "attachment; filename=employees_" + timestamp.getTime() + ".xlsx");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      salaryMonthlyService.exportManagementSalaryMonthlyExcel(
          servletResponse, queryParam, listId, employeeId);
      return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Can't export Excel");
  }

  @PutMapping(value = URI_UPDATE_DEDUCTION_SALARY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> updateDeductionSalary(
      @Valid @RequestBody DeductionSalaryRequest deductionSalaryRequest) {
    salaryMonthlyService.updateDeductionSalary(deductionSalaryRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @DeleteMapping(value = URI_DELETE_DEDUCTION_SALARY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> deleteDeductionSalary(
      @Positive(message = "deductionId must be a positive number!") @RequestParam
          Long deductionId) {
    salaryMonthlyService.deleteDeductionSalary(deductionId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PutMapping(value = URI_UPDATE_BONUS_SALARY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> updateBonusSalary(
      @Valid @RequestBody BonusSalaryRequest bonusSalaryRequest) {
    salaryMonthlyService.updateBonusSalary(bonusSalaryRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @DeleteMapping(value = URI_DELETE_BONUS_SALARY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> deleteBonusSalary(
      @Positive(message = "bonusId must be a positive number!") @RequestParam Long bonusId) {
    salaryMonthlyService.deleteBonusSalary(bonusId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PutMapping(value = URI_UPDATE_ADVANCE_SALARY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> updateAdvanceSalary(
      @Valid @RequestBody AdvanceSalaryRequest advanceSalaryRequest) {
    salaryMonthlyService.updateAdvanceSalary(advanceSalaryRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @DeleteMapping(value = URI_DELETE_ADVANCE_SALARY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> deleteAdvanceSalary(
      @Positive(message = "advanceId must be a positive number!") @RequestParam Long advanceId) {
    salaryMonthlyService.deleteAdvanceSalary(advanceId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PutMapping(value = URI_UPDATE_APPROVE_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> updateApproveSalaryMonthly(
      @Positive(message = "salaryMonthlyId must be a positive number!") @RequestParam
          Long salaryMonthlyId) {
    salaryMonthlyService.updateApproveSalaryMonthly(salaryMonthlyId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PutMapping(value = URI_UPDATE_CHECKED_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> updateCheckedSalaryMonthly(
      HttpServletRequest request,
      @Valid @RequestBody UpdateSalaryMonthlyRequest updateSalaryMonthlyRequest) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      salaryMonthlyService.updateCheckedSalaryMonthly(updateSalaryMonthlyRequest, employeeId);
      return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
    }
    throw new CustomErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized this employee");
  }

  @PutMapping(value = URI_UPDATE_REJECT_SALARY_MONTHLY)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> updateRejectSalaryMonthly(
      @Valid @RequestBody RejectSalaryMonthlyRequest rejectSalaryMonthlyRequest) {
    salaryMonthlyService.updateRejectSalaryMonthly(rejectSalaryMonthlyRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @GetMapping(value = URI_GET_LIST_DEDUCTION_TYPE)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> getListDeductionType() {
    return ResponseEntity.ok(salaryMonthlyService.getListDeductionTypeDto());
  }

  @GetMapping(value = URI_GET_LIST_BONUS_TYPE)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> getListBonusType() {
    return ResponseEntity.ok(salaryMonthlyService.getListBonusTypeDto());
  }

  //  @GetMapping("/test")
  //  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  //  public ResponseEntity<?> getTest() {
  //    LocalDate startDate = LocalDate.of(2022, 10, 01);
  //    LocalDate endDate = LocalDate.of(2022, 10, 31);
  //    salaryMonthlyService.upsertSalaryMonthlyByEmployeeIdList(startDate, endDate);
  //    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  //  }
}
