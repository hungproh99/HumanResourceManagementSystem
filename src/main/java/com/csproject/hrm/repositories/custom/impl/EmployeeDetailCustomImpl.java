package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.request.EmployeeDetailRequest;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.*;
import com.csproject.hrm.jooq.*;
import com.csproject.hrm.repositories.custom.EmployeeDetailCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.ContractType.CONTRACT_TYPE;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Job.JOB;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
import static org.jooq.codegen.maven.example.tables.Tax.TAX;
import static org.jooq.codegen.maven.example.tables.WorkingContract.WORKING_CONTRACT;
import static org.jooq.impl.DSL.*;

@AllArgsConstructor
public class EmployeeDetailCustomImpl implements EmployeeDetailCustom {
  private static final Map<String, Field<?>> field2Map;

  static {
    field2Map = new HashMap<>();
    field2Map.put(EMPLOYEE_ID, EMPLOYEE.EMPLOYEE_ID);
  }

  @Autowired private final JooqHelper queryHelper;
  @Autowired private final DBConnection connection;

  @Override
  public void updateMainDetail(EmployeeDetailRequest detailRequest) {
    //    final DSLContext dslContext = DSL.using(connection.getConnection());
    //    dslContext.transaction(
    //        configuration -> {
    //          DSLContext ctx = DSL.using(configuration);
    //          int employeeRecord =
    //              ctx.insertInto(
    //                      EMPLOYEE,
    //                      EMPLOYEE.EMPLOYEE_ID,
    //                      EMPLOYEE.FULL_NAME,
    //                      EMPLOYEE.WORK_STATUS,
    //                      EMPLOYEE.PHONE_NUMBER,
    //                      EMPLOYEE.BIRTH_DATE,
    //                      EMPLOYEE.COMPANY_EMAIL,
    //                      EMPLOYEE.GENDER,
    //                      EMPLOYEE.MARITAL_STATUS)
    //                  .values(
    //                      detailRequest.getEmployee_id(),
    //                      detailRequest.getFull_name(),
    //                      detailRequest.getWork_status(),
    //                      detailRequest.getPhone(),
    //                      detailRequest.getBirth_date(),
    //                      detailRequest.getCompanyEmail(),
    //                      detailRequest.getGender(),
    //                      detailRequest.getMaritalStatus())
    //                  .onDuplicateKeyUpdate()
    //                  .set(EMPLOYEE.FULL_NAME, detailRequest.getFull_name())
    //                  .set(EMPLOYEE.WORK_STATUS, detailRequest.getWork_status())
    //                  .set(EMPLOYEE.PHONE_NUMBER, detailRequest.getPhone())
    //                  .set(EMPLOYEE.BIRTH_DATE, detailRequest.getBirth_date())
    //                  .set(EMPLOYEE.COMPANY_EMAIL, detailRequest.getCompanyEmail())
    //                  .set(EMPLOYEE.GENDER, detailRequest.getGender())
    //                  .set(EMPLOYEE.MARITAL_STATUS, detailRequest.getMaritalStatus())
    //                  .execute();
    //
    //          WorkingContractRecord workingContractRecord =
    //              ctx.insertInto(
    //                      WORKING_CONTRACT,
    //                      WORKING_CONTRACT.EMPLOYEE_ID,
    //                      WORKING_CONTRACT.BASE_SALARY,
    //                      WORKING_CONTRACT.COMPANY_NAME,
    //                      WORKING_CONTRACT.START_DATE,
    //                      WORKING_CONTRACT.STATUS,
    //                      WORKING_CONTRACT.TYPE_ID)
    //                  .values(
    //                      employeeId,
    //                      hrmRequest.getBaseSalary(),
    //                      companyName,
    //                      startDate,
    //                      contractStatus,
    //                      EContractType.of(hrmRequest.getContractName()))
    //                  .onDuplicateKeyUpdate()
    //                  .returning()
    //                  .fetchOne();
    //
    //          ctx.insertInto(
    //                  WORKING_PLACE,
    //                  WORKING_PLACE.WORKING_CONTRACT_ID,
    //                  WORKING_PLACE.AREA_ID,
    //                  WORKING_PLACE.JOB_ID,
    //                  WORKING_PLACE.OFFICE_ID)
    //              .values(
    //                  workingContractRecord.getWorkingContractId(),
    //                  EArea.of(hrmRequest.getArea()),
    //                  EJob.of(hrmRequest.getJob()),
    //                  EOffice.of(hrmRequest.getOffice()))
    //              .execute();
    //        });
  }

