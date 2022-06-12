package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.*;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.jooq.JooqHelper;
import com.csproject.hrm.repositories.custom.EmployeeDetailRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import com.csproject.hrm.common.constant.Constants;
import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Grade.GRADE;
import static org.jooq.codegen.maven.example.tables.Job.JOB;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
import static org.jooq.codegen.maven.example.tables.Tax.TAX;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.*;

@AllArgsConstructor
public class EmployeeDetailRepositoryImpl implements EmployeeDetailRepositoryCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void updateEmployee(Employee employee) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          DSLContext ctx = DSL.using(configuration);
          //          queries.add();
          ctx.insertInto(
                  EMPLOYEE,
                  EMPLOYEE.EMPLOYEE_ID,
                  EMPLOYEE.FULL_NAME,
                  EMPLOYEE.WORKING_STATUS,
                  EMPLOYEE.PHONE_NUMBER,
                  EMPLOYEE.BIRTH_DATE,
                  EMPLOYEE.COMPANY_EMAIL,
                  EMPLOYEE.GENDER,
                  EMPLOYEE.MARITAL_STATUS)
              .values(
                  employee.getId(),
                  employee.getFullName(),
                  employee.getWorkingStatus(),
                  employee.getPhoneNumber(),
                  employee.getBirthDate(),
                  employee.getCompanyEmail(),
                  employee.getGender(),
                  employee.getMaritalStatus())
              .onDuplicateKeyUpdate()
              .set(EMPLOYEE.FULL_NAME, employee.getFullName())
              .set(EMPLOYEE.WORKING_STATUS, employee.getWorkingStatus())
              .set(EMPLOYEE.PHONE_NUMBER, employee.getPhoneNumber())
              .set(EMPLOYEE.BIRTH_DATE, employee.getBirthDate())
              .set(EMPLOYEE.COMPANY_EMAIL, employee.getCompanyEmail())
              .set(EMPLOYEE.GENDER, employee.getGender())
              .set(EMPLOYEE.MARITAL_STATUS, employee.getMaritalStatus())
              .execute();

          //          ctx.insertInto(
          //                  WORKING_CONTRACT,
          //                  WORKING_CONTRACT.EMPLOYEE_ID,
          //                  WORKING_CONTRACT.START_DATE,
          //                  WORKING_CONTRACT.CONTRACT_URL)
          //              .values(
          //                  employee.getEmployee_id(),
          //                  employee.getStart_date(),
          //                  employee.getContract_url())
          //              .onDuplicateKeyUpdate()
          //              .set(WORKING_CONTRACT.EMPLOYEE_ID, employee.getEmployee_id())
          //              .set(WORKING_CONTRACT.START_DATE, employee.getStart_date())
          //              .set(WORKING_CONTRACT.CONTRACT_URL, employee.getContract_url())
          //              .execute();
        });
  }

  public Update<?> updateEmployeeRecord() {
    return null;
  }

  @Override
  public List<RelativeInformation> findRelativeByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(RELATIVE_INFORMATION.PARENT_NAME, RELATIVE_INFORMATION.CONTACT)
            .from(RELATIVE_INFORMATION)
            .rightJoin(EMPLOYEE)
            .on(RELATIVE_INFORMATION.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(RelativeInformation.class);
  }

  @Override
  public List<WorkingHistory> findWorkingHistoryByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                WORKING_HISTORY.COMPANY_NAME,
                WORKING_HISTORY.POSITION,
                WORKING_HISTORY.START_DATE,
                WORKING_HISTORY.END_DATE)
            .from(WORKING_HISTORY)
            .rightJoin(EMPLOYEE)
            .on(WORKING_HISTORY.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(WorkingHistory.class);
  }

  @Override
  public List<Education> findEducationByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EDUCATION.EDUCATION_ID,
                EDUCATION.NAME_SCHOOL,
                EDUCATION.START_DATE,
                EDUCATION.END_DATE,
                EDUCATION.CERTIFICATE,
                EDUCATION.STATUS)
            .from(EDUCATION)
            .rightJoin(EMPLOYEE)
            .on(EDUCATION.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(Education.class);
  }

  @Override
  public List<Bank> findBankByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                BANK.BANK_ID, BANK.NAME_BANK, BANK.ADDRESS, BANK.ACCOUNT_NUMBER, BANK.ACCOUNT_NAME)
            .from(BANK)
            .rightJoin(EMPLOYEE)
            .on(BANK.BANK_ID.eq(EMPLOYEE.BANK_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(Bank.class);
  }

  @Override
  public List<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EMPLOYEE.ADDRESS,
                IDENTITY_CARD.PLACE_OF_RESIDENCE,
                IDENTITY_CARD.PLACE_OF_ORIGIN,
                IDENTITY_CARD.NATIONALITY,
                IDENTITY_CARD.CARD_ID,
                IDENTITY_CARD.PROVIDE_DATE,
                IDENTITY_CARD.PROVIDE_PLACE,
                EMPLOYEE.PHONE_NUMBER,
                EMPLOYEE.NICK_NAME,
                EMPLOYEE.FACEBOOK)
            .from(EMPLOYEE)
            .leftJoin(IDENTITY_CARD)
            .on(IDENTITY_CARD.CARD_ID.eq(EMPLOYEE.CARD_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(EmployeeAdditionalInfo.class);
  }

  @Override
  public List<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EMPLOYEE.TAX_CODE,
                INSURANCE.INSURANCE_ID,
                INSURANCE.INSURANCE_NAME,
                INSURANCE.ADDRESS)
            .from(EMPLOYEE)
            .leftJoin(TAX)
            .on(TAX.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(INSURANCE)
            .on(INSURANCE.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(TaxAndInsuranceResponse.class);
  }

  @Override
  public List<EmployeeDetailResponse> findMainDetail(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EMPLOYEE.EMPLOYEE_ID,
                EMPLOYEE.FULL_NAME,
                EMPLOYEE.COMPANY_EMAIL,
                (when(EMPLOYEE.WORKING_STATUS.eq(Boolean.TRUE), "Active")
                        .when(EMPLOYEE.WORKING_STATUS.eq(Boolean.FALSE), "Deactivate"))
                    .as(WORKING_STATUS),
                EMPLOYEE.PHONE_NUMBER,
                EMPLOYEE.MARITAL_STATUS,
                EMPLOYEE.AVATAR,
                EMPLOYEE.GENDER,
                EMPLOYEE.BIRTH_DATE,
                GRADE.NAME.as(Constants.GRADE),
                OFFICE.NAME.as(OFFICE_NAME),
                AREA.NAME.as(AREA_NAME),
                year(currentDate())
                    .minus(year(WORKING_CONTRACT.START_DATE))
                    .concat(YEAR)
                    .concat(month(currentDate()).minus(month(WORKING_CONTRACT.START_DATE)))
                    .concat(MONTH)
                    .concat(day(currentDate()).minus(day(WORKING_CONTRACT.START_DATE)))
                    .concat(DAY)
                    .as(SENIORITY),
                WORKING_CONTRACT.START_DATE,
                WORKING_CONTRACT.CONTRACT_URL,
                JOB.POSITION.as(POSITION_NAME),
                WORKING_TYPE.NAME.as(WORKING_NAME))
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_CONTRACT.AREA_ID))
            .leftJoin(OFFICE)
            .on(OFFICE.OFFICE_ID.eq(WORKING_CONTRACT.OFFICE_ID))
            .leftJoin(JOB)
            .on(JOB.JOB_ID.eq(WORKING_CONTRACT.JOB_ID))
            .leftJoin(GRADE)
            .on(GRADE.GRADE_ID.eq(WORKING_CONTRACT.GRADE_ID))
            .leftJoin(WORKING_TYPE)
            .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));

    return query.fetchInto(EmployeeDetailResponse.class);
  }
}