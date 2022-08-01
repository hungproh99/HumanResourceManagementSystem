package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.CheckInCheckOutDto;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.CheckInCheckOutRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.jooq.codegen.maven.example.Tables.CHECKIN_CHECKOUT;
import static org.jooq.codegen.maven.example.tables.Timekeeping.TIMEKEEPING;

@AllArgsConstructor
public class CheckInCheckOutRepositoryImpl implements CheckInCheckOutRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertCheckInByTimekeepingId(Long timekeepingId, LocalTime localTime) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .insertInto(CHECKIN_CHECKOUT, CHECKIN_CHECKOUT.CHECKIN, CHECKIN_CHECKOUT.TIMEKEEPING_ID)
            .values(localTime, timekeepingId)
            .execute();
  }

  @Override
  public void updateCheckOutByTimekeepingId(Long checkInCheckOutId, LocalTime localTime) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(CHECKIN_CHECKOUT)
            .set(CHECKIN_CHECKOUT.CHECKOUT, localTime)
            .where(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.eq(checkInCheckOutId))
            .execute();
  }

  @Override
  public Optional<CheckInCheckOutDto> getLastCheckInCheckOutByTimekeeping(Long timekeepingId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.as("checkin_checkout_id"),
            CHECKIN_CHECKOUT.CHECKIN.as("checkin"),
            CHECKIN_CHECKOUT.CHECKOUT.as("checkout"))
        .from(CHECKIN_CHECKOUT)
        .leftJoin(TIMEKEEPING)
        .on(TIMEKEEPING.TIMEKEEPING_ID.eq(CHECKIN_CHECKOUT.TIMEKEEPING_ID))
        .where(TIMEKEEPING.TIMEKEEPING_ID.eq(timekeepingId))
        .orderBy(CHECKIN_CHECKOUT.CHECKIN_CHECKOUT_ID.desc())
        .limit(1)
        .fetchOptionalInto(CheckInCheckOutDto.class);
  }
}
