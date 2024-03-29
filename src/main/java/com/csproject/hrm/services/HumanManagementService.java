package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.EmployeeNameAndID;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.jooq.QueryParam;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

public interface HumanManagementService {
  HrmResponseList getListHumanResource(QueryParam queryParam, String employeeId);

  void insertEmployee(HrmRequest hrmRequest);

  List<WorkingTypeDto> getListWorkingType();

  List<EmployeeTypeDto> getListEmployeeType();

  List<RoleDto> getListRoleType();

  List<OfficeDto> getListOffice();

  List<AreaDto> getListArea();

  List<JobDto> getListPosition();

  List<GradeDto> getListGradeByPosition(Long id);

  void exportEmployeeToCsv(Writer writer, List<String> list);

  void importCsvToEmployee(InputStream inputStream);

  //  List<String> getListManagerByName(String name);

  //  List<EmployeeNameAndID> getListEmployeeByManagement(String employeeId);

  void exportEmployeeToExcel(HttpServletResponse response, List<String> list);

  void importExcelToEmployee(Workbook workBook);

  HrmResponseList getListHumanResourceOfManager(QueryParam queryParam, String employeeId);

  List<EmployeeNameAndID> getListManagerHigherOfArea(String employeeId);

  List<EmployeeNameAndID> getListManagerLowerOfArea(String employeeId);

  void updateWorkingStatusForListEmployee(LocalDate dateCheck);
}
