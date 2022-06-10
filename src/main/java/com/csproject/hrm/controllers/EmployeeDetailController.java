package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.*;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

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
    List<Bank> bank = employeeDetailRepository.findBankByEmployeeID(employeeID);
    return ResponseEntity.ok(bank);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("edu")
  public ResponseEntity<?> findEducationInfo(@RequestParam String employeeID) {
    List<Education> educations = employeeDetailRepository.findEducationByEmployeeID(employeeID);
    return ResponseEntity.ok(educations);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("working_history")
  public ResponseEntity<?> findWorkingHistoryByEmployeeID(@RequestParam String employeeID) {
    List<WorkingHistory> workingHistories =
        employeeDetailRepository.findWorkingHistoryByEmployeeID(employeeID);
    return ResponseEntity.ok(workingHistories);
  }

  @PreAuthorize(value = "hasRole('ADMIN')")
  @GetMapping("relative")
  public ResponseEntity<?> findRelativeByEmployeeID(@RequestParam String employeeID) {
    List<RelativeInformation> relatives =
        employeeDetailRepository.findRelativeByEmployeeID(employeeID);
    return ResponseEntity.ok(relatives);
  }
}