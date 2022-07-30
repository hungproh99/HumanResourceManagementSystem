package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.dto.EmployeeTypeDto;
import com.csproject.hrm.dto.dto.RoleDto;
import com.csproject.hrm.dto.dto.WorkingTypeDto;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.response.EmployeeNameAndID;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
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
                  hrmPojo.getPersonalEmail())
              .execute();
          queries.add(
              insertWorkingContractAndWorkingPlaceRecord(
                  configuration,
                  hrmPojo.getEmployeeId(),
                  hrmPojo.getCompanyName(),
                  hrmPojo.isContractStatus(),
                  hrmPojo.isPlaceStatus(),
                  hrmPojo.getArea(),
                  hrmPojo.getPosition(),
                  hrmPojo.getOffice(),
                  hrmPojo.getGrade(),
                  hrmPojo.getStartDate(),
                  hrmPojo.getEndDate()));
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
                        hrmPojo.getPersonalEmail())
                    .execute();
                queries.add(
                    insertWorkingContractAndWorkingPlaceRecord(
                        configuration,
                        hrmPojo.getEmployeeId(),
                        hrmPojo.getCompanyName(),
                        hrmPojo.isContractStatus(),
                        hrmPojo.isPlaceStatus(),
                        hrmPojo.getArea(),
                        hrmPojo.getPosition(),
                        hrmPojo.getOffice(),
                        hrmPojo.getGrade(),
                        hrmPojo.getStartDate(),
                        hrmPojo.getEndDate()));
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
    return dslContext.fetchCount(countAllEmployee(conditions));
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
  public List<HrmResponse> findEmployeeByListId(QueryParam queryParam, List<String> list) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    Condition condition = noCondition();
    for (String id : list) {
      condition = condition.or(EMPLOYEE.EMPLOYEE_ID.eq(id));
    }
    conditions.add(condition);

    return findAllEmployee(conditions, sortFields, queryParam.pagination)
        .fetchInto(HrmResponse.class);
  }

  @Override
  public List<String> getListManagerByName(String name) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    if (name.contains("-")) {
      String split[] = name.split("-");
      return dslContext
          .select(EMPLOYEE.FULL_NAME.concat(" (").concat(EMPLOYEE.EMPLOYEE_ID).concat(")"))
          .from(EMPLOYEE)
          .leftJoin(ROLE_TYPE)
          .on(ROLE_TYPE.TYPE_ID.eq(EMPLOYEE.ROLE_TYPE))
          .where(
              EMPLOYEE
                  .FULL_NAME
                  .upper()
                  .like(PERCENT_CHARACTER + split[0].toUpperCase() + PERCENT_CHARACTER))
          .and(
              EMPLOYEE
                  .EMPLOYEE_ID
                  .upper()
                  .like(PERCENT_CHARACTER + split[1].toUpperCase() + PERCENT_CHARACTER))
          .and(ROLE_TYPE.ROLE.eq(ERole.ROLE_MANAGER.name()))
          .orderBy(EMPLOYEE.EMPLOYEE_ID.asc())
          .fetchInto(String.class);
    }
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

  @Override
  public List<String> getListEmployeeByNameAndId(String name) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    if (name.contains("-")) {
      String split[] = name.split("-");
      return dslContext
          .select(EMPLOYEE.FULL_NAME.concat(" (").concat(EMPLOYEE.EMPLOYEE_ID).concat(")"))
          .from(EMPLOYEE)
          .leftJoin(ROLE_TYPE)
          .on(ROLE_TYPE.TYPE_ID.eq(EMPLOYEE.ROLE_TYPE))
          .where(
              EMPLOYEE
                  .FULL_NAME
                  .upper()
                  .like(PERCENT_CHARACTER + split[0].toUpperCase() + PERCENT_CHARACTER))
          .and(
              EMPLOYEE
                  .EMPLOYEE_ID
                  .upper()
                  .like(PERCENT_CHARACTER + split[1].toUpperCase() + PERCENT_CHARACTER))
          .orderBy(EMPLOYEE.EMPLOYEE_ID.asc())
          .fetchInto(String.class);
    }
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
        .orderBy(EMPLOYEE.EMPLOYEE_ID.asc())
        .fetchInto(String.class);
  }

  public Select<?> countAllEmployee(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.EMPLOYEE_ID)
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
        .leftJoin(WORKING_TYPE)
        .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
        .where(conditions);
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
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
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

  private Insert<?> insertWorkingContractAndWorkingPlaceRecord(
      Configuration config,
      String employeeId,
      String companyName,
      boolean contractStatus,
      boolean placeStatus,
      Long area,
      Long job,
      Long office,
      Long grade,
      LocalDate startDate,
      LocalDate endDate) {

    final var query =
        DSL.using(config)
            .insertInto(
                WORKING_CONTRACT,
                WORKING_CONTRACT.EMPLOYEE_ID,
                WORKING_CONTRACT.COMPANY_NAME,
                WORKING_CONTRACT.CONTRACT_STATUS,
                WORKING_CONTRACT.START_DATE,
                WORKING_CONTRACT.END_DATE)
            .values(employeeId, companyName, contractStatus, startDate, endDate)
            .returningResult(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .fetchOne();

    return DSL.using(config)
        .insertInto(
            WORKING_PLACE,
            WORKING_PLACE.WORKING_PLACE_STATUS,
            WORKING_PLACE.AREA_ID,
            WORKING_PLACE.OFFICE_ID,
            WORKING_PLACE.JOB_ID,
            WORKING_PLACE.GRADE_ID,
            WORKING_PLACE.WORKING_CONTRACT_ID)
        .values(
            placeStatus,
            area,
            office,
            job,
            grade,
            query.getValue(WORKING_CONTRACT.WORKING_CONTRACT_ID));
  }

  @Override
  public HrmResponseList findAllEmployeeOfManager(QueryParam queryParam, String managerId) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    conditions.add(EMPLOYEE.WORKING_STATUS.isTrue());
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    List<HrmResponse> hrmResponses =
        findAllEmployeeOfManager(conditions, sortFields, queryParam.pagination, managerId)
            .fetchInto(HrmResponse.class);

    List<HrmResponse> hrmResponseList =
        getListHrmResponse(hrmResponses, conditions, sortFields, queryParam.pagination);

    List<HrmResponse> hrmResponsesCount =
        countAllEmployeeOfManager(conditions, managerId).fetchInto(HrmResponse.class);

    List<HrmResponse> hrmResponsesCountList =
        getCountListHrmResponse(hrmResponsesCount, conditions);

    hrmResponses.forEach(
        hrmResponse -> {
          hrmResponse.setArea_name(EArea.getLabel(hrmResponse.getArea_name()));
          hrmResponse.setGrade(EGradeType.getLabel(hrmResponse.getGrade()));
          hrmResponse.setPosition_name(EJob.getLabel(hrmResponse.getPosition_name()));
          hrmResponse.setOffice_name(EOffice.getLabel(hrmResponse.getOffice_name()));
          hrmResponse.setWorking_name(EWorkingType.getLabel(hrmResponse.getWorking_name()));
        });

    return HrmResponseList.builder()
        .hrmResponse(hrmResponseList)
        .total(hrmResponsesCountList.size())
        .build();
  }

  @Override
  public void updateStatusEmployee(String employeeId, boolean status) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(EMPLOYEE)
            .set(EMPLOYEE.WORKING_STATUS, status)
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId));
  }

  public Select<?> countAllEmployeeOfManager(List<Condition> conditions, String managerId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.EMPLOYEE_ID)
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
        .leftJoin(WORKING_TYPE)
        .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
        .where(conditions)
        .and(EMPLOYEE.MANAGER_ID.eq(managerId));
  }

  public Select<?> findAllEmployeeOfManager(
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination,
      String managerId) {
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
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .leftJoin(OFFICE)
        .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
        .leftJoin(JOB)
        .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
        .leftJoin(GRADE_TYPE)
        .on(GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
        .leftJoin(WORKING_TYPE)
        .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
        .where(conditions)
        .and(EMPLOYEE.MANAGER_ID.eq(managerId))
        .orderBy(sortFields)
        .limit(pagination.limit)
        .offset(pagination.offset);
  }

  private List<HrmResponse> getListHrmResponse(
      List<HrmResponse> list,
      List<Condition> conditions,
      List<OrderField<?>> sortFields,
      Pagination pagination) {
    outer:
    while (true) {
      for (int i = 0; i < list.size(); i++) {
        List<HrmResponse> hrmResponsesSub =
            findAllEmployeeOfManager(
                    conditions, sortFields, pagination, list.get(i).getEmployee_id())
                .fetchInto(HrmResponse.class);
        if (hrmResponsesSub.isEmpty() && i == list.size() - 1) {
          break outer;
        } else if (hrmResponsesSub.isEmpty()) {
          continue;
        } else {
          hrmResponsesSub = getListHrmResponse(hrmResponsesSub, conditions, sortFields, pagination);
          list =
              Stream.of(list, hrmResponsesSub)
                  .flatMap(x -> x.stream())
                  .collect(Collectors.toList());
        }
      }
    }
    return list;
  }

  private List<HrmResponse> getCountListHrmResponse(
      List<HrmResponse> list, List<Condition> conditions) {
    outer:
    while (true) {
      for (int i = 0; i < list.size(); i++) {
        List<HrmResponse> hrmResponsesSub =
            countAllEmployeeOfManager(conditions, list.get(i).getEmployee_id())
                .fetchInto(HrmResponse.class);
        if (hrmResponsesSub.isEmpty() && i == list.size() - 1) {
          break outer;
        } else if (hrmResponsesSub.isEmpty()) {
          continue;
        } else {
          hrmResponsesSub = getCountListHrmResponse(hrmResponsesSub, conditions);
          list =
              Stream.of(list, hrmResponsesSub)
                  .flatMap(x -> x.stream())
                  .collect(Collectors.toList());
        }
      }
    }
    return list;
  }

  @Override
  public String getEmployeeNameByEmployeeId(String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.FULL_NAME)
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
        .fetchOneInto(String.class);
  }

  @Override
  public String getEmployeeEmailByEmployeeId(String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.COMPANY_EMAIL)
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
        .fetchOneInto(String.class);
  }

  @Override
  public List<String> getAllEmployeeIdActive() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.EMPLOYEE_ID)
        .from(EMPLOYEE)
        .where(EMPLOYEE.WORKING_STATUS.isTrue())
        .fetchInto(String.class);
  }

  @Override
  public Optional<HrmResponse> getEmployeeByEmployeeId(String employeeId) {
    List<Condition> conditions = new ArrayList<>();
    conditions.add(EMPLOYEE.EMPLOYEE_ID.eq(employeeId));
    List<OrderField<?>> sortFields = new ArrayList<>();
    sortFields.add(EMPLOYEE.EMPLOYEE_ID.desc());
    return findAllEmployee(conditions, sortFields, Pagination.defaultPage())
        .fetchOptionalInto(HrmResponse.class);
  }

  @Override
  public List<EmployeeNameAndID> getListManagerHigherOfArea(String employeeId, Integer level) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var area =
        dslContext
            .select(AREA.NAME)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .fetchOneInto(String.class);

    return dslContext
        .select(EMPLOYEE.FULL_NAME.as("name"), EMPLOYEE.EMPLOYEE_ID.as("employeeID"))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .where(EMPLOYEE.EMPLOYEE_ID.notEqual(employeeId))
        .and(EMPLOYEE.LEVEL.le(level))
        .and(EMPLOYEE.LEVEL.gt(0))
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .and(AREA.NAME.eq(area))
        .fetchInto(EmployeeNameAndID.class);
  }

  @Override
  public List<EmployeeNameAndID> getListManagerLowerOfArea(String employeeId, Integer level) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var area =
        dslContext
            .select(AREA.NAME)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .fetchOneInto(String.class);

    return dslContext
        .select(EMPLOYEE.FULL_NAME.as("name"), EMPLOYEE.EMPLOYEE_ID.as("employeeID"))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .where(EMPLOYEE.EMPLOYEE_ID.notEqual(employeeId))
        .and(EMPLOYEE.LEVEL.ge(level))
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .and(AREA.NAME.eq(area))
        .fetchInto(EmployeeNameAndID.class);
  }

  @Override
  public int getLevelOfEmployee(String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.LEVEL)
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
        .fetchOneInto(Integer.class);
  }
}
