package com.csproject.hrm.common;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "employee")
public class EmployeeController {
	
	@GetMapping(path = {"list"})
	public List<String> getAll() {
		return new ArrayList<>(Arrays.asList("h", "e", "l", "l", "o"));
	}
}