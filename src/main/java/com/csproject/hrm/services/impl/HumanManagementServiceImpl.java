package com.csproject.hrm.services.impl;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.request.UpdateHrmRequest;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.jooq.QueryParam;

import java.io.Writer;
import java.util.List;

public interface HumanManagementServiceImpl {
  HrmResponseList getListHumanResource(QueryParam queryParam);

  void insertEmployee(HrmRequest hrmRequest);

  void insertMultiEmployee(List<HrmRequest> hrmRequestList);

  List<WorkingTypeDto> getListWorkingType();

  List<EmployeeTypeDto> getListEmployeeType();

  List<RoleDto> getListRoleType();

  List<OfficeDto> getListOffice();

  List<AreaDto> getListArea();

  List<JobDto> getListJob();

  void updateEmployeeById(UpdateHrmRequest updateHrmRequest, String employeeId);

  void exportEmployeeToCsv(Writer writer, List<String> list);
}
