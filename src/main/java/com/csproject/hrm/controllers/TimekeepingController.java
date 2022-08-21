package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponsesList;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.TimekeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
@Validated
public class TimekeepingController {
  @Autowired TimekeepingService timekeepingService;
  @Autowired JwtUtils jwtUtils;

  @GetMapping(URI_GET_LIST_TIMEKEEPING)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> getListAllTimekeeping(
      HttpServletRequest request,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      TimekeepingResponsesList timekeeping =
          timekeepingService.getListTimekeepingByManagement(queryParam, employeeId);
      return ResponseEntity.ok(timekeeping);
    }
    throw new CustomErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized");
  }

  @GetMapping("get_list_timekeeping_by_month")
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getTimekeepingByMonth(
      HttpServletRequest request,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    TimekeepingResponsesList timekeeping =
        timekeepingService.getListTimekeepingByEmployeeID(queryParam);
    return ResponseEntity.ok(timekeeping);
  }

  @PostMapping(value = URI_DOWNLOAD_CSV_TIMEKEEPING)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> downloadCsvTimekeeping(
      HttpServletResponse servletResponse,
      @RequestBody List<String> listId,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("text/csv; charset=UTF-8");
    servletResponse.addHeader(
        "Content-Disposition",
        "attachment; filename=\"Working_data_" + timestamp.getTime() + ".csv\"");
    timekeepingService.exportTimekeepingToCsv(servletResponse.getWriter(), queryParam, listId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_TIMEKEEPING)
  @PreAuthorize(value = "hasRole('MANAGER')")
  public ResponseEntity<?> downloadExcelTimekeeping(
      HttpServletResponse servletResponse,
      @RequestBody List<String> listId,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams)
      throws IOException {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("application/octet-stream");
    servletResponse.addHeader(
        "Content-Disposition", "attachment; filename=Working_data_" + timestamp.getTime() + ".xlsx");
    timekeepingService.exportTimekeepingToExcel(servletResponse, queryParam, listId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @GetMapping(URI_GET_DETAIL_TIMEKEEPING)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getListDetailTimekeeping(
      @NotBlank(message = "EmployeeID must not be blank!") @RequestParam String employeeID,
      @NotBlank(message = "Date must not be blank!") @RequestParam String date) {
    Optional<TimekeepingDetailResponse> timekeeping =
        timekeepingService.getTimekeepingByEmployeeIDAndDate(employeeID, date);
    return ResponseEntity.ok(timekeeping.orElse(null));
  }

//  @GetMapping(URI_GET_CHECKIN_CHECKOUT)
//  @PreAuthorize(value = "hasRole('MANAGER') or hasRole('USER')")
//  public ResponseEntity<?> checkInByEmployee(HttpServletRequest request) {
//    String headerAuth = request.getHeader(AUTHORIZATION);
//    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
//      String jwt = headerAuth.substring(7);
//      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
//      LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
//      timekeepingService.insertTimekeepingCheckInCheckOut(
//          employeeId, localDateTime.toLocalDate(), localDateTime.toLocalTime());
//    }
//    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
//  }
}