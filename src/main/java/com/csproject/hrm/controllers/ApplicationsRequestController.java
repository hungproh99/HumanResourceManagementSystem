package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.ApplicationsRequestCreateRequest;
import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.RejectApplicationRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.ApplicationsRequestService;
import com.csproject.hrm.services.ChartService;
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
public class ApplicationsRequestController {
  @Autowired ApplicationsRequestService applicationsRequestService;
  @Autowired JwtUtils jwtUtils;
  @Autowired ChartService chartService;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_LIST_APPLICATION_REQUEST_RECEIVE)
  public ResponseEntity<?> getListApplicationsRequestReceive(
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
          applicationsRequestService.getAllApplicationRequestReceive(queryParam, employeeId));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, UNAUTHORIZED_ERROR);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_LIST_APPLICATION_REQUEST_SEND)
  public ResponseEntity<?> getListApplicationsRequestSend(
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
          applicationsRequestService.getAllApplicationRequestSend(queryParam, employeeId));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, UNAUTHORIZED_ERROR);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @PostMapping("create_application_request")
  public ResponseEntity<?> insertApplicationRequest(
      @Valid @RequestBody ApplicationsRequestRequest applicationsRequest) {
    applicationsRequestService.insertApplicationRequest(applicationsRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @PutMapping(URI_UPDATE_CHECK_APPLICATION_QUEST)
  public ResponseEntity<?> updateCheckedApplicationRequest(
      HttpServletRequest request,
      @Valid @RequestBody UpdateApplicationRequestRequest updateApplicationRequestRequest) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      applicationsRequestService.updateCheckedApplicationRequest(
          updateApplicationRequestRequest, employeeId);
      return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
    }
    throw new CustomErrorException(HttpStatus.BAD_REQUEST, UNAUTHORIZED_ERROR);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_status")
  public ResponseEntity<?> getAllRequestStatus() {
    return ResponseEntity.ok(applicationsRequestService.getAllRequestStatus());
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_type")
  public ResponseEntity<?> getAllRequestType() {
    return ResponseEntity.ok(
        applicationsRequestService.getAllRequestType());
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_name_by_id")
  public ResponseEntity<?> getAllRequestNameByRequestTypeID(@RequestParam Long requestTypeID) {
    return ResponseEntity.ok(
        applicationsRequestService.getAllRequestNameByRequestTypeID(requestTypeID));
  }

  //  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  //  @PostMapping(URI_UPDATE_IS_READ)
  //  public ResponseEntity<?> updateIsRead(@RequestParam Long requestId) {
  //    applicationsRequestService.updateIsRead(requestId);
  //    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  //  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_APPROVE_APPLICATION_REQUEST)
  public ResponseEntity<?> updateApproveApplicationRequest(@RequestParam Long requestId) {
    applicationsRequestService.updateApproveApplicationRequest(requestId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_REJECT_APPLICATION_REQUEST)
  public ResponseEntity<?> updateRejectApplicationRequest(
      @Valid @RequestBody RejectApplicationRequestRequest rejectApplicationRequestRequest) {
    applicationsRequestService.updateRejectApplicationRequest(rejectApplicationRequestRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @PostMapping("create_request")
  public ResponseEntity<?> createApplicationsRequest(
      @Valid @RequestBody ApplicationsRequestCreateRequest applicationsRequest) {
    applicationsRequestService.createApplicationsRequest(applicationsRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_paid_leave_remaining")
  public ResponseEntity<?> getPaidLeaveDayRemaining(
      @NotBlank(message = "EmployeeID must not be blank!") @RequestParam String employeeId) {
    return ResponseEntity.ok(applicationsRequestService.getPaidLeaveDayRemaining(employeeId));
  }

  @PostMapping(value = URI_DOWNLOAD_CSV_REQUEST_RECEIVE)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadCsvRequestReceive(
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
        "attachment; filename=\"Application_Request_" + timestamp.getTime() + ".csv\"");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      applicationsRequestService.exportApplicationRequestReceiveToCsv(
          servletResponse.getWriter(), queryParam, employeeId, listId);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_REQUEST_RECEIVE)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadExcelRequestReceive(
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
        "Content-Disposition",
        "attachment; filename=Application_Request_" + timestamp.getTime() + ".xlsx");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      applicationsRequestService.exportApplicationRequestReceiveByExcel(
          servletResponse, queryParam, employeeId, listId);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_CSV_REQUEST_SEND)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadCsvRequestSend(
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
        "attachment; filename=\"Application_Request_" + timestamp.getTime() + ".csv\"");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      applicationsRequestService.exportApplicationRequestSendToCsv(
          servletResponse.getWriter(), queryParam, employeeId, listId);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_REQUEST_SEND)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> downloadExcelRequestSend(
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
        "Content-Disposition",
        "attachment; filename=Application_Request_" + timestamp.getTime() + ".xlsx");
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      applicationsRequestService.exportApplicationRequestSendByExcel(
          servletResponse, queryParam, employeeId, listId);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = "get_all_paid_leave_reason")
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  public ResponseEntity<?> getAllPaidLeaveReason() {
    return ResponseEntity.ok(chartService.getAllPaidLeaveReason());
  }
}