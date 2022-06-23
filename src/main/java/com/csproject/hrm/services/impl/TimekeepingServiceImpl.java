package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.ETimekeepingStatus;
import com.csproject.hrm.common.excel.ExcelExportTimekeeping;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.repositories.TimekeepingRepository;
import com.csproject.hrm.services.TimekeepingService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class TimekeepingServiceImpl implements TimekeepingService {
  @Autowired TimekeepingRepository timekeepingRepository;

  @Autowired EmployeeDetailRepository employeeDetailRepository;

  @Override
  public TimekeepingResponsesList getListAllTimekeeping(QueryParam queryParam) {
    List<TimekeepingResponses> timekeepingResponsesList =
        timekeepingRepository.getListAllTimekeeping(queryParam);
    int total = timekeepingRepository.countListAllTimekeeping(queryParam);

    return TimekeepingResponsesList.builder()
        .timekeepingResponsesList(timekeepingResponsesList)
        .total(total)
        .build();
  }

  @Override
  public void exportTimekeepingToCsv(Writer writer, QueryParam queryParam, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<TimekeepingResponses> timekeepingResponses =
          timekeepingRepository.getListTimekeepingToExport(queryParam, list);
      try (CSVPrinter csvPrinter =
          new CSVPrinter(
              writer,
              CSVFormat.DEFAULT.withHeader(
                  "Full Name",
                  "Position",
                  "Grade",
                  "Current Date",
                  "Timekeeping Status",
                  "First Check In",
                  "Last Check Out"))) {

        for (TimekeepingResponses timekeepingResponseList : timekeepingResponses) {
          for (TimekeepingResponse timekeepingResponse :
              timekeepingResponseList.getTimekeepingResponses()) {
            csvPrinter.printRecord(
                timekeepingResponseList.getFull_name(),
                timekeepingResponseList.getPosition(),
                timekeepingResponseList.getGrade(),
                timekeepingResponse.getCurrent_date(),
                timekeepingResponse.getTimekeeping_status(),
                timekeepingResponse.getFirst_check_in(),
                timekeepingResponse.getLast_check_out());
          }
        }
        csvPrinter.flush();
      } catch (IOException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, CAN_NOT_WRITE_CSV);
      }
    }
  }

  @Override
  public void exportTimekeepingToExcel(
      HttpServletResponse response, QueryParam queryParam, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      try {
        List<TimekeepingResponses> timekeepingResponses =
            timekeepingRepository.getListTimekeepingToExport(queryParam, list);
        ExcelExportTimekeeping excelExportTimekeeping =
            new ExcelExportTimekeeping(timekeepingResponses);
        excelExportTimekeeping.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public Optional<TimekeepingDetailResponse> getTimekeepingByEmployeeIDAndDate(
      String employeeID, String date) {
    if (employeeID == null || date == null) {
      throw new NullPointerException("Had param is null!");
    }
    LocalDate localDate;
    try {
      localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException e) {
      throw new CustomErrorException("Date must have format yyyy-MM-dd!");
    }
    if (employeeDetailRepository.checkEmployeeIDIsExists(employeeID)) {
      Optional<TimekeepingDetailResponse> list =
          timekeepingRepository.getTimekeepingByEmployeeIDAndDate(employeeID, localDate);
      Long timekeepingID = list.get().getTimekeeping_id();
      list.get()
          .setCheck_in_check_outs(
              timekeepingRepository.getCheckInCheckOutByTimekeepingID(timekeepingID));
      list.get()
          .setTimekeeping_status(ETimekeepingStatus.getValue(list.get().getTimekeeping_status()));
      return list;
    } else {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeID);
    }
  }
}