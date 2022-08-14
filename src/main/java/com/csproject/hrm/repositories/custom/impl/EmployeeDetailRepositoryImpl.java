package com.csproject.hrm.repositories.custom.impl;

import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jooq.DBConnection;
import com.csproject.hrm.repositories.SalaryContractRepository;
import com.csproject.hrm.repositories.custom.EmployeeDetailRepositoryCustom;
import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.codegen.maven.example.Tables;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

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
  @Autowired SalaryContractRepository salaryContractRepository;

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
  public boolean checkBankIsExisted(String employeeId) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(BANK.BANK_ID)
            .from(BANK)
            .leftJoin(EMPLOYEE)
            .on(BANK.BANK_ID.eq(EMPLOYEE.BANK_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId)));
  }

  @Override
  public void updateBankInfo(BankRequest bank) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    boolean existed = checkBankIsExisted(bank.getEmployeeId());
    dslContext.transaction(
        configuration -> {
          List<Long> bankID;
          if (existed) {
            dslContext
                .update(BANK)
                .set(BANK.NAME_BANK, bank.getNameBank())
                .set(BANK.ADDRESS, bank.getAddress())
                .set(BANK.ACCOUNT_NUMBER, bank.getAccountNumber())
                .set(BANK.ACCOUNT_NAME, bank.getAccountName())
                .where(BANK.BANK_ID.eq(bank.getId()));
          } else {
            bankID =
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
                    .returningResult(BANK.BANK_ID)
                    .fetchInto(Long.class);
            System.out.println(
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
                    .returningResult(BANK.BANK_ID));
            dslContext
                .update(EMPLOYEE)
                .set(EMPLOYEE.BANK_ID, bankID.get(0))
                .where(EMPLOYEE.EMPLOYEE_ID.eq(bank.getEmployeeId()))
                .execute();
          }
        });
  }

  @Override
  public void updateTaxAndInsurance(TaxAndInsuranceRequest taxAndInsurance) {
    //    final DSLContext dslContext = DSL.using(connection.getConnection());
    //    dslContext
    //        .update(EMPLOYEE)
    //        .set(EMPLOYEE.TAX_CODE, taxAndInsurance.getTaxCode())
    //        .where(EMPLOYEE.EMPLOYEE_ID.eq(taxAndInsurance.getEmployeeId()))
    //        .execute();

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

  private InsertOnDuplicateSetMoreStep<?> updateIdentityCard(
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

  private Update<?> updateEmployeeAdditional(
      Configuration configuration, EmployeeAdditionalInfoRequest employeeAdditionalInfo) {
    return DSL.using(configuration)
        .update(EMPLOYEE)
        .set(EMPLOYEE.CARD_ID, employeeAdditionalInfo.getCard_id())
        .set(EMPLOYEE.ADDRESS, employeeAdditionalInfo.getAddress())
        .set(EMPLOYEE.PERSONAL_EMAIL, employeeAdditionalInfo.getPersonal_email())
        .set(EMPLOYEE.PHONE_NUMBER, employeeAdditionalInfo.getPhone_number())
        .set(EMPLOYEE.NICK_NAME, employeeAdditionalInfo.getNick_name())
        .set(EMPLOYEE.FACEBOOK, employeeAdditionalInfo.getFacebook())
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeAdditionalInfo.getEmployee_id()));
  }

  @Override
  public void updateEmployeeDetail(EmployeeDetailRequest employeeDetailRequest) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext.transaction(
        configuration -> {
          queries.add(updateEmployee(configuration, employeeDetailRequest));
          WorkingPlaceDto workingPlace =
              getWorkingPlaceByContractID(employeeDetailRequest.getWorking_contract_id());
          queries.add(updateWorkingContract(configuration, employeeDetailRequest));
          if (workingPlace.getJob_id().equals(employeeDetailRequest.getJob_id())
              && workingPlace.getArea_id().equals(employeeDetailRequest.getArea_id())
              && workingPlace.getOffice_id().equals(employeeDetailRequest.getOffice_id())
              && workingPlace.getGrade_id().equals(employeeDetailRequest.getGrade_id())) {
            queries.add(updateWorkingPlace(configuration, employeeDetailRequest));
          } else {
            employeeDetailRequest.setWorking_place_id(workingPlace.getWorking_place_id());
            employeeDetailRequest.setStart_date(
                Instant.now().atZone(ZoneId.of("UTC+07")).toLocalDate());
            queries.add(insertWorkingPlace(configuration, employeeDetailRequest));
          }
          DSL.using(configuration).batch(queries).execute();
        });
  }

  private WorkingPlaceDto getWorkingPlaceByContractID(Long contractID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select()
        .from(WORKING_PLACE)
        .where(WORKING_PLACE.WORKING_CONTRACT_ID.eq(contractID))
        .and(WORKING_PLACE.WORKING_PLACE_STATUS.eq(true))
        .fetchOneInto(WorkingPlaceDto.class);
  }

  private Insert<?> insertWorkingPlace(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    DSL.using(configuration)
        .update(WORKING_PLACE)
        .set(WORKING_PLACE.WORKING_PLACE_STATUS, false)
        .where(WORKING_PLACE.WORKING_PLACE_ID.eq(employeeDetailRequest.getWorking_place_id()))
        .execute();
    return DSL.using(configuration)
        .insertInto(
            WORKING_PLACE,
            WORKING_PLACE.WORKING_CONTRACT_ID,
            WORKING_PLACE.AREA_ID,
            WORKING_PLACE.JOB_ID,
            WORKING_PLACE.OFFICE_ID,
            WORKING_PLACE.GRADE_ID,
            WORKING_PLACE.WORKING_PLACE_STATUS,
            WORKING_PLACE.START_DATE)
        .values(
            employeeDetailRequest.getWorking_contract_id(),
            employeeDetailRequest.getArea_id(),
            employeeDetailRequest.getJob_id(),
            employeeDetailRequest.getOffice_id(),
            employeeDetailRequest.getGrade_id(),
            true,
            employeeDetailRequest.getStart_date());
  }

  private Update<?> updateWorkingPlace(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    return DSL.using(configuration)
        .update(WORKING_PLACE)
        .set(WORKING_PLACE.WORKING_CONTRACT_ID, employeeDetailRequest.getWorking_contract_id())
        .set(WORKING_PLACE.GRADE_ID, employeeDetailRequest.getGrade_id())
        .set(WORKING_PLACE.AREA_ID, employeeDetailRequest.getArea_id())
        .set(WORKING_PLACE.JOB_ID, employeeDetailRequest.getJob_id())
        .set(WORKING_PLACE.OFFICE_ID, employeeDetailRequest.getOffice_id())
        .set(WORKING_PLACE.START_DATE, employeeDetailRequest.getStart_date())
        .where(WORKING_PLACE.WORKING_PLACE_ID.eq(employeeDetailRequest.getWorking_place_id()));
  }

  private InsertOnDuplicateSetMoreStep<?> updateWorkingContract(
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

  private Update<?> updateEmployee(
      Configuration configuration, EmployeeDetailRequest employeeDetailRequest) {
    return DSL.using(configuration)
        .update(EMPLOYEE)
        .set(EMPLOYEE.FULL_NAME, employeeDetailRequest.getFull_name())
        .set(EMPLOYEE.WORKING_STATUS, employeeDetailRequest.getWorking_status())
        .set(EMPLOYEE.PHONE_NUMBER, employeeDetailRequest.getPhone_number())
        .set(EMPLOYEE.BIRTH_DATE, employeeDetailRequest.getBirth_date())
        .set(EMPLOYEE.COMPANY_EMAIL, employeeDetailRequest.getCompany_email())
        .set(EMPLOYEE.GENDER, employeeDetailRequest.getGender())
        .set(EMPLOYEE.MARITAL_STATUS, employeeDetailRequest.getMarital_status())
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeDetailRequest.getEmployee_id()));
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
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchInto(RelativeInformationResponse.class);
  }

  @Override
  public CareerHistoryResponse findCareerHistoryByEmployeeID(String employeeID) {
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
  public BankResponse findBankByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                BANK.BANK_ID, BANK.NAME_BANK, BANK.ADDRESS, BANK.ACCOUNT_NUMBER, BANK.ACCOUNT_NAME)
            .from(BANK)
            .rightJoin(EMPLOYEE)
            .on(BANK.BANK_ID.eq(EMPLOYEE.BANK_ID))
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID));
    return query.fetchOneInto(BankResponse.class);
  }

  @Override
  public EmployeeAdditionalInfo findAdditionalInfo(String employeeID) {
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
    return query.fetchOneInto(EmployeeAdditionalInfo.class);
  }

  @Override
  public TaxAndInsuranceResponse findTaxAndInsurance(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    TaxAndInsuranceResponse taxAndInsuranceResponse = new TaxAndInsuranceResponse();

    List<EmployeeInsuranceDto> list =
        dslContext
            .select(
                EMPLOYEE_INSURANCE.EMPLOYEE_INSURANCE_ID.as("insuranceID"),
                EMPLOYEE_INSURANCE.ADDRESS.as("address"),
                POLICY_NAME.POLICY_NAME_.as("insuranceName"))
            .from(EMPLOYEE_INSURANCE)
            .leftJoin(POLICY_NAME)
            .on(EMPLOYEE_INSURANCE.POLICY_NAME_ID.eq(POLICY_NAME.POLICY_NAME_ID))
            .where(EMPLOYEE_INSURANCE.EMPLOYEE_ID.eq(employeeID))
            .fetchInto(EmployeeInsuranceDto.class);

    String taxCode =
        dslContext
            .select(EMPLOYEE.TAX_CODE)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID))
            .fetchOneInto(String.class);
    return TaxAndInsuranceResponse.builder().tax_code(taxCode).insuranceDtos(list).build();
  }

  @Override
  public EmployeeDetailResponse findMainDetail(String employeeID) {
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
                    .minus(year(Tables.WORKING_CONTRACT.START_DATE))
                    .concat(YEAR)
                    .concat(month(currentDate()).minus(month(Tables.WORKING_CONTRACT.START_DATE)))
                    .concat(MONTH)
                    .concat(day(currentDate()).minus(day(Tables.WORKING_CONTRACT.START_DATE)))
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
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID))
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue());

    return query.fetchOneInto(EmployeeDetailResponse.class);
  }

  @Override
  public WorkingInfoResponse findWorkingInfo(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    final var query =
        dslContext
            .select(
                (SALARY_CONTRACT.ADDITIONAL_SALARY.add(SALARY_CONTRACT.BASE_SALARY))
                    .as("final_salary"),
                SALARY_CONTRACT.BASE_SALARY,
                OFFICE.NAME.as("office"),
                AREA.NAME.as("area"),
                JOB.POSITION,
                GRADE_TYPE.NAME.as("grade"),
                WORKING_TYPE.NAME.as("working_type"),
                SALARY_CONTRACT.START_DATE,
                EMPLOYEE_TYPE.NAME.as("employee_type"),
                SALARY_CONTRACT.SALARY_CONTRACT_ID,
                WORKING_CONTRACT.WORKING_CONTRACT_ID,
                WORKING_PLACE.WORKING_PLACE_ID,
                EMPLOYEE.MANAGER_ID)
            .from(WORKING_CONTRACT)
            .leftJoin(EMPLOYEE)
            .on(EMPLOYEE.EMPLOYEE_ID.eq(WORKING_CONTRACT.EMPLOYEE_ID))
            .leftJoin(WORKING_PLACE)
            .on(WORKING_PLACE.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(AREA)
            .on(AREA.AREA_ID.eq(WORKING_PLACE.AREA_ID))
            .leftJoin(OFFICE)
            .on(OFFICE.OFFICE_ID.eq(WORKING_PLACE.OFFICE_ID))
            .leftJoin(JOB)
            .on(Tables.JOB.JOB_ID.eq(WORKING_PLACE.JOB_ID))
            .leftJoin(GRADE_TYPE)
            .on(Tables.GRADE_TYPE.GRADE_ID.eq(WORKING_PLACE.GRADE_ID))
            .leftJoin(WORKING_TYPE)
            .on(WORKING_TYPE.TYPE_ID.eq(EMPLOYEE.WORKING_TYPE_ID))
            .leftJoin(SALARY_CONTRACT)
            .on(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(WORKING_CONTRACT.WORKING_CONTRACT_ID))
            .leftJoin(EMPLOYEE_TYPE)
            .on(EMPLOYEE_TYPE.TYPE_ID.eq(EMPLOYEE.EMPLOYEE_TYPE_ID))
            .where(WORKING_CONTRACT.EMPLOYEE_ID.eq(employeeID))
            .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
            .and(WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .and(WORKING_PLACE.WORKING_PLACE_STATUS.isTrue());

    System.out.println(query);

    WorkingInfoResponse workingInfoResponse =
        Objects.requireNonNullElse(
            query.fetchOneInto(WorkingInfoResponse.class), new WorkingInfoResponse());
    workingInfoResponse.setManager_name(
        Objects.requireNonNullElse(
            getManagerByEmployeeID(employeeID).split("-")[0].trim(), "admin"));
    return workingInfoResponse;
  }

  @Override
  public boolean checkEmployeeIDIsExists(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext.fetchExists(
        dslContext
            .select(EMPLOYEE.EMPLOYEE_ID)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID)));
  }

  @Override
  public String getManagerByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    String managerID =
        dslContext
            .select(EMPLOYEE.MANAGER_ID)
            .from(EMPLOYEE)
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID))
            .fetchOneInto(String.class);

    return dslContext
        .select(EMPLOYEE.FULL_NAME.concat(" - ").concat(EMPLOYEE.EMPLOYEE_ID))
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(managerID))
        .fetchOneInto(String.class);
  }

  @Override
  public String getEmployeeInfoByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.FULL_NAME.concat(" - ").concat(EMPLOYEE.EMPLOYEE_ID))
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID))
        .fetchOneInto(String.class);
  }

  @Override
  public Integer getLevelByEmployeeID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());

    return dslContext
        .select(EMPLOYEE.LEVEL)
        .from(EMPLOYEE)
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID))
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
            .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeId)));
  }

  @Override
  public List<EmployeeNameAndID> getAllEmployeeByManagerID(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(EMPLOYEE.EMPLOYEE_ID.as("employeeID"), EMPLOYEE.FULL_NAME.as("name"))
        .from(EMPLOYEE)
        .where(EMPLOYEE.MANAGER_ID.eq(employeeID))
        .fetchInto(EmployeeNameAndID.class);
  }

  @Override
  public void updateAvatar(AvatarRequest avatarRequest) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .update(EMPLOYEE)
        .set(EMPLOYEE.AVATAR, avatarRequest.getAvatar())
        .where(EMPLOYEE.EMPLOYEE_ID.eq(avatarRequest.getEmployeeId()))
        .execute();
  }

  @Override
  public void updateWorkingInfo(WorkingInfoRequest workingInfoRequest) {
    List<Query> queries = new ArrayList<>();
    final DSLContext dslContext = DSL.using(connection.getConnection());
    WorkingPlaceDto workingPlace =
        getWorkingPlaceByContractID(workingInfoRequest.getWorkingContractId());
    dslContext.transaction(
        configuration -> {
          queries.add(
              DSL.using(configuration)
                  .update(EMPLOYEE)
                  .set(EMPLOYEE.WORKING_TYPE_ID, workingInfoRequest.getWorkingTypeId())
                  .set(EMPLOYEE.EMPLOYEE_TYPE_ID, workingInfoRequest.getEmployeeType())
                  .set(EMPLOYEE.MANAGER_ID, workingInfoRequest.getManagerId())
                  .where(EMPLOYEE.EMPLOYEE_ID.eq(workingInfoRequest.getEmployeeId())));
          boolean salaryChange =
              checkSalaryContractChange(
                  workingInfoRequest.getEmployeeId(),
                  new BigDecimal(workingInfoRequest.getBaseSalary()));
          boolean placeChange =
              checkWorkingPlaceChange(
                  workingPlace,
                  workingInfoRequest.getOffice(),
                  workingInfoRequest.getArea(),
                  workingInfoRequest.getPosition(),
                  workingInfoRequest.getGrade());
          if (salaryChange) {
            updateSalaryContract(
                configuration,
                workingInfoRequest.getEmployeeId(),
                new BigDecimal(workingInfoRequest.getBaseSalary()),
                new BigDecimal(workingInfoRequest.getFinalSalary()),
                workingInfoRequest.getStartDate(),
                true);
          } else {
            salaryContractRepository.insertNewSalaryContract(
                workingInfoRequest.getEmployeeId(),
                new BigDecimal(workingInfoRequest.getBaseSalary()),
                workingInfoRequest.getStartDate(),
                false,
                true);
          }
          if (placeChange) {
            queries.add(updateWorkingPlace(configuration, workingInfoRequest));
          } else {
            workingInfoRequest.setWorkingPlaceId(workingPlace.getWorking_place_id());
            workingInfoRequest.setStartDate(
                Instant.now().atZone(ZoneId.of("UTC+07")).toLocalDate());
            queries.add(insertWorkingPlace(configuration, workingInfoRequest));
          }
          DSL.using(configuration).batch(queries).execute();
        });
  }

  private boolean checkWorkingPlaceChange(
      WorkingPlaceDto workingPlace, Long officeId, Long areaID, Long positionId, Long gradeId) {
    return (workingPlace.getJob_id().equals(positionId)
        && workingPlace.getArea_id().equals(areaID)
        && workingPlace.getOffice_id().equals(officeId)
        && workingPlace.getGrade_id().equals(gradeId));
  }

  private boolean checkSalaryContractChange(String employeeId, BigDecimal newSalary) {
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    return (salaryContractDto.get().getBase_salary().equals(newSalary));
  }

  private Update<?> updateWorkingPlace(
      Configuration configuration, WorkingInfoRequest workingInfoRequest) {
    return DSL.using(configuration)
        .update(WORKING_PLACE)
        .set(WORKING_PLACE.WORKING_CONTRACT_ID, workingInfoRequest.getWorkingContractId())
        .set(WORKING_PLACE.GRADE_ID, workingInfoRequest.getGrade())
        .set(WORKING_PLACE.AREA_ID, workingInfoRequest.getArea())
        .set(WORKING_PLACE.JOB_ID, workingInfoRequest.getPosition())
        .set(WORKING_PLACE.OFFICE_ID, workingInfoRequest.getOffice())
        .set(WORKING_PLACE.START_DATE, workingInfoRequest.getStartDate())
        .where(WORKING_PLACE.WORKING_PLACE_ID.eq(workingInfoRequest.getWorkingPlaceId()));
  }

  private Insert<?> insertWorkingPlace(
      Configuration configuration, WorkingInfoRequest workingInfoRequest) {
    DSL.using(configuration)
        .update(WORKING_PLACE)
        .set(WORKING_PLACE.WORKING_PLACE_STATUS, false)
        .where(WORKING_PLACE.WORKING_PLACE_ID.eq(workingInfoRequest.getWorkingPlaceId()))
        .execute();
    return DSL.using(configuration)
        .insertInto(
            WORKING_PLACE,
            WORKING_PLACE.WORKING_CONTRACT_ID,
            WORKING_PLACE.AREA_ID,
            WORKING_PLACE.JOB_ID,
            WORKING_PLACE.OFFICE_ID,
            WORKING_PLACE.GRADE_ID,
            WORKING_PLACE.WORKING_PLACE_STATUS,
            WORKING_PLACE.START_DATE)
        .values(
            workingInfoRequest.getWorkingContractId(),
            workingInfoRequest.getArea(),
            workingInfoRequest.getPosition(),
            workingInfoRequest.getOffice(),
            workingInfoRequest.getGrade(),
            true,
            workingInfoRequest.getStartDate());
  }

  public void updateSalaryContract(
      Configuration configuration,
      String employeeId,
      BigDecimal newSalary,
      BigDecimal finalSalary,
      LocalDate startDate,
      boolean status) {
    final var contractId =
        DSL.using(configuration)
            .select(Tables.WORKING_CONTRACT.WORKING_CONTRACT_ID)
            .from(Tables.EMPLOYEE)
            .leftJoin(Tables.WORKING_CONTRACT)
            .on(Tables.WORKING_CONTRACT.EMPLOYEE_ID.eq(Tables.EMPLOYEE.EMPLOYEE_ID))
            .where(Tables.EMPLOYEE.EMPLOYEE_ID.eq(employeeId))
            .and(Tables.WORKING_CONTRACT.CONTRACT_STATUS.isTrue())
            .fetchOneInto(Long.class);

    DSL.using(configuration)
        .update(SALARY_CONTRACT)
        .set(SALARY_CONTRACT.BASE_SALARY, newSalary)
        .set(SALARY_CONTRACT.ADDITIONAL_SALARY, finalSalary.subtract(newSalary))
        .set(SALARY_CONTRACT.START_DATE, startDate)
        .set(SALARY_CONTRACT.SALARY_CONTRACT_STATUS, status)
        .where(SALARY_CONTRACT.WORKING_CONTRACT_ID.eq(contractId))
        .and(SALARY_CONTRACT.SALARY_CONTRACT_STATUS.isTrue())
        .execute();
  }

  @Override
  public RoleResponse getRole(String employeeID) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    return dslContext
        .select(ROLE_TYPE.TYPE_ID.as("roleID"), ROLE_TYPE.ROLE.as("roleName"))
        .from(EMPLOYEE)
        .leftJoin(ROLE_TYPE)
        .on(ROLE_TYPE.TYPE_ID.eq(EMPLOYEE.ROLE_TYPE))
        .where(EMPLOYEE.EMPLOYEE_ID.eq(employeeID))
        .fetchOneInto(RoleResponse.class);
  }

  @Override
  public void updateRole(RoleRequest roleRequest) {
    final DSLContext dslContext = DSL.using(connection.getConnection());
    dslContext
        .update(EMPLOYEE)
        .set(EMPLOYEE.ROLE_TYPE, Long.valueOf(roleRequest.getRoleId()))
        .where(EMPLOYEE.EMPLOYEE_ID.eq(roleRequest.getEmployeeId()))
        .execute();
  }
}