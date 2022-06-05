package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.Bank;
import com.csproject.hrm.entities.Education;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@RestController
@RequestMapping(REQUEST_MAPPING + "/employee/detail")
public class EmployeeDetailController {
	
	@Autowired
	EmployeeDetailRepository employeeDetailRepository;
	
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@GetMapping("main")
	public ResponseEntity<?> findMainDetail(@RequestParam Map<String, String> requestParams) {
		Context context = new Context();
		QueryParam queryParam = context.queryParam(requestParams);
		List<EmployeeDetailResponse> employeeDetail = employeeDetailRepository.findMainDetail(queryParam);
		return ResponseEntity.ok(employeeDetail);
	}
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@GetMapping("tax_and_insurance")
	public ResponseEntity<?> findTaxAndInsurance(@RequestParam Map<String, String> requestParams) {
		Context context = new Context();
		QueryParam queryParam = context.queryParam(requestParams);
		List<TaxAndInsuranceResponse> taxAndInsurance = employeeDetailRepository.findTaxAndInsurance(queryParam);
		return ResponseEntity.ok(taxAndInsurance);
	}
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@GetMapping("add_info")
	public ResponseEntity<?> findAdditionalInfo(@RequestParam Map<String, String> requestParams) {
		Context context = new Context();
		QueryParam queryParam = context.queryParam(requestParams);
		List<EmployeeAdditionalInfo> additionalInfo = employeeDetailRepository.findAdditionalInfo(queryParam);
		return ResponseEntity.ok(additionalInfo);
	}
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@GetMapping("bank")
	public ResponseEntity<?> findBankInfo(@RequestParam Map<String, String> requestParams) {
		Context context = new Context();
		QueryParam queryParam = context.queryParam(requestParams);
		List<Bank> bank = employeeDetailRepository.findBankByEmployeeID(queryParam);
		return ResponseEntity.ok(bank);
	}
	
	@PreAuthorize(value = "hasRole('ADMIN')")
	@GetMapping("edu")
	public ResponseEntity<?> findEducationInfo(@RequestParam Map<String, String> requestParams) {
		Context context = new Context();
		QueryParam queryParam = context.queryParam(requestParams);
		List<Education> educations = employeeDetailRepository.findEducationByEmployeeID(queryParam);
		return ResponseEntity.ok(educations);
	}
}