package com.csproject.hrm.common.excel;

import com.csproject.hrm.dto.response.TimekeepingResponse;
import com.csproject.hrm.dto.response.TimekeepingResponses;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExportTimekeeping {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private List<TimekeepingResponses> timekeepingResponses;

  public ExcelExportTimekeeping(List<TimekeepingResponses> timekeepingResponses) {
    this.timekeepingResponses = timekeepingResponses;
    workbook = new XSSFWorkbook();
  }

  private void writeHeaderLine() {
    sheet = workbook.createSheet("Timekeeping");

    Row row = sheet.createRow(0);

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);

    createCell(row, 0, "Full Name", style);
    createCell(row, 1, "Position", style);
    createCell(row, 2, "Grade", style);
    createCell(row, 3, "Current Date", style);
    createCell(row, 4, "Timekeeping Status", style);
    createCell(row, 5, "First Check In", style);
    createCell(row, 6, "Last Check Out", style);
  }

  private void createCell(Row row, int columnCount, Object value, CellStyle style) {
    sheet.autoSizeColumn(columnCount);
    Cell cell = row.createCell(columnCount);
    if (value instanceof Integer) {
      cell.setCellValue((Integer) value);
    } else if (value instanceof Boolean) {
      cell.setCellValue((Boolean) value);
    } else if (value instanceof Date) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      cell.setCellValue(simpleDateFormat.format(value));
    } else if (value instanceof Double) {
      cell.setCellValue((Double) value);
    } else if (value instanceof LocalDate) {
      cell.setCellValue((LocalDate) value);
    } else if (value instanceof LocalTime) {
      cell.setCellValue(((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    } else {
      cell.setCellValue((String) value);
    }
    cell.setCellStyle(style);
  }

  private void writeDataLines() {
    int rowCount = 1;

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontHeight(14);
    style.setFont(font);

    for (TimekeepingResponses timekeepingResponses : timekeepingResponses) {
      if (timekeepingResponses.getTimekeepingResponses().size() == 0) {
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        createCell(row, columnCount++, timekeepingResponses.getFull_name(), style);
        createCell(row, columnCount++, timekeepingResponses.getPosition(), style);
        createCell(row, columnCount++, timekeepingResponses.getGrade(), style);
      }
      for (TimekeepingResponse timekeepingResponse :
          timekeepingResponses.getTimekeepingResponses()) {
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        createCell(row, columnCount++, timekeepingResponses.getFull_name(), style);
        createCell(row, columnCount++, timekeepingResponses.getPosition(), style);
        createCell(row, columnCount++, timekeepingResponses.getGrade(), style);
        createCell(row, columnCount++, timekeepingResponse.getCurrent_date(), style);
        String timekeepingStatus = null;
        for (int i = 0; i < timekeepingResponse.getTimekeeping_status().size(); i++) {
          if (i == timekeepingResponse.getTimekeeping_status().size() - 1) {
            timekeepingStatus += timekeepingResponse.getTimekeeping_status().get(i);
          } else {
            timekeepingStatus += timekeepingResponse.getTimekeeping_status().get(i) + ", ";
          }
        }
        createCell(row, columnCount++, timekeepingStatus, style);
        createCell(row, columnCount++, timekeepingResponse.getFirst_check_in().toString(), style);
        createCell(row, columnCount++, timekeepingResponse.getLast_check_out().toString(), style);
      }
    }
  }

  public void export(HttpServletResponse response) throws IOException {
    writeHeaderLine();
    writeDataLines();

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();

    outputStream.close();
  }
}
