package com.csproject.hrm.controllers;

import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.csproject.hrm.common.constant.Constants.AUTHORIZATION;
import static com.csproject.hrm.common.constant.Constants.BEARER;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class test {
  @Autowired EmployeeDetailRepository employeeDetailRepository;
  @Autowired PolicyRepository policyRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired WorkingContractRepository workingContractRepository;
  @Autowired ChartService chartService;
  @Autowired JwtUtils jwtUtils;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("test")
  public ResponseEntity<?> test(HttpServletRequest request) {
    String areaName = "";
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      areaName =
          Objects.requireNonNullElse(chartService.getAreaNameByEmployeeID(employeeId), areaName);
    }

    return ResponseEntity.ok(chartService.getOrganizationalByAreaName(areaName));
  }
}