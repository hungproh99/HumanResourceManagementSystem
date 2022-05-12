package com.csproject.hrm.controllers;

import com.csproject.hrm.entities.Demo;
import com.csproject.hrm.services.impl.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "demo")
public class DemoController {
	
	private final DemoService demoService;
	
	@Autowired
	public DemoController(DemoService demoService) {this.demoService = demoService;}
	
	@GetMapping(path = "/list", params = "id")
	public Demo getDemoById(@RequestParam(name = "id") Long id) {
		return demoService.getDemoByID(id);
	}
	
}