package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
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

  @Autowired EmployeeDetailRepository employeeDetailRepository;

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("main")
  public ResponseEntity<?> findMainDetail(@RequestParam String employeeID) {
    List<EmployeeDetailResponse> employeeDetail =
        employeeDetailRepository.findMainDetail(employeeID);
    return ResponseEntity.ok(employeeDetail);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("tax_and_insurance")
  public ResponseEntity<?> findTaxAndInsurance(@RequestParam String employeeID) {
    List<TaxAndInsuranceResponse> taxAndInsurance =
        employeeDetailRepository.findTaxAndInsurance(employeeID);
    return ResponseEntity.ok(taxAndInsurance);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("add_info")
  public ResponseEntity<?> findAdditionalInfo(@RequestParam String employeeID) {
    List<EmployeeAdditionalInfo> additionalInfo =
        employeeDetailRepository.findAdditionalInfo(employeeID);
    return ResponseEntity.ok(additionalInfo);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("bank")
  public ResponseEntity<?> findBankInfo(@RequestParam String employeeID) {
    List<BankResponse> bank = employeeDetailRepository.findBankByEmployeeID(employeeID);
    return ResponseEntity.ok(bank);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("edu")
  public ResponseEntity<?> findEducationInfo(@RequestParam String employeeID) {
    List<EducationResponse> educations =
        employeeDetailRepository.findEducationByEmployeeID(employeeID);
    return ResponseEntity.ok(educations);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("working_history")
  public ResponseEntity<?> findWorkingHistoryByEmployeeID(@RequestParam String employeeID) {
    List<WorkingHistoryResponse> workingHistories =
        employeeDetailRepository.findWorkingHistoryByEmployeeID(employeeID);
    return ResponseEntity.ok(workingHistories);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("relative")
  public ResponseEntity<?> findRelativeByEmployeeID(@RequestParam String employeeID) {
    List<RelativeInformationResponse> relatives =
        employeeDetailRepository.findRelativeByEmployeeID(employeeID);
    return ResponseEntity.ok(relatives);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PostMapping("main/update")
  public ResponseEntity<?> updateEmployeeDetail(
      @RequestBody EmployeeDetailRequest employeeDetailRequest) {
    employeeDetailRepository.updateEmployeeDetail(employeeDetailRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PostMapping("tax_and_insurance/update")
  public ResponseEntity<?> updateTaxAndInsurance(
      @RequestBody TaxAndInsuranceRequest taxAndInsurance) {
    employeeDetailRepository.updateTaxAndInsurance(taxAndInsurance);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PostMapping("bank/update")
  public ResponseEntity<?> updateBankInfo(@RequestBody BankRequest bank) {
    employeeDetailRepository.updateBankInfo(bank);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PostMapping("working_history/update")
  public ResponseEntity<?> updateWorkingHistory(@RequestBody WorkingHistoryRequest workingHistory) {
    employeeDetailRepository.updateWorkingHistory(workingHistory);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PostMapping("relative/update")
  public ResponseEntity<?> updateRelativeInfo(
      @RequestBody RelativeInformationRequest relativeInformation) {
    employeeDetailRepository.updateRelativeInfo(relativeInformation);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @PostMapping("education/update")
  public ResponseEntity<?> updateEducationInfo(@RequestBody EducationRequest education) {
    employeeDetailRepository.updateEducationInfo(education);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.ACCEPTED, REQUEST_SUCCESS));
  }
}