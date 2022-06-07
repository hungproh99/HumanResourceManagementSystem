package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.HumanManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.REQUEST_SUCCESS;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class HrmController {

  @Autowired HumanManagementService humanManagementService;

  @GetMapping(URI_GET_ALL_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getAllEmployee(@RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    HrmResponseList hrmResponseList = humanManagementService.getListHumanResource(queryParam);
    return ResponseEntity.ok(hrmResponseList);
  }

  @PostMapping(URI_INSERT_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> addEmployee(@RequestBody HrmRequest hrmRequest) {
    humanManagementService.insertEmployee(hrmRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(URI_INSERT_MULTI_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> addMultiEmployee(@RequestBody List<HrmRequest> hrmRequestList) {
    humanManagementService.insertMultiEmployee(hrmRequestList);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @GetMapping(URI_LIST_WORKING_TYPE)
  public ResponseEntity<?> getListWorkingType() {
    return ResponseEntity.ok(humanManagementService.getListWorkingType());
  }

  @GetMapping(URI_LIST_EMPLOYEE_TYPE)
  public ResponseEntity<?> getListEmployeeType() {
    return ResponseEntity.ok(humanManagementService.getListEmployeeType());
  }

  @GetMapping(URI_LIST_AREA)
  public ResponseEntity<?> getListArea() {
    return ResponseEntity.ok(humanManagementService.getListArea());
  }

  @GetMapping(URI_LIST_JOB)
  public ResponseEntity<?> getListJob() {
    return ResponseEntity.ok(humanManagementService.getListJob());
  }

  @GetMapping(URI_LIST_OFFICE)
  public ResponseEntity<?> getListOffice() {
    return ResponseEntity.ok(humanManagementService.getListOffice());
  }

  @GetMapping(URI_LIST_ROLE_TYPE)
  public ResponseEntity<?> getListRoleType() {
    return ResponseEntity.ok(humanManagementService.getListRoleType());
  }
}
