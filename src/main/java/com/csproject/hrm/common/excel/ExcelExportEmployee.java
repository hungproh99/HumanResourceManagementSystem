package com.csproject.hrm.common.excel;

import com.csproject.hrm.dto.response.HrmResponse;
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

public class ExcelExportEmployee {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private List<HrmResponse> hrmResponseList;

  public ExcelExportEmployee(List<HrmResponse> hrmResponseList) {
    this.hrmResponseList = hrmResponseList;
    workbook = new XSSFWorkbook();
  }

  private void writeHeaderLine() {
    sheet = workbook.createSheet("Employee");

    Row row = sheet.createRow(0);

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);

    createCell(row, 0, "Employee Id", style);
    createCell(row, 1, "Full Name", style);
    createCell(row, 2, "Company Email", style);
    createCell(row, 3, "Working Status", style);
    createCell(row, 4, "Phone", style);
    createCell(row, 5, "Gender", style);
    createCell(row, 6, "Birth Date", style);
    createCell(row, 7, "Grade", style);
    createCell(row, 8, "Office", style);
    createCell(row, 9, "Area", style);
    createCell(row, 10, "Position", style);
    createCell(row, 11, "Seniority", style);
    createCell(row, 12, "Working Name", style);
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

    for (HrmResponse hrmResponse : hrmResponseList) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;

      createCell(row, columnCount++, hrmResponse.getEmployee_id(), style);
      createCell(row, columnCount++, hrmResponse.getFull_name(), style);
      createCell(row, columnCount++, hrmResponse.getEmail(), style);
      createCell(row, columnCount++, hrmResponse.getWorking_status(), style);
      createCell(row, columnCount++, hrmResponse.getPhone(), style);
      createCell(row, columnCount++, hrmResponse.getGender(), style);
      createCell(row, columnCount++, hrmResponse.getBirth_date(), style);
      createCell(row, columnCount++, hrmResponse.getGrade(), style);
      createCell(row, columnCount++, hrmResponse.getOffice_name(), style);
      createCell(row, columnCount++, hrmResponse.getArea_name(), style);
      createCell(row, columnCount++, hrmResponse.getSeniority(), style);
      createCell(row, columnCount++, hrmResponse.getPosition_name(), style);
      createCell(row, columnCount++, hrmResponse.getWorking_name(), style);
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
