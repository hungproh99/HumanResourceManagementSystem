package com.csproject.hrm.services;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.request.UpdateHrmRequest;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.jooq.QueryParam;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public interface HumanManagementService {
  HrmResponseList getListHumanResource(QueryParam queryParam);

  void insertEmployee(HrmRequest hrmRequest);

  List<WorkingTypeDto> getListWorkingType();

  List<EmployeeTypeDto> getListEmployeeType();

  List<RoleDto> getListRoleType();

  List<OfficeDto> getListOffice();

  List<AreaDto> getListArea();

  List<JobDto> getListPosition();

  List<GradeDto> getListGradeByPosition(String id);

  void updateEmployeeById(UpdateHrmRequest updateHrmRequest, String employeeId);

  void exportEmployeeToCsv(Writer writer, List<String> list);

  void importCsvToEmployee(InputStream inputStream);

  List<String> getListManagerByName(QueryParam queryParam);
}