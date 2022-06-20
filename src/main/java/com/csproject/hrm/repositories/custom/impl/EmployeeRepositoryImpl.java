package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.dto.EmployeeTypeDto;
import com.csproject.hrm.dto.dto.RoleDto;
import com.csproject.hrm.dto.dto.WorkingTypeDto;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.request.UpdateHrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.jooq.Pagination;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.custom.EmployeeRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.GradeType.GRADE_TYPE;
import static org.jooq.codegen.maven.example.tables.Job.JOB;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.*;

@AllArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
    field2Map.put(FULL_NAME, EMPLOYEE.FULL_NAME);
    field2Map.put(WORKING_STATUS, EMPLOYEE.WORKING_STATUS);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<HrmResponse> findAllEmployee(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    List<HrmResponse> hrmResponses =
        findAllEmployee(conditions, sortFields, queryParam.pagination).fetchInto(HrmResponse.class);
    hrmResponses.forEach(
        hrmResponse -> {
          hrmResponse.setArea_name(EArea.getLabel(hrmResponse.getArea_name()));
          hrmResponse.setGrade(EGradeType.getLabel(hrmResponse.getGrade()));
          hrmResponse.setPosition_name(EJob.getLabel(hrmResponse.getPosition_name()));
          hrmResponse.setOffice_name(EOffice.getLabel(hrmResponse.getOffice_name()));
          hrmResponse.setWorking_name(EWorkingType.getLabel(hrmResponse.getWorking_name()));
        });
    return hrmResponses;
  }

  @Override
  public void insertEmployee(HrmPojo hrmPojo) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          queries.add(
              insertEmployeeRecord(
                  configuration,
                  hrmPojo.getEmployeeId(),
                  hrmPojo.getCompanyEmail(),
                  hrmPojo.getPassword(),
                  hrmPojo.isWorkStatus(),
                  hrmPojo.getFullName(),
                  hrmPojo.getRole(),
                  hrmPojo.getPhone(),
                  hrmPojo.getGender(),
                  hrmPojo.getBirthDate(),
                  hrmPojo.getWorkingType(),
                  hrmPojo.getManagerId(),
                  hrmPojo.getEmployeeType(),
                  hrmPojo.getPersonalEmail()));
          queries.add(
              insertWorkingContractRecord(
                  configuration,
                  hrmPojo.getEmployeeId(),
                  hrmPojo.getCompanyName(),
                  hrmPojo.getArea(),
                  hrmPojo.getPosition(),
                  hrmPojo.getOffice(),
                  hrmPojo.getGrade()));
          DSL.using(configuration).batch(queries).execute();
        });
  }

  @Override
  public void insertMultiEmployee(List<HrmPojo> hrmPojos) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          hrmPojos.forEach(
              hrmPojo -> {
                queries.add(
                    insertEmployeeRecord(
                        configuration,
                        hrmPojo.getEmployeeId(),
                        hrmPojo.getCompanyEmail(),
                        hrmPojo.getPassword(),
                        hrmPojo.isWorkStatus(),
                        hrmPojo.getFullName(),
                        hrmPojo.getRole(),
                        hrmPojo.getPhone(),
                        hrmPojo.getGender(),
                        hrmPojo.getBirthDate(),
                        hrmPojo.getWorkingType(),
                        hrmPojo.getManagerId(),
                        hrmPojo.getEmployeeType(),
                        hrmPojo.getPersonalEmail()));
                queries.add(
                    insertWorkingContractRecord(
                        configuration,
                        hrmPojo.getEmployeeId(),
                        hrmPojo.getCompanyName(),
                        hrmPojo.getArea(),
                        hrmPojo.getPosition(),
                        hrmPojo.getOffice(),
                        hrmPojo.getGrade()));
              });

          DSL.using(configuration).batch(queries).execute();
        });
  }

  public int countEmployeeSameStartName(String standForName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select()
            .from(EMPLOYEE)
            .where(
                condition(
                    "{0} REGEXP {1}", EMPLOYEE.EMPLOYEE_ID, val("^" + standForName + "[0-9]+$")));

    return dslContext.fetchCount(query);
  }

  @Override
  public int countAllEmployeeByCondition(QueryParam queryParam) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    return dslContext.fetchCount(findAllEmployee(conditions, sortFields, queryParam.pagination));
  }

  @Override
  public List<WorkingTypeDto> getListWorkingType() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(WORKING_TYPE.TYPE_ID, WORKING_TYPE.NAME, WORKING_TYPE.DESCRIPTION)
        .from(WORKING_TYPE)
        .orderBy(WORKING_TYPE.TYPE_ID.asc())
        .fetchInto(WorkingTypeDto.class);
  }

  @Override
  public List<EmployeeTypeDto> getListEmployeeType() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE_TYPE.TYPE_ID, EMPLOYEE_TYPE.NAME, EMPLOYEE_TYPE.DESCRIPTION)
        .from(EMPLOYEE_TYPE)
        .orderBy(EMPLOYEE_TYPE.TYPE_ID.asc())
        .fetchInto(EmployeeTypeDto.class);
  }

  @Override
  public List<RoleDto> getListRoleType(boolean isAdmin) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Condition> conditions = new ArrayList<>();
    if (!isAdmin) {
      conditions.add(ROLE_TYPE.ROLE.notLike(ERole.ROLE_ADMIN.name()));
    }
    return dslContext
        .select(ROLE_TYPE.TYPE_ID, ROLE_TYPE.ROLE)
        .from(ROLE_TYPE)
        .where(conditions)
        .orderBy(ROLE_TYPE.TYPE_ID.asc())
        .fetchInto(RoleDto.class);
  }

  @Override
  public void updateEmployeeById(UpdateHrmRequest updateHrmRequest, String employeeId) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          queries.add(
              updateEmployeeById(
                  configuration,
                  employeeId,
                  updateHrmRequest.getFullName(),
                  updateHrmRequest.isWorkingStatus(),
                  updateHrmRequest.getPhone(),
                  updateHrmRequest.getGender(),
                  updateHrmRequest.getBirthDate(),
                  updateHrmRequest.getWorkingType()));
          queries.add(
              updateWorkingContractById(
                  configuration,
                  employeeId,
                  updateHrmRequest.getAreaId(),
                  updateHrmRequest.getJobId(),
                  updateHrmRequest.getOfficeId(),
                  updateHrmRequest.getStartDate()));
          DSL.using(configuration).batch(queries).execute();
        });
  }

  @Override
  public List<HrmResponse> findEmployeeByListId(List<String> list) {
    List<Condition> conditions = new ArrayList<>();
    Condition condition = noCondition();
    for (String id : list) {
      condition = condition.or(EMPLOYEE.EMPLOYEE_ID.eq(id));
    }
    conditions.add(condition);

    return findEmployeeByListIdByCondition(conditions).fetchInto(HrmResponse.class);
  }

  @Override
  public List<String> getListManagerByName(String name) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.FULL_NAME.concat(" (").concat(EMPLOYEE.EMPLOYEE_ID).concat(")"))
        .from(EMPLOYEE)
        .leftJoin(ROLE_TYPE)
        .on(ROLE_TYPE.TYPE_ID.eq(EMPLOYEE.ROLE_TYPE))
        .where(
            EMPLOYEE
                .FULL_NAME
                .upper()
                .like(PERCENT_CHARACTER + name.toUpperCase() + PERCENT_CHARACTER))
        .and(ROLE_TYPE.ROLE.eq(ERole.ROLE_MANAGER.name()))
        .orderBy(EMPLOYEE.EMPLOYEE_ID.asc())
        .fetchInto(String.class);
  }

  public Select<?> findAllEmployee(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            EMPLOYEE.COMPANY_EMAIL.as(EMAIL),
            (when(EMPLOYEE.WORKING_STATUS.eq(Boolean.TRUE), ACTIVE)
                    .when(EMPLOYEE.WORKING_STATUS.eq(Boolean.FALSE), DEACTIVE))
                .as(WORKING_STATUS),
            EMPLOYEE.PHONE_NUMBER.as(PHONE),
            EMPLOYEE.GENDER.as(GENDER),
            EMPLOYEE.BIRTH_DATE,
            GRADE_TYPE.NAME.as(GRADE),
            OFFICE.NAME.as(OFFICE_NAME),
            AREA.NAME.as(AREA_NAME),
            year(currentDate())
                .minus(year(WORKING_CONTRACT.START_DATE))
                .concat(YEAR)
                .concat(month(currentDate().minus(month(WORKING_CONTRACT.START_DATE))))
                .concat(MONTH)
                .concat(day(currentDate().minus(day(WORKING_CONTRACT.START_DATE))))
                .concat(DAY)
                .as(SENIORITY),
            JOB.POSITION.as(POSITION_NAME),
            WORKING_TYPE.NAME.as(WORKING_NAME))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_CONTRACT.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_CONTRACT.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
        .leftJoin(WORKING_TYPE)
        .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
        .where(conditions)
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  private Insert<?> insertEmployeeRecord(
      Configuration config,
      String employeeId,
      String companyEmail,
      String password,
      boolean workStatus,
      String fullName,
      Long role,
      String phone,
      String gender,
      LocalDate birthDate,
      Long workingType,
      String managerId,
      Long employeeType,
      String personalEmail) {
    return DSL.using(config)
        .insertInto(
            EMPLOYEE,
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.COMPANY_EMAIL,
            EMPLOYEE.PASSWORD,
            EMPLOYEE.WORKING_STATUS,
            EMPLOYEE.FULL_NAME,
            EMPLOYEE.ROLE_TYPE,
            EMPLOYEE.PHONE_NUMBER,
            EMPLOYEE.GENDER,
            EMPLOYEE.BIRTH_DATE,
            EMPLOYEE.WORKING_TYPE_ID,
            EMPLOYEE.MANAGER_ID,
            EMPLOYEE.EMPLOYEE_TYPE_ID,
            EMPLOYEE.PERSONAL_EMAIL)
        .values(
            employeeId,
            companyEmail,
            password,
            workStatus,
            fullName,
            role,
            phone,
            gender,
            birthDate,
            workingType,
            managerId,
            employeeType,
            personalEmail);
  }

  private Insert<?> insertWorkingContractRecord(
      Configuration config,
      String employeeId,
      String companyName,
      Long area,
      Long job,
      Long office,
      Long grade) {

    return DSL.using(config)
        .insertInto(
            WORKING_CONTRACT,
            WORKING_CONTRACT.EMPLOYEE_ID,
            WORKING_CONTRACT.COMPANY_NAME,
            WORKING_CONTRACT.AREA_ID,
            WORKING_CONTRACT.OFFICE_ID,
            WORKING_CONTRACT.JOB_ID,
            WORKING_CONTRACT.GRADE_ID)
        .values(employeeId, companyName, area, office, job, grade);
  }

  private Update<?> updateEmployeeById(
      Configuration config,
      String employeeId,
      String fullName,
      boolean workStatus,
      String phoneNumber,
      String gender,
      LocalDate birthDate,
      Long workingType) {
    return DSL.using(config)
        .update(EMPLOYEE)
        .set(EMPLOYEE.FULL_NAME, fullName)
        .set(EMPLOYEE.WORKING_STATUS, workStatus)
        .set(EMPLOYEE.PHONE_NUMBER, phoneNumber)
        .set(EMPLOYEE.GENDER, gender)
        .set(EMPLOYEE.BIRTH_DATE, birthDate)
        .set(EMPLOYEE.WORKING_TYPE_ID, workingType)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId));
  }

  private Update<?> updateWorkingContractById(
      Configuration config,
      String employeeId,
      Long areaId,
      Long jobId,
      Long officeId,
      LocalDate startDate) {
    return DSL.using(config)
        .update(WORKING_CONTRACT)
        .set(WORKING_CONTRACT.AREA_ID, areaId)
        .set(WORKING_CONTRACT.JOB_ID, jobId)
        .set(WORKING_CONTRACT.OFFICE_ID, officeId)
        .set(WORKING_CONTRACT.START_DATE, startDate)
        .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeId));
  }

  private Select<?> findEmployeeByListIdByCondition(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            EMPLOYEE.COMPANY_EMAIL.as(EMAIL),
            (when(EMPLOYEE.WORKING_STATUS.eq(Boolean.TRUE), "Active")
                    .when(EMPLOYEE.WORKING_STATUS.eq(Boolean.FALSE), "Deactive"))
                .as(WORKING_STATUS),
            EMPLOYEE.PHONE_NUMBER.as(PHONE),
            EMPLOYEE.GENDER.as(GENDER),
            EMPLOYEE.BIRTH_DATE,
            GRADE_TYPE.NAME.as(GRADE),
            OFFICE.NAME.as(OFFICE_NAME),
            AREA.NAME.as(AREA_NAME),
            year(currentDate())
                .minus(year(WORKING_CONTRACT.START_DATE))
                .concat(YEAR)
                .concat(month(currentDate()).minus(month(WORKING_CONTRACT.START_DATE)))
                .concat(MONTH)
                .concat(day(currentDate()).minus(day(WORKING_CONTRACT.START_DATE)))
                .concat(DAY)
                .as(SENIORITY),
            JOB.POSITION.as(POSITION_NAME),
            WORKING_TYPE.NAME.as(WORKING_NAME))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_CONTRACT.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_CONTRACT.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
        .leftJoin(WORKING_TYPE)
        .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
        .where(conditions)
        .orderBy(EMPLOYEE.EMPLOYEE_ID.asc());
  }
}
