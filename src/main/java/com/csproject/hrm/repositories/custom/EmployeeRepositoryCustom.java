package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.EmployeeTypeDto;
import com.csproject.hrm.dto.dto.RoleDto;
import com.csproject.hrm.dto.dto.WorkingTypeDto;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

  List<HrmResponse> findEmployeeByListId(QueryParam queryParam, List<String> list);

  List<String> getListManagerByName(String name);

  List<String> getListEmployeeByNameAndId(String name);

  HrmResponseList findAllEmployeeOfManager(QueryParam queryParam, String managerId);

  void updateStatusEmployee(String employeeId, boolean status);

  String getEmployeeNameByEmployeeId(String employeeId);

  String getEmployeeEmailByEmployeeId(String employeeId);

  List<String> getAllEmployeeIdActive();

  Optional<HrmResponse> getEmployeeByEmployeeId(String employeeId);
}