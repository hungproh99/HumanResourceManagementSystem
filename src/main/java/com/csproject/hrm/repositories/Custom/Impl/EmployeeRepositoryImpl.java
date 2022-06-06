package com.csproject.hrm.repositories.Custom.Impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.jooq.Pagination;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.Custom.EmployeeRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.codegen.maven.example.tables.records.WorkingContractRecord;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //    field2Map.put(STATUS, EMPLOYEE.W);
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
  public void insertEmployee(HrmPojo hrmPojo) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          insertEmployeeRecord(
              configuration,
              hrmPojo.getEmployeeId(),
              hrmPojo.getFullName(),
              hrmPojo.getCompanyEmail(),
              hrmPojo.getWorkStatus(),
              hrmPojo.getPhone(),
              hrmPojo.getGender(),
              hrmPojo.getBirthDate(),
              ERole.of(hrmPojo.getRole()));

          WorkingContractRecord workingContractRecord =
              insertWorkingContractRecord(
                      configuration,
                      hrmPojo.getEmployeeId(),
                      BigDecimal.ONE,
                      hrmPojo.getCompanyName(),
                      hrmPojo.getStartDate(),
                      "hrmPojo.getContractStatus()",
                      EContractType.of("hrmPojo.getContractName()"))
                  .fetchOne();

          insertWorkingPlace(
              configuration,
              workingContractRecord.getWorkingContractId(),
              EArea.of(hrmPojo.getArea()),
              EJob.of(hrmPojo.getJob()),
              EOffice.of(hrmPojo.getOffice()));
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
                when(EMPLOYEE.WORKING_STATUS.eq(Byte.parseByte("1")), ""),
                EMPLOYEE.PHONE_NUMBER.as(PHONE),
                EMPLOYEE.GENDER.as(GENDER),
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
                    .as(SENIORITY))
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            //            .leftJoin(CONTRACT_TYPE)
            //            .on(CONTRACT_TYPE.TYPE_ID.eq(WORKING_CONTRACT.TYPE_ID))
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

  private int insertEmployeeRecord(
      Configuration config,
      String employeeId,
      String fullName,
      String companyEmail,
      String workStatus,
      String phone,
      String gender,
      LocalDate birthDate,
      long role) {
    final var query =
        DSL.using(config)
            .insertInto(
                EMPLOYEE,
                EMPLOYEE.EMPLOYEE_ID,
                EMPLOYEE.FULL_NAME,
                EMPLOYEE.COMPANY_EMAIL,
                //                "EMPLOYEE.WORK_STATUS",
                EMPLOYEE.PHONE_NUMBER,
                EMPLOYEE.GENDER,
                EMPLOYEE.BIRTH_DATE,
                EMPLOYEE.ROLE_TYPE)
            .values(employeeId, fullName, companyEmail, phone, gender, birthDate, role)
            .execute();
    return query;
  }

  private InsertResultStep<WorkingContractRecord> insertWorkingContractRecord(
      Configuration config,
      String employeeId,
      BigDecimal baseSalary,
      String companyName,
      LocalDate startDate,
      String contractStatus,
      long contractType) {

    final var query =
        DSL.using(config)
            .insertInto(
                WORKING_CONTRACT,
                WORKING_CONTRACT.EMPLOYEE_ID,
                WORKING_CONTRACT.BASE_SALARY,
                WORKING_CONTRACT.COMPANY_NAME,
                WORKING_CONTRACT.START_DATE)
            .values(employeeId, baseSalary, companyName, startDate)
            .returning();
    return query;
  }

  private int insertWorkingPlace(
      Configuration config, long workingContractId, long area, long job, long office) {
    final var query =
        DSL.using(config)
            .insertInto(
                WORKING_PLACE,
                WORKING_PLACE.WORKING_CONTRACT_ID,
                WORKING_PLACE.AREA_ID,
                WORKING_PLACE.JOB_ID,
                WORKING_PLACE.OFFICE_ID)
            .values(workingContractId, area, job, office)
            .execute();
    return query;
  }
}
