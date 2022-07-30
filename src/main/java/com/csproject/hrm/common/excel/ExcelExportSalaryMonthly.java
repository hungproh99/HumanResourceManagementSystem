package com.csproject.hrm.common.excel;

import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class ExcelExportSalaryMonthly {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private List<SalaryMonthlyResponse> salaryMonthlyResponses;

  public ExcelExportSalaryMonthly(List<SalaryMonthlyResponse> salaryMonthlyResponses) {
    this.salaryMonthlyResponses = salaryMonthlyResponses;
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
    createCell(row, 2, "Position", style);
    createCell(row, 3, "Standard Point", style);
    createCell(row, 4, "Actual Point", style);
    createCell(row, 5, "Overtime Point", style);
    createCell(row, 6, "Total Deduction", style);
    createCell(row, 7, "Total Bonus", style);
    createCell(row, 8, "Total Insurance", style);
    createCell(row, 9, "Total Tax", style);
    createCell(row, 10, "Total Advance", style);
    createCell(row, 11, "Final Salary", style);
    createCell(row, 12, "Start Date", style);
    createCell(row, 13, "End Date", style);
    createCell(row, 14, "Salary Status", style);
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
    } else if (value instanceof BigDecimal) {
      cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
    } else if (value instanceof LocalDate) {
      cell.setCellValue((LocalDate) value);
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

    for (SalaryMonthlyResponse salaryMonthlyResponse : salaryMonthlyResponses) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;

      createCell(row, columnCount++, salaryMonthlyResponse.getEmployeeId(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getFullName(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getPosition(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getStandardPoint(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getActualPoint(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getOtPoint(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getTotalDeduction(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getTotalBonus(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getTotalInsurance(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getTotalTax(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getTotalAdvance(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getFinalSalary(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getStartDate(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getEndDate(), style);
      createCell(row, columnCount++, salaryMonthlyResponse.getSalaryStatus(), style);
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
