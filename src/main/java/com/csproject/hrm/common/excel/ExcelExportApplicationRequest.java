package com.csproject.hrm.common.excel;

import com.csproject.hrm.dto.response.ApplicationsRequestResponse;
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
import java.time.LocalDateTime;
import java.util.List;

public class ExcelExportApplicationRequest {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private List<ApplicationsRequestResponse> applicationsRequestResponseList;

  public ExcelExportApplicationRequest(
      List<ApplicationsRequestResponse> applicationsRequestResponseList) {
    this.applicationsRequestResponseList = applicationsRequestResponseList;
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
    createCell(row, 2, "Create Date", style);
    createCell(row, 3, "Request Type", style);
    createCell(row, 4, "Request Name", style);
    createCell(row, 5, "Description", style);
    createCell(row, 6, "Request Status", style);
    createCell(row, 7, "Latest Change", style);
    createCell(row, 8, "Duration", style);
    createCell(row, 9, "Approver", style);
    createCell(row, 10, "Checked By", style);
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
    } else if (value instanceof LocalDateTime) {
      cell.setCellValue((LocalDateTime) value);
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

    for (ApplicationsRequestResponse applicationsRequestResponse :
        applicationsRequestResponseList) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;
      String checkBy = null;
      createCell(row, columnCount++, applicationsRequestResponse.getEmployee_id(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getFull_name(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getCreate_date(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getRequest_type(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getRequest_name(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getDescription(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getRequest_status(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getChange_status_time(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getDuration(), style);
      createCell(row, columnCount++, applicationsRequestResponse.getApprover(), style);
      for (int i = 0; i < applicationsRequestResponse.getChecked_by().size(); i++) {
        if (i == applicationsRequestResponse.getChecked_by().size() - 1) {
          checkBy += applicationsRequestResponse.getChecked_by().get(i);
        }
        checkBy += applicationsRequestResponse.getChecked_by().get(i) + ", ";
      }
      createCell(row, columnCount++, checkBy, style);
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
