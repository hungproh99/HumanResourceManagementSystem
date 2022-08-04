package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.EmployeeDetailService;
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
import java.util.List;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING + REQUEST_DETAIL_MAPPING)
@Validated
public class EmployeeDetailController {
  @Autowired EmployeeDetailService employeeDetailService;

  @Autowired JwtUtils jwtUtils;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_MAIN_DETAIL)
  public ResponseEntity<?> findMainDetail(
      @NotBlank(message = "Please input employeeID!") @RequestParam String employeeID) {
    EmployeeDetailResponse employeeDetail = employeeDetailService.findMainDetail(employeeID);
    return ResponseEntity.ok(employeeDetail);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("working_info")
  @Validated
  public ResponseEntity<?> findWorkingInfo(HttpServletRequest request) {
    String employeeID = "";
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      employeeID = jwtUtils.getIdFromJwtToken(jwt);
    }
    WorkingInfoResponse workingInfo = employeeDetailService.findWorkingInfo(employeeID);
    return ResponseEntity.ok(workingInfo);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_TAX_AND_INSURANCE)
  @Validated
  public ResponseEntity<?> findTaxAndInsurance(@RequestParam String employeeID) {
    TaxAndInsuranceResponse taxAndInsurance = employeeDetailService.findTaxAndInsurance(employeeID);
    return ResponseEntity.ok(taxAndInsurance);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_ADDITIONAL_INFO)
  @Validated
  public ResponseEntity<?> findAdditionalInfo(@RequestParam String employeeID) {
    EmployeeAdditionalInfo additionalInfo = employeeDetailService.findAdditionalInfo(employeeID);
    return ResponseEntity.ok(additionalInfo);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_BANK_INFO)
  @Validated
  public ResponseEntity<?> findBankInfo(@RequestParam String employeeID) {
    BankResponse bank = employeeDetailService.findBankByEmployeeID(employeeID);
    return ResponseEntity.ok(bank);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_EDU_INFO)
  @Validated
  public ResponseEntity<?> findEducationInfo(@RequestParam String employeeID) {
    List<EducationResponse> educations =
        employeeDetailService.findEducationByEmployeeID(employeeID);
    return ResponseEntity.ok(educations);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_WORKING_HISTORY_INFO)
  @Validated
  public ResponseEntity<?> findWorkingHistoryByEmployeeID(@RequestParam String employeeID) {
    List<WorkingHistoryResponse> workingHistories =
        employeeDetailService.findWorkingHistoryByEmployeeID(employeeID);
    return ResponseEntity.ok(workingHistories);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping(URI_GET_RELATIVE_INFO)
  @Validated
  public ResponseEntity<?> findRelativeByEmployeeID(@RequestParam String employeeID) {
    List<RelativeInformationResponse> relatives =
        employeeDetailService.findRelativeByEmployeeID(employeeID);
    return ResponseEntity.ok(relatives);
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_MAIN_DETAIL)
  @Validated
  public ResponseEntity<?> updateEmployeeDetail(
      @Valid @RequestBody EmployeeDetailRequest employeeDetailRequest) {
    employeeDetailService.updateEmployeeDetail(employeeDetailRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @PutMapping("/add_info/update")
  @Validated
  public ResponseEntity<?> updateAdditionalInfo(
      @Valid @RequestBody EmployeeAdditionalInfoRequest employeeAdditionalInfoRequest) {
    employeeDetailService.updateAdditionalInfo(employeeAdditionalInfoRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_TAX_AND_INSURANCE)
  @Validated
  public ResponseEntity<?> updateTaxAndInsurance(
      @Valid @RequestBody TaxAndInsuranceRequest taxAndInsurance) {
    employeeDetailService.updateTaxAndInsurance(taxAndInsurance);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_BANK_INFO)
  @Validated
  public ResponseEntity<?> updateBankInfo(@Valid @RequestBody BankRequest bank) {
    employeeDetailService.updateBankInfo(bank);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_WORKING_HISTORY_INFO)
  @Validated
  public ResponseEntity<?> updateWorkingHistory(
      @Valid @RequestBody WorkingHistoryRequest workingHistory) {
    employeeDetailService.updateWorkingHistory(workingHistory);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_RELATIVE_INFO)
  @Validated
  public ResponseEntity<?> updateRelativeInfo(
      @Valid @RequestBody RelativeInformationRequest relativeInformation) {
    employeeDetailService.updateRelativeInfo(relativeInformation);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  @PutMapping(URI_UPDATE_EDUCATION_INFO)
  @Validated
  public ResponseEntity<?> updateEducationInfo(@Valid @RequestBody EducationRequest education) {
    employeeDetailService.updateEducationInfo(education);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }
}