package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.EmployeeTypeDto;
import com.csproject.hrm.dto.dto.RoleDto;
import com.csproject.hrm.dto.dto.WorkingTypeDto;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.request.UpdateHrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepositoryCustom {
  List<HrmResponse> findAllEmployee(QueryParam queryParam);

  void insertEmployee(HrmPojo hrmPojo);

  void insertMultiEmployee(List<HrmPojo> hrmPojos);

  int countEmployeeSameStartName(String fullName);

  int countAllEmployeeByCondition(QueryParam queryParam);

  List<WorkingTypeDto> getListWorkingType();

  List<EmployeeTypeDto> getListEmployeeType();

  List<RoleDto> getListRoleType(boolean isAdmin);

  void updateEmployeeById(UpdateHrmRequest updateHrmRequest, String employeeId);

  List<HrmResponse> findEmployeeByListId(List<String> list);

  List<String> getListManagerByName(String name);
}