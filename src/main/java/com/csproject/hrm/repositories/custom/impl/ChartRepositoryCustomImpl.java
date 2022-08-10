package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.chart.EmployeeChart;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.response.SalaryMonthlyResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.repositories.custom.ChartRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.SalaryContract.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.currentLocalDate;
import static org.jooq.impl.DSL.dateDiff;

@AllArgsConstructor
public class ChartRepositoryCustomImpl implements ChartRepositoryCustom {

  @Autowired private final DBConnection connection;

  @Override
  public String getAreaNameByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(AREA.NAME)
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID))
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .fetchOneInto(String.class);
  }

  @Override
  public int countTotalEmployeeByAreaName(String areaName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext.fetchCount(
        dslContext
            .select(EMPLOYEE.EMPLOYEE_ID)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_PLACE.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(WORKING_PLACE.AREA_ID.eq(AREA.AREA_ID))
            .where(AREA.NAME.eq(areaName))
            .and(EMPLOYEE.WORKING_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue()));
  }

  @Override
  public int countTotalEmployeeByGenderAndAreaName(String areaName, String gender) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select(EMPLOYEE.EMPLOYEE_ID)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_PLACE.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(WORKING_PLACE.AREA_ID.eq(AREA.AREA_ID))
            .where(AREA.NAME.eq(areaName))
            .and(EMPLOYEE.WORKING_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
            .and(EMPLOYEE.GENDER.eq(gender)));
  }

  @Override
  public List<Integer> getAllEmployeeSeniorityForChartByAreaName(String areaName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            dateDiff(
                currentLocalDate().cast(SQLDataType.DATE),
                WORKING_CONTRACT.START_DATE.cast(SQLDataType.DATE)))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .where(AREA.NAME.eq(areaName))
        .and(EMPLOYEE.WORKING_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .fetchInto(Integer.class);
  }

  @Override
  public List<Integer> getAllEmployeeAgeForChartByAreaName(String areaName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            dateDiff(
                currentLocalDate().cast(SQLDataType.DATE),
                EMPLOYEE.BIRTH_DATE.cast(SQLDataType.DATE)))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .where(AREA.NAME.eq(areaName))
        .and(EMPLOYEE.WORKING_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .fetchInto(Integer.class);
  }

  @Override
  public List<WorkingTypeDto> getAllWorkingType() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(WORKING_TYPE.TYPE_ID, WORKING_TYPE.NAME)
        .from(WORKING_TYPE)
        .fetchInto(WorkingTypeDto.class);
  }

  @Override
  public int countTotalEmployeeByContractTypeAndAreaName(String areaName, Long contractTypeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext.fetchCount(
        dslContext
            .select(EMPLOYEE.EMPLOYEE_ID)
            .from(EMPLOYEE)
            .leftJoin(WORKING_TYPE)
            .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
            .leftJoin(WORKING_CONTRACT)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .where(AREA.NAME.eq(areaName))
            .and(WORKING_TYPE.TYPE_ID.eq(contractTypeID))
            .and(EMPLOYEE.WORKING_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue()));
  }

  @Override
  public EmployeeChart getManagerByManagerID(String managerID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE.FULL_NAME.as("name"), EMPLOYEE.AVATAR, EMPLOYEE.EMPLOYEE_ID.as("employeeID"))
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(managerID))
        .and(EMPLOYEE.WORKING_STATUS.isTrue())
        .fetchOneInto(EmployeeChart.class);
  }

  @Override
  public List<EmployeeChart> getEmployeeByManagerIDAndAreaID(String managerID, long areaID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    System.out.println(
        dslContext
            .select(
                EMPLOYEE.FULL_NAME.as("name"),
                EMPLOYEE.AVATAR,
                EMPLOYEE.EMPLOYEE_ID.as("employeeID"))
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .where(EMPLOYEE.MANAGER_ID.eq(managerID))
            .and(EMPLOYEE.WORKING_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
            .and(AREA.AREA_ID.eq(areaID)));
    return dslContext
        .select(
            EMPLOYEE.FULL_NAME.as("name"), EMPLOYEE.AVATAR, EMPLOYEE.EMPLOYEE_ID.as("employeeID"))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .where(EMPLOYEE.MANAGER_ID.eq(managerID))
        .and(EMPLOYEE.WORKING_STATUS.isTrue())
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
        .and(AREA.AREA_ID.eq(areaID))
        .fetchInto(EmployeeChart.class);
  }

  @Override
  public List<LeaveCompanyReasonDto> getAllLeaveCompanyReason() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(LEAVE_COMPANY_REASON.REASON_ID, LEAVE_COMPANY_REASON.REASON_NAME)
        .from(LEAVE_COMPANY_REASON)
        .fetchInto(LeaveCompanyReasonDto.class);
  }

  @Override
  public int countLeaveCompanyReasonByDateAndReasonID(
      LocalDate startDate, LocalDate endDate, Long reasonID, String areaName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(LEAVE_COMPANY_REASON)
            .leftJoin(LEAVE_COMPANY)
            .on(LEAVE_COMPANY.LEAVE_COMPANY_REASON_ID.eq(LEAVE_COMPANY_REASON.REASON_ID))
            .leftJoin(WORKING_CONTRACT)
            .on(LEAVE_COMPANY.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .where(LEAVE_COMPANY_REASON.REASON_ID.eq(reasonID))
            .and(WORKING_CONTRACT.END_DATE.between(startDate, endDate))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isFalse())
            .and(AREA.NAME.eq(areaName)));
  }

  @Override
  public List<PaidLeaveReasonDto> getAllPaidLeaveReason() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(PAID_LEAVE_REASON.REASON_ID, PAID_LEAVE_REASON.REASON_NAME)
        .from(PAID_LEAVE_REASON)
        .fetchInto(PaidLeaveReasonDto.class);
  }

  @Override
  public int countPaidLeaveReasonByDateAndReasonID(
      LocalDate startDate, LocalDate endDate, Long reasonID, String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select(PAID_LEAVE_REASON.REASON_ID)
            .from(PAID_LEAVE_REASON)
            .leftJoin(PAID_LEAVE)
            .on(PAID_LEAVE.PAID_LEAVE_REASON_ID.eq(PAID_LEAVE_REASON.REASON_ID))
            .leftJoin(TIMEKEEPING)
            .on(PAID_LEAVE.TIMEKEEPING_ID.eq(TIMEKEEPING.TIMEKEEPING_ID))
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(TIMEKEEPING.EMPLOYEE_ID))
            .where(PAID_LEAVE_REASON.REASON_ID.eq(reasonID))
            .and(TIMEKEEPING.DATE.between(startDate, endDate))
            .and(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeID)));
  }

  @Override
  public List<SalaryMonthlyResponse> getSalaryHistoryByDateAndEmployeeIDAndType(
      LocalDate startDate, LocalDate endDate, String employeeID, String type) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    var query =
        dslContext
            .select(
                SALARY_MONTHLY.FINAL_SALARY.as("finalSalary"),
                SALARY_MONTHLY.START_DATE.as("startDate"))
            .from(SALARY_MONTHLY)
            .leftJoin(SALARY_CONTRACT)
            .on(SALARY_CONTRACT.SALARY_CONTRACT_ID.eq(SALARY_MONTHLY.SALARY_CONTRACT_ID))
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(SALARY_STATUS)
            .on(SALARY_STATUS.STATUS_ID.eq(SALARY_MONTHLY.SALARY_STATUS_ID))
            .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeID))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .and(SALARY_MONTHLY.SALARY_STATUS_ID.eq(2L))
            .and(SALARY_MONTHLY.START_DATE.between(startDate, endDate))
            .orderBy(SALARY_MONTHLY.START_DATE);
    return query.fetchInto(SalaryMonthlyResponse.class);
  }

  @Override
  public LocalDate getStartDateOfContract(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(WORKING_CONTRACT.START_DATE)
        .from(WORKING_CONTRACT)
        .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeID))
        .fetchOneInto(LocalDate.class);
  }
}