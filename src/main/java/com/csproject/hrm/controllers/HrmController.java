package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.services.HumanManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.csproject.hrm.common.constant.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class HrmController {

    @Autowired
    HumanManagementService humanManagementService;

    @PostMapping(URI_GET_ALL_EMPLOYEE)
    public ResponseEntity<?> getAllEmployee(@RequestParam String limit, @RequestParam String page) {
        List<HrmResponse> hrmResponses = humanManagementService.getListHumanResource(limit, page);
        return ResponseEntity.ok(hrmResponses);
    }
}