  @Override
  public List<RelativeInformation> findRelativeByEmployeeID(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    return findRelativeByEmployeeID(conditions);
  }

  public List<RelativeInformation> findRelativeByEmployeeID(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(RELATIVE_INFORMATION.PARENT_NAME, RELATIVE_INFORMATION.CONTACT)
            .from(RELATIVE_INFORMATION)
            .rightJoin(EMPLOYEE)
            .on(RELATIVE_INFORMATION.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(conditions);
    return query.fetchInto(RelativeInformation.class);
  }

  @Override
  public List<WorkingHistory> findWorkingHistoryByEmployeeID(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    return findWorkingHistoryByEmployeeID(conditions);
  }

  public List<WorkingHistory> findWorkingHistoryByEmployeeID(List<Condition> conditions) {
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
            .where(conditions);
    return query.fetchInto(WorkingHistory.class);
  }

  @Override
  public List<Education> findEducationByEmployeeID(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    return findEducationByEmployeeID(conditions);
  }

  public List<Education> findEducationByEmployeeID(List<Condition> conditions) {
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
            .where(conditions);
    return query.fetchInto(Education.class);
  }

  @Override
  public List<Bank> findBankByEmployeeID(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    return findBankByEmployeeID(conditions);
  }

  public List<Bank> findBankByEmployeeID(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                BANK.BANK_ID, BANK.NAME_BANK, BANK.ADDRESS, BANK.ACCOUNT_NUMBER, BANK.ACCOUNT_NAME)
            .from(BANK)
            .rightJoin(EMPLOYEE)
            .on(BANK.BANK_ID.eq(EMPLOYEE.BANK_ID))
            .where(conditions);
    return query.fetchInto(Bank.class);
  }

  @Override
  public List<EmployeeAdditionalInfo> findAdditionalInfo(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    return findAdditionalInfo(conditions);
  }

  public List<EmployeeAdditionalInfo> findAdditionalInfo(List<Condition> conditions) {
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
            .where(conditions);
    return query.fetchInto(EmployeeAdditionalInfo.class);
  }

  @Override
  public List<TaxAndInsuranceResponse> findTaxAndInsurance(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    return findTaxAndInsurance(conditions);
  }

  public List<TaxAndInsuranceResponse> findTaxAndInsurance(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(TAX.TAX_ID, INSURANCE.INSURANCE_ID, INSURANCE.INSURANCE_NAME, INSURANCE.ADDRESS)
            .from(EMPLOYEE)
            .leftJoin(TAX)
            .on(TAX.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(INSURANCE)
            .on(INSURANCE.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(conditions);
    return query.fetchInto(TaxAndInsuranceResponse.class);
  }

  @Override
  public List<EmployeeDetailResponse> findMainDetail(QueryParam queryParam) {
    List<Condition> conditions = queryHelper.queryFilters(queryParam, field2Map);
    return findMainDetail(conditions);
  }

  public List<EmployeeDetailResponse> findMainDetail(List<Condition> conditions) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EMPLOYEE.EMPLOYEE_ID,
                EMPLOYEE.FULL_NAME,
                EMPLOYEE.COMPANY_EMAIL.as(EMAIL),
                EMPLOYEE.WORKING_STATUS,
                EMPLOYEE.PHONE_NUMBER.as(PHONE),
                EMPLOYEE.MARITAL_STATUS,
                EMPLOYEE.AVATAR,
                EMPLOYEE.GENDER,
                EMPLOYEE.BIRTH_DATE,
                JOB.TITLE,
                OFFICE.NAME.as(OFFICE_NAME),
                AREA.NAME.as(AREA_NAME),
                CONTRACT_TYPE.NAME,
                year(currentDate())
                    .minus(year(WORKING_CONTRACT.START_DATE))
                    .concat(YEAR)
                    .concat(month(currentDate()).minus(month(WORKING_CONTRACT.START_DATE)))
                    .concat(MONTH)
                    .concat(day(currentDate()).minus(day(WORKING_CONTRACT.START_DATE)))
                    .concat(DAY)
                    .as(SENIORITY),
                WORKING_CONTRACT.START_DATE)
            .from(EMPLOYEE)
            .leftJoin(WORKING_CONTRACT)
            .on(WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(CONTRACT_TYPE)
            .on(CONTRACT_TYPE.TYPE_ID.eq(WORKING_CONTRACT.TYPE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .leftJoin(OFFICE)
            .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
            .leftJoin(JOB)
            .on(JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
            .where(conditions);

    return query.fetchInto(EmployeeDetailResponse.class);
  }
}