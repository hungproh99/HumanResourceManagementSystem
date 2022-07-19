package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.chart.EmployeeChart;
import com.csproject.hrm.dto.dto.WorkingTypeDto;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.repositories.custom.ChartRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.jooq.codegen.maven.example.Tables.WORKING_PLACE;
import static org.jooq.codegen.maven.example.Tables.WORKING_TYPE;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
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
            .where(AREA.NAME.like("%" + areaName + "%"))
            .and(EMPLOYEE.WORKING_STATUS.eq(true)));
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
            .where(AREA.NAME.like("%" + areaName + "%"))
            .and(EMPLOYEE.WORKING_STATUS.eq(true))
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
        .where(AREA.NAME.like("%" + areaName + "%"))
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
        .where(AREA.NAME.like("%" + areaName + "%"))
        .fetchInto(Integer.class);
  }

  @Override
  public List<WorkingTypeDto> getAllWorkingType() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(WORKING_TYPE.TYPE_ID, WORKING_TYPE.NAME)
        .from(EMPLOYEE)
        .leftJoin(WORKING_TYPE)
        .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
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
            .where(AREA.NAME.like("%" + areaName + "%"))
            .and(WORKING_TYPE.TYPE_ID.eq(contractTypeID)));
  }

  @Override
  public List<EmployeeChart> getManagerByAreaName(String areaName) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            AREA.AREA_ID.as("title"),
            EMPLOYEE.FULL_NAME.as("name"),
            EMPLOYEE.AVATAR,
            EMPLOYEE.MANAGER_ID.as("managerID"))
        .from(AREA)
        .leftJoin(WORKING_PLACE)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
        .where(AREA.NAME.like("%" + areaName + "%"))
        .fetchInto(EmployeeChart.class);
  }

  @Override
  public List<EmployeeChart> getEmployeeByManagerID(String managerID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            AREA.AREA_ID.as("title"),
            EMPLOYEE.FULL_NAME.as("name"),
            EMPLOYEE.AVATAR,
            EMPLOYEE.MANAGER_ID.as("managerID"))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
        .leftJoin(WORKING_PLACE)
        .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(AREA)
        .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
        .where(EMPLOYEE.MANAGER_ID.eq(managerID))
        .fetchInto(EmployeeChart.class);
  }
}