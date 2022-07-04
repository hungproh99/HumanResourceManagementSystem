package com.csproject.hrm.controllers;

import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class PolicyController {

  @Autowired PolicyService policyService;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_list_working_policy")
  public ResponseEntity<?> getListWorkingPolicy(
      @RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    return ResponseEntity.ok(policyService.getListWorkingPolicy(queryParam));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_list_a_and_b_policy")
  public ResponseEntity<?> getListCAndBPolicy(@RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    return ResponseEntity.ok(policyService.getListCAndBPolicy(queryParam));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_list_insurance_policy")
  public ResponseEntity<?> getListInsurancePolicy(
      @RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    return ResponseEntity.ok(policyService.getListInsurancePolicy(queryParam));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_list_leave_policy")
  public ResponseEntity<?> getListLeavePolicy(@RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    return ResponseEntity.ok(policyService.getListLeavePolicy(queryParam));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_list_tax_policy")
  public ResponseEntity<?> getListTaxPolicy(@RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    return ResponseEntity.ok(policyService.getListTaxPolicy(queryParam));
  }
}