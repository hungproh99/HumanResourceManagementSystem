package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.ETimekeepingStatus;
import com.csproject.hrm.dto.dto.OvertimeDto;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.OvertimeRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.ListTimekeepingStatus.LIST_TIMEKEEPING_STATUS;
import static org.jooq.codegen.maven.example.tables.Overtime.OVERTIME;
import static org.jooq.codegen.maven.example.tables.OvertimeType.OVERTIME_TYPE;
import static org.jooq.codegen.maven.example.tables.Timekeeping.TIMEKEEPING;
import static org.jooq.codegen.maven.example.tables.TimekeepingStatus.TIMEKEEPING_STATUS;

@AllArgsConstructor
public class OvertimeRepositoryImpl implements OvertimeRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public Optional<OvertimeDto> getOvertimeByEmployeeIdAndDate(LocalDate date, String employeeId) {
    DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            OVERTIME.START_TIME,
            OVERTIME.END_TIME,
            OVERTIME_TYPE.OVERTIME_TYPE_.as("overtime_type"))
        .from(OVERTIME)
        .leftJoin(EMPLOYEE)
        .on(EMPLOYEE.EMPLOYEE_ID.eq(TIMEKEEPING.EMPLOYEE_ID))
        .leftJoin(LIST_TIMEKEEPING_STATUS)
        .on(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(TIMEKEEPING.TIMEKEEPING_ID))
        .leftJoin(TIMEKEEPING_STATUS)
        .on(TIMEKEEPING_STATUS.TYPE_ID.eq(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID))
        .leftJoin(TIMEKEEPING)
        .on(OVERTIME.TIMEKEEPING_ID.eq(TIMEKEEPING.TIMEKEEPING_ID))
        .leftJoin(OVERTIME_TYPE)
        .on(OVERTIME_TYPE.OVERTIME_TYPE_ID.eq(OVERTIME.OVERTIME_TYPE_ID))
        .where(TIMEKEEPING_STATUS.NAME.eq(ETimekeepingStatus.OVERTIME.name()))
        .and(TIMEKEEPING.DATE.eq(date))
        .and(EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
        .fetchOptionalInto(OvertimeDto.class);
  }
}
