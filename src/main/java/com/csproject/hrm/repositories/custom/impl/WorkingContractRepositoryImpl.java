package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.repositories.custom.WorkingContractRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.year;

@AllArgsConstructor
public class WorkingContractRepositoryImpl implements WorkingContractRepositoryCustom {

  @Autowired private final DBConnection connection;

  @Override
  public BigDecimal getBaseSalaryByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(SALARY_CONTRACT.BASE_SALARY)
        .from(WORKING_CONTRACT)
        .leftJoin(SALARY_CONTRACT)
        .on(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(SALARY_CONTRACT.WORKING_CONTRACT_ID))
        .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeID))
        .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
        .fetchOneInto(BigDecimal.class);
  }

  @Override
  public void updateStatusWorkingContract(Boolean status, LocalDate dateCheck) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    List<Long> workingContractIdList =
        dslContext
            .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(WORKING_CONTRACT)
            .where(month(WORKING_CONTRACT.START_DATE).eq(dateCheck.getMonth().getValue()))
            .and(year(WORKING_CONTRACT.START_DATE).eq(dateCheck.getYear()))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isFalse())
            .fetchInto(Long.class);

    dslContext.transaction(
        configuration -> {
          workingContractIdList.forEach(
              newWorkingContract -> {
                String employeeId =
                    dslContext
                        .select(EMPLOYEE.EMPLOYEE_ID)
                        .from(EMPLOYEE)
                        .leftJoin(WORKING_CONTRACT)
                        .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
                        .where(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(newWorkingContract))
                        .fetchOneInto(String.class);

                Long oldWorkingContractId =
                    dslContext
                        .select(WORKING_CONTRACT.WORKING_CONTRACT_ID)
                        .from(WORKING_CONTRACT)
                        .where(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
                        .and(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeId))
                        .fetchOneInto(Long.class);

                final var updateOldWorkingContract =
                    dslContext
                        .update(WORKING_CONTRACT)
                        .set(WORKING_CONTRACT.CONTRACT_STATUS, false)
                        .where(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(oldWorkingContractId))
                        .execute();

                final var updateNewWorkingContract =
                    dslContext
                        .update(WORKING_CONTRACT)
                        .set(WORKING_CONTRACT.CONTRACT_STATUS, status)
                        .where(WORKING_CONTRACT.WORKING_CONTRACT_ID.eq(newWorkingContract))
                        .execute();

                final var updateOldWorkingPlace =
                    dslContext
                        .update(WORKING_PLACE)
                        .set(WORKING_PLACE.WORKING_PLACE_STATUS, false)
                        .where(WORKING_PLACE.WORKING_CONTRACT_ID.eq(oldWorkingContractId))
                        .execute();

                final var updateNewWorkingPlace =
                    dslContext
                        .update(WORKING_PLACE)
                        .set(WORKING_PLACE.WORKING_PLACE_STATUS, status)
                        .where(WORKING_PLACE.WORKING_CONTRACT_ID.eq(newWorkingContract))
                        .execute();

                final var updateOldSalaryContract =
                    dslContext
                        .update(SALARY_CONTRACT)
                        .set(SALARY_CONTRACT.SALARY_CONTRACT_STATUS, false)
                        .where(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(oldWorkingContractId))
                        .execute();

                final var updateNewSalaryContract =
                    dslContext
                        .update(SALARY_CONTRACT)
                        .set(SALARY_CONTRACT.SALARY_CONTRACT_STATUS, status)
                        .where(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(newWorkingContract))
                        .execute();
              });
        });
  }
}