package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.EmployeeNameAndID;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.HumanManagementService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
@Validated
public class HrmController {

  @Autowired HumanManagementService humanManagementService;
  @Autowired JwtUtils jwtUtils;

  @GetMapping(URI_GET_ALL_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getAllEmployee(
      HttpServletRequest request,
      @RequestParam
          Map<String, @NotBlank(message = "Value must not be blank!") String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    HrmResponseList hrmResponseList;
      String headerAuth = request.getHeader(AUTHORIZATION);
      if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
        String jwt = headerAuth.substring(7);
        String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      if (request.isUserInRole("MANAGER")) {
        hrmResponseList =
            humanManagementService.getListHumanResourceOfManager(queryParam, employeeId);
        return ResponseEntity.ok(hrmResponseList);
      } else if (request.isUserInRole("ADMIN")) {
        hrmResponseList = humanManagementService.getListHumanResource(queryParam, employeeId);
        return ResponseEntity.ok(hrmResponseList);
      }
    }
    throw new CustomErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized");
  }

  @PostMapping(URI_INSERT_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> addEmployee(@Valid @RequestBody HrmRequest hrmRequest) {
    humanManagementService.insertEmployee(hrmRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(URI_INSERT_MULTI_EMPLOYEE_BY_CSV)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> importCsvToEmployee(
      @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    try {
      String extension = multipartFile.getContentType();
      if (!extension.equals("text/csv")) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ONLY_UPLOAD_CSV);
      }
      InputStream fileName = multipartFile.getInputStream();
      humanManagementService.importCsvToEmployee(fileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(URI_INSERT_MULTI_EMPLOYEE_BY_EXCEL)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> importExcelToEmployee(
      @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
    if (multipartFile == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    try {
      String extension = multipartFile.getContentType();
      InputStream inputStream = multipartFile.getInputStream();
      Workbook workbook = null;
      if (!extension.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
          && !extension.equals("application/vnd.ms-excel")) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ONLY_UPLOAD_EXCEL);
      } else if (extension.equals("application/vnd.ms-excel")) {
        workbook = WorkbookFactory.create(inputStream);
      } else if (extension.equals(
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
        workbook = WorkbookFactory.create(inputStream);
      }
      humanManagementService.importExcelToEmployee(workbook);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @GetMapping(URI_LIST_WORKING_TYPE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListWorkingType() {
    return ResponseEntity.ok(humanManagementService.getListWorkingType());
  }

  @GetMapping(URI_LIST_EMPLOYEE_TYPE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListEmployeeType() {
    return ResponseEntity.ok(humanManagementService.getListEmployeeType());
  }

  @GetMapping(URI_LIST_AREA)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListArea() {
    return ResponseEntity.ok(humanManagementService.getListArea());
  }

  @GetMapping(URI_LIST_JOB)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListJob() {
    return ResponseEntity.ok(humanManagementService.getListPosition());
  }

  @GetMapping(URI_LIST_GRADE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListGrade(@PathVariable Long job_id) {
    return ResponseEntity.ok(humanManagementService.getListGradeByPosition(job_id));
  }

  @GetMapping(URI_LIST_OFFICE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListOffice() {
    return ResponseEntity.ok(humanManagementService.getListOffice());
  }

  @GetMapping(URI_LIST_ROLE_TYPE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListRoleType() {
    return ResponseEntity.ok(humanManagementService.getListRoleType());
  }

  //  @GetMapping(URI_GET_LIST_MANAGER)
  //  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  //  public ResponseEntity<?> getListManager(@RequestParam String name) {
  //    return ResponseEntity.ok(humanManagementService.getListManagerByName(name));
  //  }

  //  @GetMapping(URI_GET_LIST_EMPLOYEE_NAME_AND_ID)
  //  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  //  public ResponseEntity<?> getListEmployee(@RequestParam String name) {
  //    return ResponseEntity.ok(humanManagementService.getListEmployeeByManagement(name));
  //  }

  @PostMapping(value = URI_DOWNLOAD_CSV_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> downloadCsvEmployee(
      HttpServletResponse servletResponse, @RequestBody List<String> listId) throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("text/csv; charset=UTF-8");
    servletResponse.addHeader(
        "Content-Disposition",
        "attachment; filename=\"Employees_" + timestamp.getTime() + ".csv\"");
    humanManagementService.exportEmployeeToCsv(servletResponse.getWriter(), listId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> downloadExcelEmployee(
      HttpServletResponse servletResponse, @RequestBody List<String> listId) throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("application/octet-stream");
    servletResponse.addHeader(
        "Content-Disposition", "attachment; filename=Employees_" + timestamp.getTime() + ".xlsx");
    humanManagementService.exportEmployeeToExcel(servletResponse, listId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @GetMapping(value = URI_GET_LIST_MANAGER_HIGHER_OF_AREA)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListManagerHigherOfArea(HttpServletRequest request) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      List<EmployeeNameAndID> listManagerOfArea =
          humanManagementService.getListManagerHigherOfArea(employeeId);
      return ResponseEntity.ok(listManagerOfArea);
    }
    throw new CustomErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized");
  }

  @GetMapping(value = URI_GET_LIST_MANAGER_LOWER_OF_AREA)
  @PreAuthorize(value = "hasRole('ADMIN') or hasRole('MANAGER')")
  public ResponseEntity<?> getListManagerLowerOfArea(HttpServletRequest request) {
    String headerAuth = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
      String jwt = headerAuth.substring(7);
      String employeeId = jwtUtils.getIdFromJwtToken(jwt);
      List<EmployeeNameAndID> listManagerOfArea =
          humanManagementService.getListManagerLowerOfArea(employeeId);
      return ResponseEntity.ok(listManagerOfArea);
    }
    throw new CustomErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized");
  }
}
