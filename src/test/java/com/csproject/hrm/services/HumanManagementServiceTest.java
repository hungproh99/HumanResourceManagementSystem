package com.csproject.hrm.services;

import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.Pagination;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.impl.HumanManagementServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.csproject.hrm.common.sample.DataSample.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HumanManagementServiceTest {
  @Autowired @Mock EmployeeRepository employeeRepository;
  @Autowired @Mock EmployeeDetailRepository employeeDetailRepository;
  @Autowired @Mock WorkingPlaceRepository workingPlaceRepository;
  @Autowired @Mock GeneralFunction generalFunction;
  @Autowired @Mock PasswordEncoder passwordEncoder;
  @Autowired @Mock RoleRepository roleRepository;
  @Autowired @Mock AreaRepository areaRepository;
  @Autowired @Mock OfficeRepository officeRepository;
  @Autowired @Mock GradeRepository gradeRepository;
  @Autowired @Mock JobRepository jobRepository;
  @Autowired @Mock WorkingTypeRepository workingTypeRepository;
  @Autowired @Mock EmployeeTypeRepository employeeTypeRepository;
  @Autowired @Mock Writer writer;
  @Autowired @Mock InputStream inputStream;
  @Autowired @Mock HttpServletResponse httpServletResponse;
  @Autowired @Mock ServletOutputStream outputStream;
  @Autowired @Mock Workbook workbook;
  @Autowired @Mock Sheet sheet;
  @Autowired @Mock Row row;
  @Autowired @Mock Cell cell;
  @InjectMocks HumanManagementServiceImpl humanManagementService;

  @Test
  void test_getListHumanResource() {
    List<HrmResponse> hrmResponses = HRM_RESPONSES;
    HrmResponseList hrmResponseListExpected = HRM_RESPONSE_LIST;
    QueryParam queryParam =
        new QueryParam(Pagination.defaultPage(), new ArrayList<>(), new ArrayList<>());
    String employeeId = "huynq100";
    when(employeeRepository.findAllEmployee(queryParam, employeeId)).thenReturn(hrmResponses);
    when(employeeRepository.countAllEmployeeByCondition(queryParam)).thenReturn(1);

    HrmResponseList hrmResponseListActual =
        humanManagementService.getListHumanResource(queryParam, employeeId);

    assertEquals("", hrmResponseListExpected, hrmResponseListActual);
  }

  @Test
  void test_insertEmployee_AdminRole() {
    HrmRequest hrmRequest = HRM_REQUEST;
    String employeeId = "huynq100";
    String companyEmail = "huynq100@fpt.edu.vn";
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(true);
    when(workingTypeRepository.existsById(hrmRequest.getWorkingType())).thenReturn(true);
    when(employeeRepository.existsById(hrmRequest.getManagerId())).thenReturn(true);
    when(employeeTypeRepository.existsById(hrmRequest.getEmployeeType())).thenReturn(true);
    when(generalFunction.generateIdEmployee(hrmRequest.getFullName(), 0)).thenReturn(employeeId);
    when(generalFunction.generateEmailEmployee(employeeId)).thenReturn(companyEmail);

    humanManagementService.insertEmployee(hrmRequest);
  }

  @Test
  void test_insertEmployee_NotExistRole() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_WrongGender() {
    HrmRequest hrmRequest = HRM_REQUEST_WRONG_GENDER;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotExistArea() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotExistOffice() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotExistGrade() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotExistPosition() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotExistWorkingType() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(true);
    when(workingTypeRepository.existsById(hrmRequest.getWorkingType())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotExistManager() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(true);
    when(workingTypeRepository.existsById(hrmRequest.getWorkingType())).thenReturn(true);
    when(employeeRepository.existsById(hrmRequest.getManagerId())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotEmployeeType() {
    HrmRequest hrmRequest = HRM_REQUEST;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(true);
    when(workingTypeRepository.existsById(hrmRequest.getWorkingType())).thenReturn(true);
    when(employeeRepository.existsById(hrmRequest.getManagerId())).thenReturn(true);
    when(employeeTypeRepository.existsById(hrmRequest.getEmployeeType())).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_NotEnoughBirthDate() {
    HrmRequest hrmRequest = HRM_REQUEST_NOT_ENOUGH_BIRTH_DATE;
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(true);
    when(workingTypeRepository.existsById(hrmRequest.getWorkingType())).thenReturn(true);
    when(employeeRepository.existsById(hrmRequest.getManagerId())).thenReturn(true);
    when(employeeTypeRepository.existsById(hrmRequest.getEmployeeType())).thenReturn(true);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.insertEmployee(hrmRequest));
  }

  @Test
  void test_insertEmployee_MangerRole() {
    HrmRequest hrmRequest = HRM_REQUEST_MANAGER_ROLE;
    String employeeId = "huynq100";
    String companyEmail = "huynq100@fpt.edu.vn";
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(true);
    when(workingTypeRepository.existsById(hrmRequest.getWorkingType())).thenReturn(true);
    when(employeeRepository.existsById(hrmRequest.getManagerId())).thenReturn(true);
    when(employeeTypeRepository.existsById(hrmRequest.getEmployeeType())).thenReturn(true);
    when(generalFunction.generateIdEmployee(hrmRequest.getFullName(), 0)).thenReturn(employeeId);
    when(generalFunction.generateEmailEmployee(employeeId)).thenReturn(companyEmail);

    humanManagementService.insertEmployee(hrmRequest);
  }

  @Test
  void test_insertEmployee_UserRole() {
    HrmRequest hrmRequest = HRM_REQUEST_USER_ROLE;
    String employeeId = "huynq100";
    String companyEmail = "huynq100@fpt.edu.vn";
    when(roleRepository.existsById(hrmRequest.getRole())).thenReturn(true);
    when(areaRepository.existsById(hrmRequest.getArea())).thenReturn(true);
    when(officeRepository.existsById(hrmRequest.getOffice())).thenReturn(true);
    when(gradeRepository.existsById(hrmRequest.getGrade())).thenReturn(true);
    when(jobRepository.existsById(hrmRequest.getPosition())).thenReturn(true);
    when(workingTypeRepository.existsById(hrmRequest.getWorkingType())).thenReturn(true);
    when(employeeRepository.existsById(hrmRequest.getManagerId())).thenReturn(true);
    when(employeeTypeRepository.existsById(hrmRequest.getEmployeeType())).thenReturn(true);
    when(generalFunction.generateIdEmployee(hrmRequest.getFullName(), 0)).thenReturn(employeeId);
    when(generalFunction.generateEmailEmployee(employeeId)).thenReturn(companyEmail);

    humanManagementService.insertEmployee(hrmRequest);
  }

  @Test
  void test_getListWorkingType() {
    when(employeeRepository.getListWorkingType()).thenReturn(LIST_WORKING_TYPE);
    humanManagementService.getListWorkingType();
  }

  @Test
  void test_getListEmployeeType() {
    when(employeeRepository.getListEmployeeType()).thenReturn(LIST_EMPLOYEE_TYPE);
    humanManagementService.getListEmployeeType();
  }

  @Test
  void test_getListRoleType_IsAdmin() {
    boolean isAdmin = true;
    when(employeeRepository.getListRoleType(isAdmin)).thenReturn(LIST_ROLE_DTO);
    humanManagementService.getListRoleType(isAdmin);
  }

  @Test
  void test_getListOffice() {
    when(workingPlaceRepository.getListOffice()).thenReturn(LIST_OFFICE_DTO);
    humanManagementService.getListOffice();
  }

  @Test
  void test_getListArea() {
    when(workingPlaceRepository.getListArea()).thenReturn(LIST_AREA_DTO);
    humanManagementService.getListArea();
  }

  @Test
  void test_getListPosition() {
    when(workingPlaceRepository.getListPosition()).thenReturn(LIST_JOB_DTO);
    humanManagementService.getListPosition();
  }

  @Test
  void test_getListGradeByPosition() {
    Long id = 1L;
    when(workingPlaceRepository.getListGradeByPosition(id)).thenReturn(LIST_GRADE_DTO);
    when(workingPlaceRepository.checkExistJobId(id)).thenReturn(true);
    humanManagementService.getListGradeByPosition(id);
  }

  @Test
  void test_getListGradeByPosition_NullId() {
    Long id = null;
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.getListGradeByPosition(id));
  }

  @Test
  void test_getListGradeByPosition_NotExistId() {
    Long id = 10L;
    when(workingPlaceRepository.checkExistJobId(id)).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.getListGradeByPosition(id));
  }

  @Test
  void test_exportEmployeeToCsv() {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    list.add("lienpt1");
    for (String employeeId : list) {
      when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    }
    when(employeeRepository.findEmployeeByListId(QueryParam.defaultParam(), list))
        .thenReturn(HRM_RESPONSES);

    humanManagementService.exportEmployeeToCsv(writer, list);
  }

  @Test
  void test_exportEmployeeToCsv_ListNull() {
    List<String> list = new ArrayList<>();
    assertThrows(
        CustomDataNotFoundException.class,
        () -> humanManagementService.exportEmployeeToCsv(writer, list));
  }

  @Test
  void test_exportEmployeeToCsv_NotExistId() {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    list.add("lienpt1");
    when(employeeDetailRepository.checkEmployeeIDIsExists(list.get(0))).thenReturn(false);
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.exportEmployeeToCsv(writer, list));
  }

  //  @Test
  //  void test_importCsvToEmployee() throws IOException {
  //    List<HrmRequest> hrmRequestList = new ArrayList<>();
  //    hrmRequestList.add(HRM_REQUEST);
  //      when(roleRepository.existsById(1L)).thenReturn(true);
  //      when(areaRepository.existsById(1L)).thenReturn(true);
  //      when(officeRepository.existsById(1L)).thenReturn(true);
  //      when(gradeRepository.existsById(1L)).thenReturn(true);
  //      when(jobRepository.existsById(1L)).thenReturn(true);
  //      when(workingTypeRepository.existsById(1L)).thenReturn(true);
  //      when(employeeRepository.existsById("huynq1000")).thenReturn(true);
  //      when(employeeTypeRepository.existsById(1L)).thenReturn(true);
  //
  //    humanManagementService.importCsvToEmployee(inputStream);
  //  }

  @Test
  void test_exportEmployeeToExcel() throws IOException {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    list.add("lienpt1");
    for (String employeeId : list) {
      when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    }
    doReturn(HRM_RESPONSES)
        .when(employeeRepository)
        .findEmployeeByListId(QueryParam.defaultParam(), list);
    when(httpServletResponse.getOutputStream()).thenReturn(outputStream);

    humanManagementService.exportEmployeeToExcel(httpServletResponse, list);
  }

  @Test
  void test_exportEmployeeToExcel_ListNull() {
    List<String> list = new ArrayList<>();
    assertThrows(
        CustomDataNotFoundException.class,
        () -> humanManagementService.exportEmployeeToExcel(httpServletResponse, list));
  }

  @Test
  void test_exportEmployeeToExcel_NotExistId() {
    List<String> list = new ArrayList<>();
    list.add("huynq100");
    list.add("lienpt1");
    when(employeeDetailRepository.checkEmployeeIDIsExists(list.get(0))).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () -> humanManagementService.exportEmployeeToExcel(httpServletResponse, list));
  }

  @Test
  void test_importExcelToEmployee() {
    List<HrmRequest> hrmRequestList = new ArrayList<>();
    hrmRequestList.add(HRM_REQUEST);
    when(workbook.getSheetAt(0)).thenReturn(sheet);
    when(sheet.getLastRowNum()).thenReturn(11);
    for (int i = 0; i <= 1; i++) {
      when(sheet.getRow(i)).thenReturn(row);
      when(row.getCell(0)).thenReturn(cell);
    }
    Row row1 = mock(Row.class);
    Cell cell0 = mock(Cell.class);
    Cell cell1 = mock(Cell.class);
    Cell cell2 = mock(Cell.class);
    Cell cell3 = mock(Cell.class);
    Cell cell4 = mock(Cell.class);
    Cell cell5 = mock(Cell.class);
    Cell cell6 = mock(Cell.class);
    Cell cell7 = mock(Cell.class);
    Cell cell8 = mock(Cell.class);
    Cell cell9 = mock(Cell.class);
    Cell cell10 = mock(Cell.class);
    Cell cell11 = mock(Cell.class);
    Cell cell12 = mock(Cell.class);
    Cell cell13 = mock(Cell.class);
    Cell cell14 = mock(Cell.class);
    for (int i = 0; i < 1; i++) {
      when(sheet.getRow(1)).thenReturn(row1);
      when(row1.getCell(0)).thenReturn(cell0);
      when(row1.getCell(1)).thenReturn(cell1);
      when(row1.getCell(2)).thenReturn(cell2);
      when(row1.getCell(3)).thenReturn(cell3);
      when(row1.getCell(4)).thenReturn(cell4);
      when(row1.getCell(5)).thenReturn(cell5);
      when(row1.getCell(6)).thenReturn(cell6);
      when(row1.getCell(7)).thenReturn(cell7);
      when(row1.getCell(8)).thenReturn(cell8);
      when(row1.getCell(9)).thenReturn(cell9);
      when(row1.getCell(10)).thenReturn(cell10);
      when(row1.getCell(11)).thenReturn(cell11);
      when(row1.getCell(12)).thenReturn(cell12);
      when(row1.getCell(13)).thenReturn(cell13);
      when(row1.getCell(14)).thenReturn(cell14);

      when(row1.getCell(0).getStringCellValue()).thenReturn("Nguyen Quang Huy");
      when(row1.getCell(1).getStringCellValue()).thenReturn("ROLE_ADMIN");
      when(row1.getCell(2).getNumericCellValue()).thenReturn(132487617D);
      when(row1.getCell(3).getStringCellValue()).thenReturn("Male");
      when(row1.getCell(4).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(5).getStringCellValue()).thenReturn("DEVELOP_1");
      when(row1.getCell(6).getStringCellValue()).thenReturn("IT");
      when(row1.getCell(7).getStringCellValue()).thenReturn("HN_OFFICE");
      when(row1.getCell(8).getStringCellValue()).thenReturn("BACK_OFFICE");
      when(row1.getCell(9).getStringCellValue()).thenReturn("FULL_TIME");
      when(row1.getCell(10).toString()).thenReturn("huynq100");
      when(row1.getCell(11).getStringCellValue()).thenReturn("TRAINEE");
      when(row1.getCell(12).toString()).thenReturn("huynq100@fpt.edu.vn");
      when(row1.getCell(13).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(14).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
    }
    humanManagementService.importExcelToEmployee(workbook);
  }

  @Test
  void test_importExcelToEmployee_NullHrmQuest() {
    when(workbook.getSheetAt(0)).thenReturn(sheet);
    when(sheet.getLastRowNum()).thenReturn(11);
    for (int i = 0; i <= 1; i++) {
      when(sheet.getRow(i)).thenReturn(row);
      when(row.getCell(0)).thenReturn(cell);
    }
    Row row1 = mock(Row.class);
    Cell cell0 = mock(Cell.class);
    Cell cell1 = mock(Cell.class);
    Cell cell2 = mock(Cell.class);
    Cell cell3 = mock(Cell.class);
    Cell cell4 = mock(Cell.class);
    Cell cell5 = mock(Cell.class);
    Cell cell6 = mock(Cell.class);
    Cell cell7 = mock(Cell.class);
    Cell cell8 = mock(Cell.class);
    Cell cell9 = mock(Cell.class);
    Cell cell10 = mock(Cell.class);
    Cell cell11 = mock(Cell.class);
    Cell cell12 = mock(Cell.class);
    Cell cell13 = mock(Cell.class);
    Cell cell14 = mock(Cell.class);
    for (int i = 0; i < 1; i++) {
      when(sheet.getRow(1)).thenReturn(row1);
      when(row1.getCell(0)).thenReturn(cell0);
      when(row1.getCell(1)).thenReturn(cell1);
      when(row1.getCell(2)).thenReturn(cell2);
      when(row1.getCell(3)).thenReturn(cell3);
      when(row1.getCell(4)).thenReturn(cell4);
      when(row1.getCell(5)).thenReturn(cell5);
      when(row1.getCell(6)).thenReturn(cell6);
      when(row1.getCell(7)).thenReturn(cell7);
      when(row1.getCell(8)).thenReturn(cell8);
      when(row1.getCell(9)).thenReturn(cell9);
      when(row1.getCell(10)).thenReturn(cell10);
      when(row1.getCell(11)).thenReturn(cell11);
      when(row1.getCell(12)).thenReturn(cell12);
      when(row1.getCell(13)).thenReturn(cell13);
      when(row1.getCell(14)).thenReturn(cell14);

      when(row1.getCell(0).getStringCellValue()).thenReturn(null);
      when(row1.getCell(1).getStringCellValue()).thenReturn("ROLE_ADMIN");
      when(row1.getCell(2).getNumericCellValue()).thenReturn(132487617D);
      when(row1.getCell(3).getStringCellValue()).thenReturn("Male");
      when(row1.getCell(4).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(5).getStringCellValue()).thenReturn("DEVELOP_1");
      when(row1.getCell(6).getStringCellValue()).thenReturn("IT");
      when(row1.getCell(7).getStringCellValue()).thenReturn("HN_OFFICE");
      when(row1.getCell(8).getStringCellValue()).thenReturn("BACK_OFFICE");
      when(row1.getCell(9).getStringCellValue()).thenReturn("FULL_TIME");
      when(row1.getCell(10).toString()).thenReturn("huynq100");
      when(row1.getCell(11).getStringCellValue()).thenReturn("TRAINEE");
      when(row1.getCell(12).toString()).thenReturn("huynq100@fpt.edu.vn");
      when(row1.getCell(13).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(14).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
    }
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.importExcelToEmployee(workbook));
  }

  @Test
  void test_importExcelToEmployee_PhoneInvalid() {
    when(workbook.getSheetAt(0)).thenReturn(sheet);
    when(sheet.getLastRowNum()).thenReturn(11);
    for (int i = 0; i <= 1; i++) {
      when(sheet.getRow(i)).thenReturn(row);
      when(row.getCell(0)).thenReturn(cell);
    }
    Row row1 = mock(Row.class);
    Cell cell0 = mock(Cell.class);
    Cell cell1 = mock(Cell.class);
    Cell cell2 = mock(Cell.class);
    Cell cell3 = mock(Cell.class);
    Cell cell4 = mock(Cell.class);
    Cell cell5 = mock(Cell.class);
    Cell cell6 = mock(Cell.class);
    Cell cell7 = mock(Cell.class);
    Cell cell8 = mock(Cell.class);
    Cell cell9 = mock(Cell.class);
    Cell cell10 = mock(Cell.class);
    Cell cell11 = mock(Cell.class);
    Cell cell12 = mock(Cell.class);
    Cell cell13 = mock(Cell.class);
    Cell cell14 = mock(Cell.class);
    for (int i = 0; i < 1; i++) {
      when(sheet.getRow(1)).thenReturn(row1);
      when(row1.getCell(0)).thenReturn(cell0);
      when(row1.getCell(1)).thenReturn(cell1);
      when(row1.getCell(2)).thenReturn(cell2);
      when(row1.getCell(3)).thenReturn(cell3);
      when(row1.getCell(4)).thenReturn(cell4);
      when(row1.getCell(5)).thenReturn(cell5);
      when(row1.getCell(6)).thenReturn(cell6);
      when(row1.getCell(7)).thenReturn(cell7);
      when(row1.getCell(8)).thenReturn(cell8);
      when(row1.getCell(9)).thenReturn(cell9);
      when(row1.getCell(10)).thenReturn(cell10);
      when(row1.getCell(11)).thenReturn(cell11);
      when(row1.getCell(12)).thenReturn(cell12);
      when(row1.getCell(13)).thenReturn(cell13);
      when(row1.getCell(14)).thenReturn(cell14);

      when(row1.getCell(0).getStringCellValue()).thenReturn("Nguyen Quang Huy");
      when(row1.getCell(1).getStringCellValue()).thenReturn("ROLE_ADMIN");
      when(row1.getCell(2).getNumericCellValue()).thenReturn(1234D);
      when(row1.getCell(3).getStringCellValue()).thenReturn("Male");
      when(row1.getCell(4).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(5).getStringCellValue()).thenReturn("DEVELOP_1");
      when(row1.getCell(6).getStringCellValue()).thenReturn("IT");
      when(row1.getCell(7).getStringCellValue()).thenReturn("HN_OFFICE");
      when(row1.getCell(8).getStringCellValue()).thenReturn("BACK_OFFICE");
      when(row1.getCell(9).getStringCellValue()).thenReturn("FULL_TIME");
      when(row1.getCell(10).toString()).thenReturn("huynq100");
      when(row1.getCell(11).getStringCellValue()).thenReturn("TRAINEE");
      when(row1.getCell(12).toString()).thenReturn("huynq100@fpt.edu.vn");
      when(row1.getCell(13).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(14).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
    }
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.importExcelToEmployee(workbook));
  }

  @Test
  void test_importExcelToEmployee_FullNameInvalid() {
    when(workbook.getSheetAt(0)).thenReturn(sheet);
    when(sheet.getLastRowNum()).thenReturn(11);
    for (int i = 0; i <= 1; i++) {
      when(sheet.getRow(i)).thenReturn(row);
      when(row.getCell(0)).thenReturn(cell);
    }
    Row row1 = mock(Row.class);
    Cell cell0 = mock(Cell.class);
    Cell cell1 = mock(Cell.class);
    Cell cell2 = mock(Cell.class);
    Cell cell3 = mock(Cell.class);
    Cell cell4 = mock(Cell.class);
    Cell cell5 = mock(Cell.class);
    Cell cell6 = mock(Cell.class);
    Cell cell7 = mock(Cell.class);
    Cell cell8 = mock(Cell.class);
    Cell cell9 = mock(Cell.class);
    Cell cell10 = mock(Cell.class);
    Cell cell11 = mock(Cell.class);
    Cell cell12 = mock(Cell.class);
    Cell cell13 = mock(Cell.class);
    Cell cell14 = mock(Cell.class);
    for (int i = 0; i < 1; i++) {
      when(sheet.getRow(1)).thenReturn(row1);
      when(row1.getCell(0)).thenReturn(cell0);
      when(row1.getCell(1)).thenReturn(cell1);
      when(row1.getCell(2)).thenReturn(cell2);
      when(row1.getCell(3)).thenReturn(cell3);
      when(row1.getCell(4)).thenReturn(cell4);
      when(row1.getCell(5)).thenReturn(cell5);
      when(row1.getCell(6)).thenReturn(cell6);
      when(row1.getCell(7)).thenReturn(cell7);
      when(row1.getCell(8)).thenReturn(cell8);
      when(row1.getCell(9)).thenReturn(cell9);
      when(row1.getCell(10)).thenReturn(cell10);
      when(row1.getCell(11)).thenReturn(cell11);
      when(row1.getCell(12)).thenReturn(cell12);
      when(row1.getCell(13)).thenReturn(cell13);
      when(row1.getCell(14)).thenReturn(cell14);

      when(row1.getCell(0).getStringCellValue()).thenReturn("23");
      when(row1.getCell(1).getStringCellValue()).thenReturn("ROLE_ADMIN");
      when(row1.getCell(2).getNumericCellValue()).thenReturn(132487617D);
      when(row1.getCell(3).getStringCellValue()).thenReturn("Male");
      when(row1.getCell(4).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(5).getStringCellValue()).thenReturn("DEVELOP_1");
      when(row1.getCell(6).getStringCellValue()).thenReturn("IT");
      when(row1.getCell(7).getStringCellValue()).thenReturn("HN_OFFICE");
      when(row1.getCell(8).getStringCellValue()).thenReturn("BACK_OFFICE");
      when(row1.getCell(9).getStringCellValue()).thenReturn("FULL_TIME");
      when(row1.getCell(10).toString()).thenReturn("huynq100");
      when(row1.getCell(11).getStringCellValue()).thenReturn("TRAINEE");
      when(row1.getCell(12).toString()).thenReturn("huynq100@fpt.edu.vn");
      when(row1.getCell(13).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(14).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
    }
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.importExcelToEmployee(workbook));
  }

  @Test
  void test_importExcelToEmployee_EmailInvalid() {
    when(workbook.getSheetAt(0)).thenReturn(sheet);
    when(sheet.getLastRowNum()).thenReturn(11);
    for (int i = 0; i <= 1; i++) {
      when(sheet.getRow(i)).thenReturn(row);
      when(row.getCell(0)).thenReturn(cell);
    }
    Row row1 = mock(Row.class);
    Cell cell0 = mock(Cell.class);
    Cell cell1 = mock(Cell.class);
    Cell cell2 = mock(Cell.class);
    Cell cell3 = mock(Cell.class);
    Cell cell4 = mock(Cell.class);
    Cell cell5 = mock(Cell.class);
    Cell cell6 = mock(Cell.class);
    Cell cell7 = mock(Cell.class);
    Cell cell8 = mock(Cell.class);
    Cell cell9 = mock(Cell.class);
    Cell cell10 = mock(Cell.class);
    Cell cell11 = mock(Cell.class);
    Cell cell12 = mock(Cell.class);
    Cell cell13 = mock(Cell.class);
    Cell cell14 = mock(Cell.class);
    for (int i = 0; i < 1; i++) {
      when(sheet.getRow(1)).thenReturn(row1);
      when(row1.getCell(0)).thenReturn(cell0);
      when(row1.getCell(1)).thenReturn(cell1);
      when(row1.getCell(2)).thenReturn(cell2);
      when(row1.getCell(3)).thenReturn(cell3);
      when(row1.getCell(4)).thenReturn(cell4);
      when(row1.getCell(5)).thenReturn(cell5);
      when(row1.getCell(6)).thenReturn(cell6);
      when(row1.getCell(7)).thenReturn(cell7);
      when(row1.getCell(8)).thenReturn(cell8);
      when(row1.getCell(9)).thenReturn(cell9);
      when(row1.getCell(10)).thenReturn(cell10);
      when(row1.getCell(11)).thenReturn(cell11);
      when(row1.getCell(12)).thenReturn(cell12);
      when(row1.getCell(13)).thenReturn(cell13);
      when(row1.getCell(14)).thenReturn(cell14);

      when(row1.getCell(0).getStringCellValue()).thenReturn("Nguyen Nguyen");
      when(row1.getCell(1).getStringCellValue()).thenReturn("ROLE_ADMIN");
      when(row1.getCell(2).getNumericCellValue()).thenReturn(1231324324D);
      when(row1.getCell(3).getStringCellValue()).thenReturn("Male");
      when(row1.getCell(4).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(5).getStringCellValue()).thenReturn("DEVELOP_1");
      when(row1.getCell(6).getStringCellValue()).thenReturn("IT");
      when(row1.getCell(7).getStringCellValue()).thenReturn("HN_OFFICE");
      when(row1.getCell(8).getStringCellValue()).thenReturn("BACK_OFFICE");
      when(row1.getCell(9).getStringCellValue()).thenReturn("FULL_TIME");
      when(row1.getCell(10).toString()).thenReturn("huynq100");
      when(row1.getCell(11).getStringCellValue()).thenReturn("TRAINEE");
      when(row1.getCell(12).toString()).thenReturn(null);
      when(row1.getCell(13).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
      when(row1.getCell(14).getLocalDateTimeCellValue())
          .thenReturn(LocalDateTime.now(ZoneId.systemDefault()));
    }
    assertThrows(
        CustomErrorException.class, () -> humanManagementService.importExcelToEmployee(workbook));
  }

  @Test
  void test_getListHumanResourceOfManager() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    humanManagementService.getListHumanResourceOfManager(QueryParam.defaultParam(), employeeId);
  }

  @Test
  void test_getListHumanResourceOfManager_IdNotExist() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            humanManagementService.getListHumanResourceOfManager(
                QueryParam.defaultParam(), employeeId));
  }

  @Test
  void test_getListManagerHigherOfArea() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeRepository.getLevelOfEmployee(employeeId)).thenReturn(1);
    humanManagementService.getListManagerHigherOfArea(employeeId);
    verify(employeeRepository, times(1)).getLevelOfEmployee(employeeId);
  }

  @Test
  void test_getListManagerHigherOfArea_IdNotExist() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () -> humanManagementService.getListManagerHigherOfArea(employeeId));
  }

  @Test
  void test_getListManagerLowerOfArea() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeRepository.getLevelOfEmployee(employeeId)).thenReturn(1);
    humanManagementService.getListManagerLowerOfArea(employeeId);
    verify(employeeRepository, times(1)).getLevelOfEmployee(employeeId);
  }

  @Test
  void test_getListManagerLowerOfArea_IdNotExist() {
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () -> humanManagementService.getListManagerLowerOfArea(employeeId));
  }
}
