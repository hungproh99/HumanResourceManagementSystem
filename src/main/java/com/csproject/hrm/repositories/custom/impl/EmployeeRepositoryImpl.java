package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.request.HrmPojo;
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
import static org.jooq.codegen.maven.example.Tables.WORKING_TYPE;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
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

    return findAllEmployee(conditions, sortFields, queryParam.pagination)
        .fetchInto(HrmResponse.class);
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
                  EWorkStatus.of(hrmPojo.getWorkStatus()),
                  hrmPojo.getFullName(),
                  ERole.of(hrmPojo.getRole()),
                  hrmPojo.getPhone(),
                  hrmPojo.getGender(),
                  hrmPojo.getBirthDate(),
                  EWorkingType.of(hrmPojo.getWorkingType()),
                  hrmPojo.getManagerId(),
                  EEmployeeType.of(hrmPojo.getEmployeeType())));
          queries.add(
              insertWorkingContractRecord(
                  configuration,
                  hrmPojo.getEmployeeId(),
                  hrmPojo.getCompanyName(),
                  EArea.of(hrmPojo.getArea()),
                  EJob.of(hrmPojo.getGrade(), hrmPojo.getPosition()),
                  EOffice.of(hrmPojo.getOffice())));
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

  public Select<?> findAllEmployee(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
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
            JOB.GRADE.as(GRADE),
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
      long role,
      String phone,
      String gender,
      LocalDate birthDate,
      long workingType,
      String managerId,
      long employeeType) {
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
            EMPLOYEE.EMPLOYEE_TYPE_ID)
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
            employeeType);
  }

  private Insert<?> insertWorkingContractRecord(
      Configuration config,
      String employeeId,
      String companyName,
      long area,
      long job,
      long office) {

    return DSL.using(config)
        .insertInto(
            WORKING_CONTRACT,
            WORKING_CONTRACT.EMPLOYEE_ID,
            WORKING_CONTRACT.COMPANY_NAME,
            WORKING_CONTRACT.AREA_ID,
            WORKING_CONTRACT.OFFICE_ID,
            WORKING_CONTRACT.JOB_ID)
        .values(employeeId, companyName, area, job, office);
  }
}
