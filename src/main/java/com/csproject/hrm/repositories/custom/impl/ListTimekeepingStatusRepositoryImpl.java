package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.ETimekeepingStatus;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.ListTimekeepingStatusRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.jooq.codegen.maven.example.tables.ListTimekeepingStatus.LIST_TIMEKEEPING_STATUS;

@AllArgsConstructor
public class ListTimekeepingStatusRepositoryImpl implements ListTimekeepingStatusRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertListTimekeepingStatus(Long timekeepingId, String timekeepingStatus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var checkExistTimekeepingStatusInList =
        dslContext.fetchExists(
            dslContext
                .select()
                .from(LIST_TIMEKEEPING_STATUS)
                .where(LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID.eq(timekeepingId))
                .and(
                    LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID.eq(
                        ETimekeepingStatus.getValue(timekeepingStatus))));
    if (!checkExistTimekeepingStatusInList) {
      final var insertListTimekeepingStatus =
          dslContext
              .insertInto(
                  LIST_TIMEKEEPING_STATUS,
                  LIST_TIMEKEEPING_STATUS.TIMEKEEPING_ID,
                  LIST_TIMEKEEPING_STATUS.TIMEKEEPING_STATUS_ID)
              .values(timekeepingId, ETimekeepingStatus.getValue(timekeepingStatus))
              .execute();
    }
  }
}
