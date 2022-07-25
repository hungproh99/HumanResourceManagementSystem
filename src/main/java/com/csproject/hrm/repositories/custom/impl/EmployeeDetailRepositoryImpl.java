package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.repositories.custom.EmployeeDetailRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.codegen.maven.example.Tables;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.csproject.hrm.common.constant.Constants.*;
import static org.jooq.codegen.maven.example.Tables.*;
import static org.jooq.codegen.maven.example.tables.Area.AREA;
import static org.jooq.codegen.maven.example.tables.Employee.EMPLOYEE;
import static org.jooq.codegen.maven.example.tables.Office.OFFICE;
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
        .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(taxAndInsurance.getEmployeeId()))
        .execute();

//    dslContext
//        .insertInto(
//            EMPLOYEE_INSURANCE,
//            EMPLOYEE_INSURANCE.EMPLOYEE_INSURANCE_ID,
//            EMPLOYEE_INSURANCE.EMPLOYEE_ID,
//            EMPLOYEE_INSURANCE.ADDRESS,
//            EMPLOYEE_INSURANCE.INSURANCE_STATUS,
//            EMPLOYEE_INSURANCE.POLICY_TYPE_ID)
//        .values(
//            taxAndInsurance.getInsuranceId(),
//            taxAndInsurance.getEmployeeId(),
//            taxAndInsurance.getInsuranceAddress(),
//            taxAndInsurance.getInsuranceStatus(),
//            taxAndInsurance.getPolicyTypeId())
//        .onDuplicateKeyUpdate()
//        .set(EMPLOYEE_INSURANCE.EMPLOYEE_ID, taxAndInsurance.getEmployeeId())
//        .set(EMPLOYEE_INSURANCE.ADDRESS, taxAndInsurance.getInsuranceAddress())
//        .set(EMPLOYEE_INSURANCE.INSURANCE_STATUS, taxAndInsurance.getInsuranceStatus())
//        .set(EMPLOYEE_INSURANCE.POLICY_TYPE_ID, taxAndInsurance.getPolicyTypeId())
//        .execute();
  }

  @Override
  public void updateAdditionalInfo(EmployeeAdditionalInfoRequest employeeAdditionalInfo) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          queries.add(updateIdentityCard(configuration, employeeAdditionalInfo));
          queries.add(updateEmployeeAdditional(configuration, employeeAdditionalInfo));
          DSL.using(configuration).batch(queries).execute();
        });
  }

  public InsertOnDuplicateSetMoreStep<?> updateIdentityCard(
      Configuration configuration, EmployeeAdditionalInfoRequest employeeAdditionalInfo) {
    return DSL.using(configuration)
        .insertInto(
            IDENTITY_CARD,
            IDENTITY_CARD.PLACE_OF_RESIDENCE,
            IDENTITY_CARD.PLACE_OF_ORIGIN,
            IDENTITY_CARD.NATIONALITY,
            IDENTITY_CARD.CARD_ID,
            IDENTITY_CARD.PROVIDE_DATE,
            IDENTITY_CARD.PROVIDE_PLACE)
        .values(
            employeeAdditionalInfo.getPlace_of_residence(),
            employeeAdditionalInfo.getPlace_of_origin(),
            employeeAdditionalInfo.getNationality(),
            employeeAdditionalInfo.getCard_id(),
            employeeAdditionalInfo.getProvideDate(),
            employeeAdditionalInfo.getProvidePlace())
        .onDuplicateKeyUpdate()
        .set(IDENTITY_CARD.PLACE_OF_RESIDENCE, employeeAdditionalInfo.getPlace_of_residence())
        .set(IDENTITY_CARD.PLACE_OF_ORIGIN, employeeAdditionalInfo.getPlace_of_origin())
        .set(IDENTITY_CARD.NATIONALITY, employeeAdditionalInfo.getNationality())
        .set(IDENTITY_CARD.CARD_ID, employeeAdditionalInfo.getCard_id())
        .set(IDENTITY_CARD.PROVIDE_DATE, employeeAdditionalInfo.getProvideDate())
        .set(IDENTITY_CARD.PROVIDE_PLACE, employeeAdditionalInfo.getProvidePlace());
  }

  public Update<?> updateEmployeeAdditional(
      Configuration configuration, EmployeeAdditionalInfoRequest employeeAdditionalInfo) {
    return DSL.using(configuration)
        .update(EMPLOYEE)
        .set(EMPLOYEE.CARD_ID, employeeAdditionalInfo.getCard_id())
        .set(EMPLOYEE.ADDRESS, employeeAdditionalInfo.getAddress())
        .set(EMPLOYEE.PERSONAL_EMAIL, employeeAdditionalInfo.getPersonal_email())
        .set(EMPLOYEE.PHONE_NUMBER, employeeAdditionalInfo.getPhone_number())
        .set(EMPLOYEE.NICK_NAME, employeeAdditionalInfo.getNick_name())
        .set(EMPLOYEE.FACEBOOK, employeeAdditionalInfo.getFacebook())
        .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeAdditionalInfo.getEmployee_id()));
  }

  @Override
  public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          queries.add(updateEmployee(configuration, employeeDetailRequest));
          queries.add(updateWorkingPlace(configuration, employeeDetailRequest));
          queries.add(updateWorkingContract(configuration, employeeDetailRequest));
          DSL.using(configuration).batch(queries).execute();
        });
  }

  public InsertOnDuplicateSetMoreStep<?> updateWorkingPlace(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    return DSL.using(configuration)
        .insertInto(
            WORKING_PLACE,
            WORKING_PLACE.WORKING_PLACE_ID,
            WORKING_PLACE.WORKING_CONTRACT_ID,
            WORKING_PLACE.AREA_ID,
            WORKING_PLACE.JOB_ID,
            WORKING_PLACE.OFFICE_ID,
            WORKING_PLACE.GRADE_ID,
            WORKING_PLACE.WORKING_PLACE_STATUS)
        .values(
            employeeDetailRequest.getWorking_place_id(),
            employeeDetailRequest.getWorking_contract_id(),
            employeeDetailRequest.getArea_id(),
            employeeDetailRequest.getGrade_id(),
            employeeDetailRequest.getOffice_id(),
            employeeDetailRequest.getGrade_id(),
            employeeDetailRequest.getWorking_place_status())
        .onDuplicateKeyUpdate()
        .set(WORKING_PLACE.WORKING_PLACE_ID, employeeDetailRequest.getWorking_place_id())
        .set(WORKING_PLACE.WORKING_CONTRACT_ID, employeeDetailRequest.getWorking_contract_id())
        .set(WORKING_PLACE.GRADE_ID, employeeDetailRequest.getGrade_id())
        .set(WORKING_PLACE.AREA_ID, employeeDetailRequest.getArea_id())
        .set(WORKING_PLACE.JOB_ID, employeeDetailRequest.getGrade_id())
        .set(WORKING_PLACE.OFFICE_ID, employeeDetailRequest.getOffice_id())
        .set(WORKING_PLACE.WORKING_PLACE_STATUS, employeeDetailRequest.getWorking_place_status());
  }

  public InsertOnDuplicateSetMoreStep<?> updateWorkingContract(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    return DSL.using(configuration)
        .insertInto(
            WORKING_CONTRACT,
            WORKING_CONTRACT.WORKING_CONTRACT_ID,
            WORKING_CONTRACT.START_DATE,
            WORKING_CONTRACT.END_DATE,
            WORKING_CONTRACT.CONTRACT_URL,
            WORKING_CONTRACT.EMPLOYEE_ID)
        .values(
            employeeDetailRequest.getWorking_contract_id(),
            employeeDetailRequest.getStart_date(),
            employeeDetailRequest.getEnd_date(),
            employeeDetailRequest.getContract_url(),
            employeeDetailRequest.getEmployee_id())
        .onDuplicateKeyUpdate()
        .set(WORKING_CONTRACT.WORKING_CONTRACT_ID, employeeDetailRequest.getWorking_contract_id())
        .set(WORKING_CONTRACT.START_DATE, employeeDetailRequest.getStart_date())
        .set(WORKING_CONTRACT.END_DATE, employeeDetailRequest.getEnd_date())
        .set(WORKING_CONTRACT.CONTRACT_URL, employeeDetailRequest.getContract_url())
        .set(WORKING_CONTRACT.EMPLOYEE_ID, employeeDetailRequest.getEmployee_id());
  }

  public Update<?> updateEmployee(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    return DSL.using(configuration)
        .update(EMPLOYEE)
        .set(EMPLOYEE.FULL_NAME, employeeDetailRequest.getFull_name())
        .set(EMPLOYEE.WORKING_STATUS, employeeDetailRequest.getWorking_status())
        .set(EMPLOYEE.PHONE_NUMBER, employeeDetailRequest.getPhone_number())
        .set(EMPLOYEE.BIRTH_DATE, employeeDetailRequest.getBirth_date())
        .set(EMPLOYEE.COMPANY_EMAIL, employeeDetailRequest.getCompany_email())
        .set(EMPLOYEE.GENDER, employeeDetailRequest.getGender())
        .set(EMPLOYEE.AVATAR, employeeDetailRequest.getAvatar())
        .set(EMPLOYEE.MARITAL_STATUS, employeeDetailRequest.getMarital_status())
        .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeDetailRequest.getEmployee_id()));
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
                RELATIVE_TYPE.NAME.as("relative_type_name"),
                RELATIVE_INFORMATION.STATUS,
                RELATIVE_INFORMATION.CONTACT)
            .from(RELATIVE_INFORMATION)
            .leftJoin(RELATIVE_TYPE)
            .on(RELATIVE_INFORMATION.RELATIVE_TYPE.eq(RELATIVE_TYPE.TYPE_ID))
            .rightJoin(EMPLOYEE)
            .on(RELATIVE_INFORMATION.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID));
    return query.fetchInto(RelativeInformationResponse.class);
  }

  @Override
  public Optional<CareerHistoryResponse> findCareerHistoryByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return null;
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
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID));
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
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID));
    return query.fetchInto(EducationResponse.class);
  }

  @Override
  public Optional<BankResponse> findBankByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                BANK.BANK_ID, BANK.NAME_BANK, BANK.ADDRESS, BANK.ACCOUNT_NUMBER, BANK.ACCOUNT_NAME)
            .from(BANK)
            .rightJoin(EMPLOYEE)
            .on(BANK.BANK_ID.eq(EMPLOYEE.BANK_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID));
    return query.fetchOptionalInto(BankResponse.class);
  }

  @Override
  public Optional<EmployeeAdditionalInfo> findAdditionalInfo(String employeeID) {
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
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID));
    return query.fetchOptionalInto(EmployeeAdditionalInfo.class);
  }

  @Override
  public Optional<TaxAndInsuranceResponse> findTaxAndInsurance(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EMPLOYEE.TAX_CODE,
                EMPLOYEE_INSURANCE.EMPLOYEE_INSURANCE_ID,
                EMPLOYEE_INSURANCE.ADDRESS,
                POLICY_TYPE.POLICY_TYPE_,
                POLICY_TYPE.POLICY_TYPE_ID,
                POLICY_TYPE.POLICY_CATEGORY_ID)
            .from(EMPLOYEE)
            .leftJoin(EMPLOYEE_INSURANCE)
            .on(EMPLOYEE_INSURANCE.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
//            .leftJoin(POLICY_TYPE)
//            .on(EMPLOYEE_INSURANCE.POLICY_TYPE_ID.eq(POLICY_TYPE.POLICY_TYPE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID));
    return query.fetchOptionalInto(TaxAndInsuranceResponse.class);
  }

  @Override
  public Optional<EmployeeDetailResponse> findMainDetail(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                EMPLOYEE.EMPLOYEE_ID,
                EMPLOYEE.FULL_NAME,
                EMPLOYEE.COMPANY_EMAIL,
                (when(EMPLOYEE.WORKING_STATUS.eq(Boolean.TRUE), ACTIVE)
                        .when(EMPLOYEE.WORKING_STATUS.eq(Boolean.FALSE), DEACTIVE))
                    .as(WORKING_STATUS),
                EMPLOYEE.PHONE_NUMBER,
                EMPLOYEE.GENDER,
                EMPLOYEE.BIRTH_DATE,
                EMPLOYEE.MARITAL_STATUS,
                EMPLOYEE.AVATAR,
                GRADE_TYPE.NAME.as(GRADE),
                OFFICE.NAME.as(OFFICE_NAME),
                AREA.NAME.as(AREA_NAME),
                year(currentDate())
                    .minus(year(WORKING_CONTRACT.START_DATE))
                    .concat(YEAR)
                    .concat(month(currentDate().minus(month(WORKING_CONTRACT.START_DATE))))
                    .concat(MONTH)
                    .concat(day(currentDate().minus(day(WORKING_CONTRACT.START_DATE))))
                    .concat(DAY)
                    .as(SENIORITY),
                JOB.POSITION.as(POSITION_NAME),
                WORKING_TYPE.NAME.as(WORKING_NAME),
                WORKING_CONTRACT.CONTRACT_URL,
                WORKING_CONTRACT.START_DATE,
                WORKING_CONTRACT.END_DATE,
                WORKING_CONTRACT.WORKING_CONTRACT_ID,
                WORKING_PLACE.WORKING_PLACE_ID)
            .from(EMPLOYEE)
            .leftJoin(Tables.WORKING_CONTRACT)
            .on(Tables.WORKING_CONTRACT.EMPLOYEE_ID.eq(EMPLOYEE.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .leftJoin(OFFICE)
            .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
            .leftJoin(Tables.JOB)
            .on(Tables.JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
            .leftJoin(Tables.GRADE_TYPE)
            .on(Tables.GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
            .leftJoin(WORKING_TYPE)
            .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID));

    return query.fetchOptionalInto(EmployeeDetailResponse.class);
  }

  @Override
  public boolean checkEmployeeIDIsExists(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(EMPLOYEE.EMPLOYEE_ID)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID)));
  }

  @Override
  public String getManagerByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    String managerID =
        dslContext
            .select(EMPLOYEE.MANAGER_ID)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID))
            .fetchOneInto(String.class);

    return dslContext
        .select(EMPLOYEE.FULL_NAME.concat(" - ").concat(EMPLOYEE.EMPLOYEE_ID))
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(managerID))
        .fetchOneInto(String.class);
  }

  @Override
  public String getEmployeeInfoByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.FULL_NAME.concat(" - ").concat(EMPLOYEE.EMPLOYEE_ID))
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID))
        .fetchOneInto(String.class);
  }

  @Override
  public Integer getLevelByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select()
        .select(EMPLOYEE.LEVEL)
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.equalIgnoreCase(employeeID))
        .fetchOneInto(Integer.class);
  }

  @Override
  public int countNumberDependentRelative(String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchCount(
        dslContext
            .select()
            .from(RELATIVE_INFORMATION)
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(RELATIVE_INFORMATION.EMPLOYEE_ID))
            .where(RELATIVE_INFORMATION.IS_DEPENDENT.isTrue()));
  }
}