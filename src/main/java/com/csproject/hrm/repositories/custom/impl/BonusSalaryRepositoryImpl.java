package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.common.enums.EBonus;
import com.csproject.hrm.dto.request.BonusSalaryRequest;
import com.csproject.hrm.dto.dto.BonusTypeDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyInfoDto;
import com.csproject.hrm.dto.response.BonusSalaryResponse;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.BonusSalaryRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.jooq.codegen.maven.example.tables.BonusSalary.BONUS_SALARY;
import static org.jooq.codegen.maven.example.tables.BonusType.BONUS_TYPE;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.SalaryContract.SALARY_CONTRACT;
import static org.jooq.codegen.maven.example.tables.SalaryMonthly.SALARY_MONTHLY;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.sum;

@AllArgsConstructor
public class BonusSalaryRepositoryImpl implements BonusSalaryRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void insertBonusSalaryByEmployeeId(
      Long salaryId, LocalDate date, String description, Long bonusType, BigDecimal bonus) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .insertInto(
                BONUS_SALARY,
                BONUS_SALARY.DATE,
                BONUS_SALARY.DESCRIPTION,
                BONUS_SALARY.VALUE,
                BONUS_SALARY.SALARY_ID,
                BONUS_SALARY.BONUS_TYPE_ID)
            .values(date, description, bonus, salaryId, bonusType)
            .execute();
  }

  @Override
  public List<BonusSalaryResponse> getListBonusMonthlyBySalaryMonthlyId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<BonusSalaryResponse> bonusSalaryResponses =
        dslContext
            .select(
                BONUS_SALARY.BONUS_ID,
                BONUS_SALARY.VALUE,
                BONUS_TYPE.BONUS_TYPE_.as("bonus_name"),
                BONUS_SALARY.DATE,
                BONUS_SALARY.DESCRIPTION)
            .from(BONUS_SALARY)
            .leftJoin(BONUS_TYPE)
            .on(BONUS_TYPE.BONUS_TYPE_ID.eq(BONUS_SALARY.BONUS_TYPE_ID))
            .leftJoin(SALARY_MONTHLY)
            .on(SALARY_MONTHLY.SALARY_ID.eq(BONUS_SALARY.SALARY_ID))
            .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
            .fetchInto(BonusSalaryResponse.class);
    return bonusSalaryResponses;
  }

  @Override
  public BigDecimal sumListBonusMonthlyBySalaryMonthlyId(Long salaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(sum(BONUS_SALARY.VALUE))
        .from(BONUS_SALARY)
        .leftJoin(BONUS_TYPE)
        .on(BONUS_TYPE.BONUS_TYPE_ID.eq(BONUS_SALARY.BONUS_TYPE_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_ID.eq(BONUS_SALARY.SALARY_ID))
        .where(SALARY_MONTHLY.SALARY_ID.eq(salaryId))
        .fetchOneInto(BigDecimal.class);
  }

  @Override
  public void updateBonusSalaryByBonusSalaryId(BonusSalaryRequest bonusSalaryRequest) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .update(BONUS_SALARY)
            .set(BONUS_SALARY.VALUE, bonusSalaryRequest.getValue())
            .set(BONUS_SALARY.DESCRIPTION, bonusSalaryRequest.getDescription())
            .set(BONUS_SALARY.DATE, bonusSalaryRequest.getDate())
            .set(BONUS_SALARY.BONUS_TYPE_ID, bonusSalaryRequest.getBonusTypeId())
            .where(BONUS_SALARY.BONUS_ID.eq(bonusSalaryRequest.getBonusSalaryId()))
            .execute();
  }

  @Override
  public void deleteBonusSalaryByBonusSalaryId(Long bonusSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext.delete(BONUS_SALARY).where(BONUS_SALARY.BONUS_ID.eq(bonusSalaryId)).execute();
  }

  @Override
  public Optional<SalaryMonthlyInfoDto> getSalaryMonthlyInfoByBonusSalary(Long bonusSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(
            EMPLOYEE.EMPLOYEE_ID.as("employeeId"),
            SALARY_MONTHLY.START_DATE.as("startDate"),
            SALARY_MONTHLY.END_DATE.as("endDate"))
        .from(EMPLOYEE)
        .leftJoin(WORKING_CONTRACT)
        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
        .leftJoin(SALARY_CONTRACT)
        .on(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
        .leftJoin(SALARY_MONTHLY)
        .on(SALARY_MONTHLY.SALARY_CONTRACT_ID.eq(SALARY_CONTRACT.SALARY_CONTRACT_ID))
        .leftJoin(BONUS_SALARY)
        .on(BONUS_SALARY.SALARY_ID.eq(SALARY_MONTHLY.SALARY_ID))
        .where(BONUS_SALARY.BONUS_ID.eq(bonusSalaryId))
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
        .fetchOptionalInto(SalaryMonthlyInfoDto.class);
  }

  @Override
  public boolean checkExistBonusSalary(Long bonusSalaryId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext.select().from(BONUS_SALARY).where(BONUS_SALARY.BONUS_ID.eq(bonusSalaryId)));
  }

  @Override
  public List<BonusTypeDto> getListBonusTypeDto() {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<BonusTypeDto> bonusTypeDtos =
        dslContext
            .select(BONUS_TYPE.BONUS_TYPE_ID, BONUS_TYPE.BONUS_TYPE_.as("bonus_type_name"))
            .from(BONUS_TYPE)
            .fetchInto(BonusTypeDto.class);
    bonusTypeDtos.forEach(
        bonusTypeDto -> {
          bonusTypeDto.setBonus_type_name(EBonus.getLabel(bonusTypeDto.getBonus_type_name()));
        });
    return bonusTypeDtos;
  }
}
