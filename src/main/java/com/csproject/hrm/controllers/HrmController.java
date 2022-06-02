package com.csproject.hrm.controllers;

import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.HumanManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;
import static com.csproject.hrm.common.uri.Uri.URI_GET_ALL_EMPLOYEE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class HrmController {
	
	@Autowired
	HumanManagementService humanManagementService;
	
	@PostMapping(URI_GET_ALL_EMPLOYEE)
	public ResponseEntity<?> getAllEmployee(@RequestParam Map<String, String> allRequestParams) {
		//        List<HrmResponse> hrmResponses = humanManagementService.getListHumanResource(limit, page);
		Context context = new Context();
		QueryParam queryParam = context.queryParam(allRequestParams);
		return ResponseEntity.ok(queryParam.filters + "\n" + queryParam.pagination + "\n" + queryParam.orderByList);
		
	}
}