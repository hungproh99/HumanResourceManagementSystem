package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.request.UpdateHrmRequest;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.HumanManagementService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class HrmController {

  @Autowired HumanManagementService humanManagementService;

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
  public ResponseEntity<?> addEmployee(@RequestBody HrmRequest hrmRequest) {
    humanManagementService.insertEmployee(hrmRequest);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(URI_INSERT_MULTI_EMPLOYEE_BY_CSV)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> importCsvToEmployee(@RequestParam MultipartFile multipartFile) {
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
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> importExcelToEmployee(@RequestParam MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    try {
      String extension = multipartFile.getContentType();
      InputStream inputStream = multipartFile.getInputStream();
      Workbook workbook = null;
      if (!extension.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
          || extension.equals("application/vnd.ms-excel")) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, ONLY_UPLOAD_EXCEL);
      } else if (extension.equals("application/vnd.ms-excel")) {
        workbook = new HSSFWorkbook(inputStream);
      } else if (extension.equals(
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
        workbook = new XSSFWorkbook(inputStream);
      }
      humanManagementService.importExcelToEmployee(workbook);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @GetMapping(URI_LIST_WORKING_TYPE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListWorkingType() {
    return ResponseEntity.ok(humanManagementService.getListWorkingType());
  }

  @GetMapping(URI_LIST_EMPLOYEE_TYPE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListEmployeeType() {
    return ResponseEntity.ok(humanManagementService.getListEmployeeType());
  }

  @GetMapping(URI_LIST_AREA)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListArea() {
    return ResponseEntity.ok(humanManagementService.getListArea());
  }

  @GetMapping(URI_LIST_JOB)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListJob() {
    return ResponseEntity.ok(humanManagementService.getListPosition());
  }

  @GetMapping(URI_LIST_GRADE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListGrade(@PathVariable Long job_id) {
    return ResponseEntity.ok(humanManagementService.getListGradeByPosition(job_id));
  }

  @GetMapping(URI_LIST_OFFICE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListOffice() {
    return ResponseEntity.ok(humanManagementService.getListOffice());
  }

  @GetMapping(URI_LIST_ROLE_TYPE)
  @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
  public ResponseEntity<?> getListRoleType(HttpServletRequest request) {
    boolean isAdmin = false;
    if (request.isUserInRole("ADMIN")) {
      isAdmin = true;
    }
    return ResponseEntity.ok(humanManagementService.getListRoleType(isAdmin));
  }

  @GetMapping(URI_GET_LIST_MANAGER)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListManager(@RequestParam String name) {
    return ResponseEntity.ok(humanManagementService.getListManagerByName(name));
  }

  @PutMapping(URI_UPDATE_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> updateEmployee(
      @RequestBody UpdateHrmRequest updateHrmRequest,
      @PathVariable("employee_id") String employeeId) {
    humanManagementService.updateEmployeeById(updateHrmRequest, employeeId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_CSV_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> downloadCsvEmployee(
      HttpServletResponse servletResponse, @RequestBody List<String> listId) throws IOException {
    servletResponse.setContentType("text/csv; charset=UTF-8");
    servletResponse.addHeader("Content-Disposition", "attachment; filename=\"employees.csv\"");
    humanManagementService.exportEmployeeToCsv(servletResponse.getWriter(), listId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }

  @PostMapping(value = URI_DOWNLOAD_EXCEL_EMPLOYEE)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> downloadExcelEmployee(
      HttpServletResponse servletResponse, @RequestBody List<String> listId) throws IOException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    servletResponse.setContentType("application/octet-stream");
    servletResponse.addHeader(
        "Content-Disposition", "attachment; filename=employees_" + timestamp.getTime() + ".xlsx");
    humanManagementService.exportEmployeeToExcel(servletResponse, listId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }
}
