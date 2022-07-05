package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.ApplicationsRequestRequest;
import com.csproject.hrm.dto.request.UpdateApplicationRequestRequest;
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
  public ResponseEntity<?> updateApplicationRequest(
      @RequestBody UpdateApplicationRequestRequest updateApplicationRequestRequest) {
    applicationsRequestService.updateStatusApplicationRequest(updateApplicationRequestRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_type")
  public ResponseEntity<?> getAllRequestType() {
    return ResponseEntity.ok(applicationsRequestService.getAllRequestType());
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_all_request_name_by_id")
  public ResponseEntity<?> getAllRequestNameByRequestTypeID(@RequestParam Long requestTypeID) {
    return ResponseEntity.ok(
        applicationsRequestService.getAllRequestNameByRequestTypeID(requestTypeID));
  }
}