package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.HumanManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.REQUEST_SUCCESS;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class HrmController {

    @Autowired
    HumanManagementService humanManagementService;

    @GetMapping(URI_GET_ALL_EMPLOYEE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEmployee(@RequestParam Map<String, String> allRequestParams) {
        Context context = new Context();
        QueryParam queryParam = context.queryParam(allRequestParams);
        HrmResponseList hrmResponseList = humanManagementService.getListHumanResource(queryParam);
        return ResponseEntity.ok(hrmResponseList);
    }

    @PostMapping(URI_INSERT_EMPLOYEE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addEmployeeByForm(@RequestBody HrmRequest hrmRequest) {
        humanManagementService.insertEmployee(hrmRequest);
        return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
    }
}
