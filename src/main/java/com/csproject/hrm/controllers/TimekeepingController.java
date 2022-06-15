package com.csproject.hrm.controllers;

import com.csproject.hrm.dto.response.TimekeepingDetailResponse;
import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.exception.errors.ErrorResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.TimekeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.REQUEST_SUCCESS;
import static com.csproject.hrm.common.uri.Uri.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(REQUEST_MAPPING)
public class TimekeepingController {
  @Autowired TimekeepingService timekeepingService;

  @GetMapping(URI_GET_LIST_TIMEKEEPING)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListAllTimekeeping(
      @RequestParam Map<String, String> allRequestParams) {
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams);
    List<TimekeepingResponse> timekeeping = timekeepingService.getListAllTimekeeping(queryParam);
    return ResponseEntity.ok(timekeeping);
  }

  @GetMapping(value = URI_DOWNLOAD_CSV_TIMEKEEPING)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> downloadCsvTimekeeping(
      HttpServletResponse servletResponse, @RequestBody List<String> listId) throws IOException {
    servletResponse.setContentType("text/csv; charset=UTF-8");
    servletResponse.addHeader("Content-Disposition", "attachment; filename=\"timekeeping.csv\"");
    timekeepingService.exportTimekeepingToCsv(servletResponse.getWriter(), listId);
    return ResponseEntity.ok(new ErrorResponse(HttpStatus.CREATED, REQUEST_SUCCESS));
  }
  
  @GetMapping(URI_GET_DETAIL_TIMEKEEPING)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getListDetailTimekeeping(
          @RequestParam String employeeID, String date) {
    List<TimekeepingDetailResponse> timekeeping = timekeepingService.getTimekeepingByEmployeeIDAndDate(employeeID, date);
    return ResponseEntity.ok(timekeeping);
  }
}