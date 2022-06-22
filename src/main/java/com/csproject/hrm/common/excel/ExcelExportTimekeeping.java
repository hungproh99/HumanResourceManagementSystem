package com.csproject.hrm.common.excel;

import com.csproject.hrm.dto.response.TimekeepingResponse;
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
import java.util.List;

public class ExcelExportTimekeeping {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private List<TimekeepingResponse> timekeepingResponseList;

  public ExcelExportTimekeeping(List<TimekeepingResponse> timekeepingResponseList) {
    this.timekeepingResponseList = timekeepingResponseList;
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

    for (TimekeepingResponse timekeepingResponse : timekeepingResponseList) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;

      createCell(row, columnCount++, timekeepingResponse.getFull_name(), style);
      createCell(row, columnCount++, timekeepingResponse.getPosition(), style);
      createCell(row, columnCount++, timekeepingResponse.getGrade(), style);
      createCell(row, columnCount++, timekeepingResponse.getCurrent_date(), style);
      createCell(row, columnCount++, timekeepingResponse.getTimekeeping_status(), style);
      createCell(row, columnCount++, timekeepingResponse.getFirst_check_in(), style);
      createCell(row, columnCount++, timekeepingResponse.getLast_check_out(), style);
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
