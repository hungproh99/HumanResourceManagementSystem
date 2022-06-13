package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.entities.*;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.repositories.custom.EmployeeDetailRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
  @Autowired private final DBConnection connection;

  @Override
  public void updateRelativeInfo(RelativeInformationRequest relativeInformation) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .insertInto(
            RELATIVE_INFORMATION,
            RELATIVE_INFORMATION.RELATIVE_ID,
            RELATIVE_INFORMATION.EMPLOYEE_ID,
            RELATIVE_INFORMATION.BIRTH_DATE,
            RELATIVE_INFORMATION.PARENT_NAME,
            RELATIVE_INFORMATION.CONTACT,
            RELATIVE_INFORMATION.STATUS,
            RELATIVE_INFORMATION.RELATIVE_TYPE)
        .values(
            relativeInformation.getId(),
            relativeInformation.getEmployeeId(),
            relativeInformation.getBirthDate(),
            relativeInformation.getParentName(),
            relativeInformation.getContact(),
            relativeInformation.getStatus(),
            relativeInformation.getRelativeTypeId())
        .onDuplicateKeyUpdate()
        .set(RELATIVE_INFORMATION.EMPLOYEE_ID, relativeInformation.getEmployeeId())
        .set(RELATIVE_INFORMATION.BIRTH_DATE, relativeInformation.getBirthDate())
        .set(RELATIVE_INFORMATION.PARENT_NAME, relativeInformation.getParentName())
        .set(RELATIVE_INFORMATION.CONTACT, relativeInformation.getContact())
        .set(RELATIVE_INFORMATION.STATUS, relativeInformation.getStatus())
        .set(RELATIVE_INFORMATION.RELATIVE_TYPE, relativeInformation.getRelativeTypeId())
        .execute();
  }

  @Override
  public void updateWorkingHistory(WorkingHistoryRequest workingHistory) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .insertInto(
            WORKING_HISTORY,
            WORKING_HISTORY.WORKING_HISTORY_ID,
            WORKING_HISTORY.EMPLOYEE_ID,
            WORKING_HISTORY.END_DATE,
            WORKING_HISTORY.COMPANY_NAME,
            WORKING_HISTORY.POSITION,
            WORKING_HISTORY.START_DATE)
        .values(
            workingHistory.getId(),
            workingHistory.getEmployeeId(),
            workingHistory.getEndDate(),
            workingHistory.getCompanyName(),
            workingHistory.getPosition(),
            workingHistory.getStartDate())
        .onDuplicateKeyUpdate()
        .set(WORKING_HISTORY.EMPLOYEE_ID, workingHistory.getEmployeeId())
        .set(WORKING_HISTORY.COMPANY_NAME, workingHistory.getCompanyName())
        .set(WORKING_HISTORY.END_DATE, workingHistory.getEndDate())
        .set(WORKING_HISTORY.START_DATE, workingHistory.getStartDate())
        .set(WORKING_HISTORY.POSITION, workingHistory.getPosition())
        .execute();
  }

  @Override
  public void updateEducationInfo(EducationRequest education) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .insertInto(
            EDUCATION,
            EDUCATION.EDUCATION_ID,
            EDUCATION.EMPLOYEE_ID,
            EDUCATION.CERTIFICATE,
            EDUCATION.END_DATE,
            EDUCATION.START_DATE,
            EDUCATION.NAME_SCHOOL,
            EDUCATION.STATUS)
        .values(
            education.getId(),
            education.getEmployeeId(),
            education.getCertificate(),
            education.getEndDate(),
            education.getStartDate(),
            education.getNameSchool(),
            education.getStatus())
        .onDuplicateKeyUpdate()
        .set(EDUCATION.EMPLOYEE_ID, education.getEmployeeId())
        .set(EDUCATION.CERTIFICATE, education.getCertificate())
        .set(EDUCATION.END_DATE, education.getEndDate())
        .set(EDUCATION.START_DATE, education.getStartDate())
        .set(EDUCATION.NAME_SCHOOL, education.getNameSchool())
        .set(EDUCATION.STATUS, education.getStatus())
        .execute();
  }

  @Override
  public void updateBankInfo(BankRequest bank) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
      configuration -> {
        int bankID =
            dslContext
                .insertInto(
                    BANK,
                    BANK.BANK_ID,
                    BANK.NAME_BANK,
                    BANK.ADDRESS,
                    BANK.ACCOUNT_NUMBER,
                    BANK.ACCOUNT_NAME)
                .values(
                    bank.getId(),
                    bank.getNameBank(),
                    bank.getAddress(),
                    bank.getAccountNumber(),
                    bank.getAccountName())
                .onDuplicateKeyUpdate()
                .set(BANK.NAME_BANK, bank.getNameBank())
                .set(BANK.ADDRESS, bank.getAddress())
                .set(BANK.ACCOUNT_NUMBER, bank.getAccountNumber())
                .set(BANK.ACCOUNT_NAME, bank.getAccountName())
                .returningResult(BANK.BANK_ID)
                .execute();
    
        dslContext
            .update(EMPLOYEE)
            .set(EMPLOYEE.BANK_ID, Long.valueOf(bankID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(bank.getEmployeeId()))
            .execute();
      });
  }

  @Override
  public void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .update(EMPLOYEE)
        .set(EMPLOYEE.TAX_CODE, taxAndInsurance.getTaxCode())
        .where(EMPLOYEE.EMPLOYEE_ID.eq(taxAndInsurance.getEmployeeId()))
        .execute();

    dslContext
        .insertInto(
            INSURANCE,
            INSURANCE.INSURANCE_ID,
            INSURANCE.EMPLOYEE_ID,
            INSURANCE.ADDRESS,
            INSURANCE.INSURANCE_NAME,
            INSURANCE.PERCENT,
            INSURANCE.DESCRIPTION,
            INSURANCE.TITLE)
        .values(
            taxAndInsurance.getInsuranceId(),
            taxAndInsurance.getEmployeeId(),
            taxAndInsurance.getInsuranceAddress(),
            taxAndInsurance.getInsuranceName(),
            taxAndInsurance.getInsurancePercent(),
            taxAndInsurance.getInsuranceDescription(),
            taxAndInsurance.getInsuranceTitle())
        .onDuplicateKeyUpdate()
        .set(INSURANCE.EMPLOYEE_ID, taxAndInsurance.getEmployeeId())
        .set(INSURANCE.ADDRESS, taxAndInsurance.getInsuranceAddress())
        .set(INSURANCE.INSURANCE_NAME, taxAndInsurance.getInsuranceName())
        .set(INSURANCE.PERCENT, taxAndInsurance.getInsurancePercent())
        .set(INSURANCE.DESCRIPTION, taxAndInsurance.getInsurancePercent())
        .set(INSURANCE.TITLE, taxAndInsurance.getInsuranceTitle())
        .execute();
  }

  @Override
  public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          queries.add(updateEmployee(configuration, employeeDetailRequest));
          queries.add(updateWorkingContract(configuration, employeeDetailRequest));
          DSL.using(configuration).batch(queries).execute();
        });
  }

  public InsertOnDuplicateSetMoreStep<?> updateWorkingContract(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    return DSL.using(configuration)
        .insertInto(
            WORKING_CONTRACT,
            WORKING_CONTRACT.WORKING_CONTRACT_ID,
            WORKING_CONTRACT.COMPANY_NAME,
            WORKING_CONTRACT.CONTRACT_TYPE_ID,
            WORKING_CONTRACT.START_DATE,
            WORKING_CONTRACT.END_DATE,
            WORKING_CONTRACT.BASE_SALARY,
            WORKING_CONTRACT.CONTRACT_URL,
            WORKING_CONTRACT.CONTRACT_STATUS,
            WORKING_CONTRACT.AREA_ID,
            WORKING_CONTRACT.JOB_ID,
            WORKING_CONTRACT.OFFICE_ID,
            WORKING_CONTRACT.EMPLOYEE_ID)
        .values(
            employeeDetailRequest.getWorking_contract_id(),
            employeeDetailRequest.getCompany_name(),
            employeeDetailRequest.getContract_type_id(),
            employeeDetailRequest.getStart_date(),
            employeeDetailRequest.getEnd_date(),
            employeeDetailRequest.getBase_salary(),
            employeeDetailRequest.getContract_url(),
            employeeDetailRequest.getContract_status(),
            employeeDetailRequest.getArea_id(),
            employeeDetailRequest.getGrade_id(),
            employeeDetailRequest.getOffice_id(),
            employeeDetailRequest.getEmployee_id())
        .onDuplicateKeyUpdate()
        .set(WORKING_CONTRACT.WORKING_CONTRACT_ID, employeeDetailRequest.getWorking_contract_id())
        .set(WORKING_CONTRACT.COMPANY_NAME, employeeDetailRequest.getCompany_name())
        .set(WORKING_CONTRACT.CONTRACT_TYPE_ID, employeeDetailRequest.getContract_type_id())
        .set(WORKING_CONTRACT.START_DATE, employeeDetailRequest.getStart_date())
        .set(WORKING_CONTRACT.END_DATE, employeeDetailRequest.getEnd_date())
        .set(WORKING_CONTRACT.BASE_SALARY, employeeDetailRequest.getBase_salary())
        .set(WORKING_CONTRACT.CONTRACT_URL, employeeDetailRequest.getContract_url())
        .set(WORKING_CONTRACT.CONTRACT_STATUS, employeeDetailRequest.getContract_status())
        .set(WORKING_CONTRACT.AREA_ID, employeeDetailRequest.getArea_id())
        .set(WORKING_CONTRACT.JOB_ID, employeeDetailRequest.getGrade_id())
        .set(WORKING_CONTRACT.OFFICE_ID, employeeDetailRequest.getOffice_id())
        .set(WORKING_CONTRACT.EMPLOYEE_ID, employeeDetailRequest.getEmployee_id());
  }

  public InsertOnDuplicateSetMoreStep<?> updateEmployee(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    return DSL.using(configuration)
        .insertInto(
            EMPLOYEE,
            EMPLOYEE.EMPLOYEE_ID,
            EMPLOYEE.FULL_NAME,
            EMPLOYEE.WORKING_STATUS,
            EMPLOYEE.PHONE_NUMBER,
            EMPLOYEE.BIRTH_DATE,
            EMPLOYEE.COMPANY_EMAIL,
            EMPLOYEE.GENDER,
            EMPLOYEE.EMPLOYEE_TYPE_ID,
            EMPLOYEE.ADDRESS,
            EMPLOYEE.AVATAR,
            EMPLOYEE.CURRENT_SITUATION,
            EMPLOYEE.FACEBOOK,
            EMPLOYEE.MANAGER_ID,
            EMPLOYEE.NICK_NAME,
            EMPLOYEE.PERSONAL_EMAIL,
            EMPLOYEE.ROLE_TYPE,
            EMPLOYEE.TAX_CODE,
            EMPLOYEE.WORKING_TYPE_ID,
            EMPLOYEE.MARITAL_STATUS)
        .values(
            employeeDetailRequest.getEmployee_id(),
            employeeDetailRequest.getFull_name(),
            employeeDetailRequest.getWorking_status(),
            employeeDetailRequest.getPhone_number(),
            employeeDetailRequest.getBirth_date(),
            employeeDetailRequest.getCompany_email(),
            employeeDetailRequest.getGender(),
            employeeDetailRequest.getEmployee_type_id(),
            employeeDetailRequest.getAddress(),
            employeeDetailRequest.getAvatar(),
            employeeDetailRequest.getCurrent_situation(),
            employeeDetailRequest.getFacebook(),
            employeeDetailRequest.getManager_id(),
            employeeDetailRequest.getNick_name(),
            employeeDetailRequest.getPersonal_email(),
            employeeDetailRequest.getRole_type(),
            employeeDetailRequest.getTax_code(),
            employeeDetailRequest.getWorking_type_id(),
            employeeDetailRequest.getMarital_status())
        .onDuplicateKeyUpdate()
        .set(EMPLOYEE.FULL_NAME, employeeDetailRequest.getFull_name())
        .set(EMPLOYEE.WORKING_STATUS, employeeDetailRequest.getWorking_status())
        .set(EMPLOYEE.PHONE_NUMBER, employeeDetailRequest.getPhone_number())
        .set(EMPLOYEE.BIRTH_DATE, employeeDetailRequest.getBirth_date())
        .set(EMPLOYEE.COMPANY_EMAIL, employeeDetailRequest.getCompany_email())
        .set(EMPLOYEE.GENDER, employeeDetailRequest.getGender())
        .set(EMPLOYEE.EMPLOYEE_TYPE_ID, employeeDetailRequest.getEmployee_type_id())
        .set(EMPLOYEE.ADDRESS, employeeDetailRequest.getAddress())
        .set(EMPLOYEE.AVATAR, employeeDetailRequest.getAvatar())
        .set(EMPLOYEE.CURRENT_SITUATION, employeeDetailRequest.getCurrent_situation())
        .set(EMPLOYEE.FACEBOOK, employeeDetailRequest.getFacebook())
        .set(EMPLOYEE.MANAGER_ID, employeeDetailRequest.getManager_id())
        .set(EMPLOYEE.NICK_NAME, employeeDetailRequest.getNick_name())
        .set(EMPLOYEE.PERSONAL_EMAIL, employeeDetailRequest.getPersonal_email())
        .set(EMPLOYEE.ROLE_TYPE, employeeDetailRequest.getRole_type())
        .set(EMPLOYEE.TAX_CODE, employeeDetailRequest.getTax_code())
        .set(EMPLOYEE.WORKING_TYPE_ID, employeeDetailRequest.getWorking_type_id())
        .set(EMPLOYEE.MARITAL_STATUS, employeeDetailRequest.getMarital_status());
  }

  @Override
  public List<RelativeInformationResponse> findRelativeByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                RELATIVE_INFORMATION.RELATIVE_ID,
                RELATIVE_INFORMATION.PARENT_NAME,
                RELATIVE_INFORMATION.BIRTH_DATE,
                RELATIVE_TYPE.TYPE_ID,
                RELATIVE_INFORMATION.STATUS,
                RELATIVE_INFORMATION.CONTACT)
            .from(RELATIVE_INFORMATION)
            .leftJoin(RELATIVE_TYPE)
            .on(RELATIVE_INFORMATION.RELATIVE_TYPE.eq(RELATIVE_TYPE.TYPE_ID))
            .rightJoin(EMPLOYEE)
            .on(RELATIVE_INFORMATION.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(RelativeInformationResponse.class);
  }

  @Override
  public List<WorkingHistoryResponse> findWorkingHistoryByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                WORKING_HISTORY.WORKING_HISTORY_ID,
                WORKING_HISTORY.COMPANY_NAME,
                WORKING_HISTORY.TYPE_ID,
                WORKING_HISTORY.POSITION,
                WORKING_HISTORY.START_DATE,
                WORKING_HISTORY.END_DATE)
            .from(WORKING_HISTORY)
            .rightJoin(EMPLOYEE)
            .on(WORKING_HISTORY.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(WorkingHistoryResponse.class);
  }

  @Override
  public List<EducationResponse> findEducationByEmployeeID(String employeeID) {
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
    return query.fetchInto(EducationResponse.class);
  }

  @Override
  public List<BankResponse> findBankByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                BANK.BANK_ID, BANK.NAME_BANK, BANK.ADDRESS, BANK.ACCOUNT_NUMBER, BANK.ACCOUNT_NAME)
            .from(BANK)
            .rightJoin(EMPLOYEE)
            .on(BANK.BANK_ID.eq(EMPLOYEE.BANK_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(BankResponse.class);
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
                EMPLOYEE.PERSONAL_EMAIL,
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
  
  @Override
  public boolean checkEmployeeIDIsExists(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
            dslContext.selectFrom(EMPLOYEE).where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID)));
  }
}