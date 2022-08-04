package com.csproject.hrm.controllers;

import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;

import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
@Validated
public class PolicyController {

  @Autowired PolicyService policyService;

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_list_policy")
  public ResponseEntity<?> getListPolicyByCategoryID(
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    return ResponseEntity.ok(policyService.getListPolicyByCategoryID(queryParam));
  }

  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
  @GetMapping("get_list_category")
  public ResponseEntity<?> getAllPolicyCategory() {
    return ResponseEntity.ok(policyService.getAllPolicyCategory());
  }
}