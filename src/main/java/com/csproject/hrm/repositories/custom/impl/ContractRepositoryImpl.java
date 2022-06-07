package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.AreaDto;
import com.csproject.hrm.dto.dto.JobDto;
import com.csproject.hrm.dto.dto.OfficeDto;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.ContractRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.codegen.maven.example.Tables.*;

@AllArgsConstructor
public class ContractRepositoryImpl implements ContractRepositoryCustom {
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
        .fetchInto(OfficeDto.class);
  }

  @Override
  public List<AreaDto> getListArea() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.select(AREA.AREA_ID, AREA.NAME).from(AREA).fetchInto(AreaDto.class);
  }

  @Override
  public List<JobDto> getListJob() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(JOB.JOB_ID, JOB.DESCRIPTION, JOB.GRADE, JOB.POSITION)
        .from(JOB)
        .fetchInto(JobDto.class);
  }
}
