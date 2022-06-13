package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.*;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.services.EmployeeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.csproject.hrm.common.constant.Constants.REQUEST_SUCCESS;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING + "/employee/detail")
public class EmployeeDetailController {
  @Autowired
  EmployeeDetailService employeeDetailService;
  
  
  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("main")
  public ResponseEntity<?> findMainDetail(@RequestParam String employeeID) {
    List<EmployeeDetailResponse> employeeDetail =
            employeeDetailService.findMainDetail(employeeID);
    return ResponseEntity.ok(employeeDetail);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("tax_and_insurance")
  public ResponseEntity<?> findTaxAndInsurance(@RequestParam String employeeID) {
    List<TaxAndInsuranceResponse> taxAndInsurance =
            employeeDetailService.findTaxAndInsurance(employeeID);
    return ResponseEntity.ok(taxAndInsurance);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("add_info")
  public ResponseEntity<?> findAdditionalInfo(@RequestParam String employeeID) {
    List<EmployeeAdditionalInfo> additionalInfo =
        employeeDetailService.findAdditionalInfo(employeeID);
    return ResponseEntity.ok(additionalInfo);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("bank")
  public ResponseEntity<?> findBankInfo(@RequestParam String employeeID) {
    List<BankResponse> bank = employeeDetailService.findBankByEmployeeID(employeeID);
    return ResponseEntity.ok(bank);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("edu")
  public ResponseEntity<?> findEducationInfo(@RequestParam String employeeID) {
    List<EducationResponse> educations =
        employeeDetailService.findEducationByEmployeeID(employeeID);
    return ResponseEntity.ok(educations);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("working_history")
  public ResponseEntity<?> findWorkingHistoryByEmployeeID(@RequestParam String employeeID) {
    List<WorkingHistoryResponse> workingHistories =
        employeeDetailService.findWorkingHistoryByEmployeeID(employeeID);
    return ResponseEntity.ok(workingHistories);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("relative")
  public ResponseEntity<?> findRelativeByEmployeeID(@RequestParam String employeeID) {
    List<RelativeInformationResponse> relatives =
        employeeDetailService.findRelativeByEmployeeID(employeeID);
    return ResponseEntity.ok(relatives);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PutMapping("main/update")
  public ResponseEntity<?> updateEmployeeDetail(
      @RequestBody EmployeeDetailRequest employeeDetailRequest) {
    employeeDetailService.updateEmployeeDetail(employeeDetailRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PutMapping("tax_and_insurance/update")
  public ResponseEntity<?> updateTaxAndInsurance(
      @RequestBody TaxAndInsuranceRequest taxAndInsurance) {
    employeeDetailService.updateTaxAndInsurance(taxAndInsurance);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PutMapping("bank/update")
  public ResponseEntity<?> updateBankInfo(@RequestBody BankRequest bank) {
    employeeDetailService.updateBankInfo(bank);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PutMapping("working_history/update")
  public ResponseEntity<?> updateWorkingHistory(@RequestBody WorkingHistoryRequest workingHistory) {
    employeeDetailService.updateWorkingHistory(workingHistory);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PutMapping("relative/update")
  public ResponseEntity<?> updateRelativeInfo(
      @RequestBody RelativeInformationRequest relativeInformation) {
    employeeDetailService.updateRelativeInfo(relativeInformation);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PutMapping("education/update")
  public ResponseEntity<?> updateEducationInfo(@RequestBody EducationRequest education) {
    employeeDetailService.updateEducationInfo(education);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }
}