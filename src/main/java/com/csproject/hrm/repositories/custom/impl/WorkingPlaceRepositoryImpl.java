package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.WorkingPlaceRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static org.jooq.codegen.maven.example.Tables.*;

@AllArgsConstructor
public class WorkingPlaceRepositoryImpl implements WorkingPlaceRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public List<OfficeDto> getListOffice() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(OFFICE.OFFICE_ID, OFFICE.NAME, OFFICE.ADDRESS)
        .from(OFFICE)
        .orderBy(OFFICE.OFFICE_ID.asc())
        .fetchInto(OfficeDto.class);
  }

  @Override
  public List<AreaDto> getListArea() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(AREA.AREA_ID, AREA.NAME)
        .from(AREA)
        .orderBy(AREA.AREA_ID.asc())
        .fetchInto(AreaDto.class);
  }

  @Override
  public List<JobDto> getListPosition() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(JOB.JOB_ID, JOB.POSITION, JOB.DESCRIPTION)
        .from(JOB)
        .orderBy(JOB.JOB_ID.asc())
        .fetchInto(JobDto.class);
  }

  @Override
  public List<GradeDto> getListGradeByPosition(Long jodId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(GRADE_TYPE.GRADE_ID, GRADE_TYPE.NAME, GRADE_TYPE.DESCRIPTION)
        .from(GRADE_TYPE)
        .where(GRADE_TYPE.JOB_ID.eq(jodId))
        .orderBy(GRADE_TYPE.GRADE_ID.asc())
        .fetchInto(GradeDto.class);
  }

  @Override
  public void insertNewWorkingPlace(
      String employeeId,
      Long area,
      Long office,
      Long grade,
      Long position,
      LocalDate startDate,
      boolean oldStatus,
      boolean newStatus) {

    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var contractId =
        dslContext
            .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .fetchOneInto(Long.class);

    final var updateStatusOldWorkingPlace =
        dslContext
            .update(WORKING_PLACE)
            .set(WORKING_PLACE.WORKING_PLACE_STATUS, oldStatus)
            .where(WORKING_PLACE.WORKING_CONTRACT_ID.eq(contractId))
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue())
            .execute();

    final var insertNewWorkingPlace =
        dslContext
            .insertInto(
                WORKING_PLACE,
                WORKING_PLACE.AREA_ID,
                WORKING_PLACE.GRADE_ID,
                WORKING_PLACE.JOB_ID,
                WORKING_PLACE.OFFICE_ID,
                WORKING_PLACE.WORKING_CONTRACT_ID,
                WORKING_PLACE.WORKING_PLACE_STATUS,
                WORKING_PLACE.START_DATE)
            .values(area, grade, position, office, contractId, newStatus, startDate)
            .execute();
  }
}