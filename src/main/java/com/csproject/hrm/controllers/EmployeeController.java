package com.csproject.hrm.controllers;

import com.csproject.hrm.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.csproject.hrm.common.constant.Uri.REQUEST_MAPPING;

@RestController
@RequestMapping(REQUEST_MAPPING)
public class EmployeeController {
	
	EmployeeRepository employeeRepository;
	
	@GetMapping("/list")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(employeeRepository.getListEmployee(0, 1000));
	}
}