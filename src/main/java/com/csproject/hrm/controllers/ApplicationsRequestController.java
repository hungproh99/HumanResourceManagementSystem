package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.ApplicationsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class ApplicationsRequestController {
  @Autowired ApplicationsRequestService applicationsRequestService;
  @Autowired JwtUtils jwtUtils;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_LIST_APPLICATION_REQUEST_RECEIVE)
  public ResponseEntity<?> getListApplicationsRequestReceive(
      HttpServletRequest request, @RequestParam Map<String, String> allRequestParams) {
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
      HttpServletRequest request, @RequestParam Map<String, String> allRequestParams) {
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
      @RequestBody ApplicationsRequestRequest applicationsRequest) {
    applicationsRequestService.insertApplicationRequest(applicationsRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @PostMapping(URI_UPDATE_STATUS_APPLICATION_QUEST)
  public ResponseEntity<?> updateStatusApplicationRequest(
      @RequestBody UpdateApplicationRequestRequest updateApplicationRequestRequest) {
    applicationsRequestService.updateStatusApplicationRequest(updateApplicationRequestRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_status")
  public ResponseEntity<?> getAllRequestStatus() {
    return ResponseEntity.ok(applicationsRequestService.getAllRequestStatus());
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_type")
  public ResponseEntity<?> getAllRequestType(@RequestParam String employeeId) {
    return ResponseEntity.ok(
        applicationsRequestService.getAllRequestTypeByEmployeeLevel(employeeId));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_name_by_id")
  public ResponseEntity<?> getAllRequestNameByRequestTypeID(@RequestParam Long requestTypeID) {
    return ResponseEntity.ok(
        applicationsRequestService.getAllRequestNameByRequestTypeID(requestTypeID));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @PostMapping(URI_UPDATE_IS_READ)
  public ResponseEntity<?> updateIsRead(@RequestParam Long requestId) {
    applicationsRequestService.updateIsRead(requestId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PostMapping(URI_UPDATE_APPLICATION_REQUEST)
  public ResponseEntity<?> updateApplicationRequest(@RequestParam Long requestId) {
    applicationsRequestService.updateApplicationRequest(requestId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @PostMapping("create_request")
  public ResponseEntity<?> createApplicationsRequest(
      @RequestBody ApplicationsRequestRequestC applicationsRequest) {
    applicationsRequestService.createApplicationsRequest(applicationsRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  //  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  //  @PutMapping("test2")
  //  public ResponseEntity<?> createApproveTaxEnrollment(
  //      @RequestBody String taxNameList, @RequestBody String employeeId) {
  //    System.out.println(taxNameList);
  //    //    applicationsRequestService.createApproveTaxEnrollment(taxNameList, employeeId);
  //    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  //  }
}