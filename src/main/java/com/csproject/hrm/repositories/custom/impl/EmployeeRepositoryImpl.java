package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.EmployeeRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.codegen.maven.example.tables.records.EmployeeRecord;
import org.jooq.codegen.maven.example.tables.records.WorkingContractRecord;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.WORKING_PLACE;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.ContractType.CONTRACT_TYPE;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Job.JOB;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.*;

@AllArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
  private static final Logger Logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
    field2Map.put(FULL_NAME, EMPLOYEE.FULL_NAME);
    field2Map.put(STATUS, EMPLOYEE.WORK_STATUS);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<HrmResponse> findAllEmployee(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);

    return findAllEmployee(conditions, sortFields, queryParam.pagination);
  }

  @Override
  public void insertEmployee(
      HrmRequest hrmRequest,
      String employeeId,
      String companyEmail,
      String password,
      String workStatus,
      String companyName,
      String contractStatus,
      LocalDate startDate) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          DSLContext ctx = DSL.using(configuration);
          EmployeeRecord employeeRecord =
              ctx.insertInto(
                      EMPLOYEE,
                      EMPLOYEE.EMPLOYEE_ID,
                      EMPLOYEE.FULL_NAME,
                      EMPLOYEE.COMPANY_EMAIL,
                      EMPLOYEE.WORK_STATUS,
                      EMPLOYEE.PHONE_NUMBER,
                      EMPLOYEE.GENDER,
                      EMPLOYEE.BIRTH_DATE,
                      EMPLOYEE.ROLE_TYPE)
                  .values(
                      employeeId,
                      hrmRequest.getFullName(),
                      companyEmail,
                      workStatus,
                      hrmRequest.getPhone(),
                      hrmRequest.getGender(),
                      hrmRequest.getBirthDate(),
                      ERole.of(hrmRequest.getRole()))
                  .returning(EMPLOYEE.EMPLOYEE_ID)
                  .fetchOne();

          WorkingContractRecord workingContractRecord =
              ctx.insertInto(
                      WORKING_CONTRACT,
                      WORKING_CONTRACT.EMPLOYEE_ID,
                      WORKING_CONTRACT.BASE_SALARY,
                      WORKING_CONTRACT.COMPANY_NAME,
                      WORKING_CONTRACT.START_DATE,
                      WORKING_CONTRACT.STATUS,
                      WORKING_CONTRACT.TYPE_ID)
                  .values(
                      employeeId,
                      hrmRequest.getBaseSalary(),
                      companyName,
                      startDate,
                      contractStatus,
                      EContractType.of(hrmRequest.getContractName()))
                  .returning()
                  .fetchOne();

          ctx.insertInto(
                  WORKING_PLACE,
                  WORKING_PLACE.WORKING_CONTRACT_ID,
                  WORKING_PLACE.AREA_ID,
                  WORKING_PLACE.JOB_ID,
                  WORKING_PLACE.OFFICE_ID)
              .values(
                  workingContractRecord.getWorkingContractId(), EArea.of(hrmRequest.getArea()),
                  EJob.of(hrmRequest.getJob()), EOffice.of(hrmRequest.getOffice()))
              .execute();
        });
  }

  public int countEmployeeSameStartName(String standForName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .selectCount()
            .from(EMPLOYEE)
            .where(
                condition(
                    "{0} REGEXP {1}", EMPLOYEE.EMPLOYEE_ID, val("^" + standForName + "[0-9]+$")));

    return query.fetchOne(0, int.class);
  }

  @Override
  public int countAllEmployeeByCondition(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    List<OrderField<?>> sortFields =
        queryHelper.queryOrderBy(queryParam, field2Map, EMPLOYEE.EMPLOYEE_ID);
    return countAllEmployeeByCondition(conditions, sortFields, queryParam.pagination);
  }

  public int countAllEmployeeByCondition(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .selectCount()
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(CONTRACT_TYPE)
            .on(CONTRACT_TYPE.TYPE_ID.eq(WORKING_CONTRACT.TYPE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .leftJoin(OFFICE)
            .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
            .leftJoin(JOB)
            .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
            .where(conditions)
            .orderBy(sortFields)
            .limit(pagination.limit)
            .offset(pagination.offset);

    return query.fetchOne(0, int.class);
  }

  public List<HrmResponse> findAllEmployee(
      List<Condition> conditions, List<OrderField<?>> sortFields, Pagination pagination) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EMPLOYEE.EMPLOYEE_ID,
                EMPLOYEE.FULL_NAME,
                EMPLOYEE.COMPANY_EMAIL.as(EMAIL),
                EMPLOYEE.WORK_STATUS,
                EMPLOYEE.PHONE_NUMBER.as(PHONE),
                when(EMPLOYEE.GENDER.eq(FEMALE), FEMALE)
                    .when(EMPLOYEE.GENDER.eq(MALE), MALE)
                    .as(GENDER),
                EMPLOYEE.BIRTH_DATE,
                JOB.TITLE.as(JOB_NAME),
                OFFICE.NAME.as(OFFICE_NAME),
                AREA.NAME.as(AREA_NAME),
                CONTRACT_TYPE.NAME.as(CONTRACT),
                year(currentDate())
                    .minus(year(WORKING_CONTRACT.START_DATE))
                    .concat(YEAR)
                    .concat(month(currentDate()).minus(month(WORKING_CONTRACT.START_DATE)))
                    .concat(MONTH)
                    .concat(day(currentDate()).minus(day(WORKING_CONTRACT.START_DATE)))
                    .concat(DAY)
                    .as(SENIORITY),
                WORKING_CONTRACT.START_DATE)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(CONTRACT_TYPE)
            .on(CONTRACT_TYPE.TYPE_ID.eq(WORKING_CONTRACT.TYPE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .leftJoin(OFFICE)
            .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
            .leftJoin(JOB)
            .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
            .where(conditions)
            .orderBy(sortFields)
            .limit(pagination.limit)
            .offset(pagination.offset);

    return query.fetchInto(HrmResponse.class);
  }
}