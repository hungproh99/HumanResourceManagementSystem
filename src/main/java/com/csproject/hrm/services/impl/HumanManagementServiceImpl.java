package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EWorkStatus;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.request.UpdateHrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.entities.Employee;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.ContractRepository;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.HumanManagementService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class HumanManagementServiceImpl implements HumanManagementService {
  @Autowired EmployeeRepository employeeRepository;
  @Autowired ContractRepository contractRepository;
  @Autowired GeneralFunction generalFunction;
  @Autowired PasswordEncoder passwordEncoder;

  @Override
  public HrmResponseList getListHumanResource(QueryParam queryParam) {
    List<HrmResponse> hrmResponses = employeeRepository.findAllEmployee(queryParam);
    int total = employeeRepository.countAllEmployeeByCondition(queryParam);
    return new HrmResponseList(hrmResponses, total);
  }

  @Override
  public void insertEmployee(HrmRequest hrmRequest) {
    if (hrmRequest.getFullName() == null
        || hrmRequest.getRole() == null
        || hrmRequest.getPhone() == null
        || hrmRequest.getGender() == null
        || hrmRequest.getBirthDate() == null
        || hrmRequest.getGrade() == null
        || hrmRequest.getPosition() == null
        || hrmRequest.getOffice() == null
        || hrmRequest.getArea() == null
        || hrmRequest.getWorkingType() == null
        || hrmRequest.getManagerId() == null
        || hrmRequest.getEmployeeType() == null
        || hrmRequest.getPersonalEmail() == null) {
      throw new CustomParameterConstraintException(FILL_NOT_FULL);
    } else if (!hrmRequest.getPhone().matches(PHONE_VALIDATION)) {
      throw new CustomParameterConstraintException(INVALID_PHONE_FORMAT);
    }
    HrmPojo hrmPojo = createHrmPojo(hrmRequest);
    String employeeId = generalFunction.generateIdEmployee(hrmRequest.getFullName(), 0);
    String companyEmail = generalFunction.generateEmailEmployee(employeeId);
    hrmPojo.setEmployeeId(employeeId);
    hrmPojo.setCompanyEmail(companyEmail);

    employeeRepository.insertEmployee(hrmPojo);
  }

  @Override
  public List<WorkingTypeDto> getListWorkingType() {
    return employeeRepository.getListWorkingType();
  }

  @Override
  public List<EmployeeTypeDto> getListEmployeeType() {
    return employeeRepository.getListEmployeeType();
  }

  @Override
  public List<RoleDto> getListRoleType() {
    return employeeRepository.getListRoleType();
  }

  @Override
  public List<OfficeDto> getListOffice() {
    return contractRepository.getListOffice();
  }

  @Override
  public List<AreaDto> getListArea() {
    return contractRepository.getListArea();
  }

  @Override
  public List<JobDto> getListPosition() {
    return contractRepository.getListPosition();
  }

  @Override
  public List<GradeDto> getListGradeByPosition(String id) {
    if (id == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    }
    try {
      long jobId = Long.parseLong(id);
      return contractRepository.getListGradeByPosition(jobId);
    } catch (NumberFormatException e) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, WRONG_NUMBER_FORMAT);
    }
  }

  @Override
  public void updateEmployeeById(UpdateHrmRequest updateHrmRequest, String employeeId) {
    Optional<Employee> employee = employeeRepository.findById(employeeId);
    if (employee.isEmpty()) {
      throw new CustomDataNotFoundException(NO_EMPLOYEE_WITH_ID + employeeId);
    }
    employeeRepository.updateEmployeeById(updateHrmRequest, employeeId);
  }

  @Override
  public void exportEmployeeToCsv(Writer writer, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<HrmResponse> hrmResponses = employeeRepository.findEmployeeByListId(list);
      try (CSVPrinter csvPrinter =
          new CSVPrinter(
              writer,
              CSVFormat.DEFAULT.withHeader(
                  "Employee Id",
                  "Full Name",
                  "Company Email",
                  "Working Status",
                  "Phone",
                  "Gender",
                  "Birth Date",
                  "Grade",
                  "Office",
                  "Area",
                  "Seniority",
                  "Position",
                  "Working Name"))) {

        for (HrmResponse hrmResponse : hrmResponses) {
          csvPrinter.printRecord(
              hrmResponse.getEmployee_id(),
              hrmResponse.getFull_name(),
              hrmResponse.getEmail(),
              hrmResponse.getWorking_status(),
              hrmResponse.getPhone(),
              hrmResponse.getGender(),
              hrmResponse.getBirth_date(),
              hrmResponse.getGrade(),
              hrmResponse.getOffice_name(),
              hrmResponse.getArea_name(),
              hrmResponse.getSeniority(),
              hrmResponse.getPosition_name(),
              hrmResponse.getWorking_name());
        }
        csvPrinter.flush();
      } catch (IOException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, CAN_NOT_WRITE_CSV);
      }
    }
  }

  @Override
  public void importCsvToEmployee(InputStream inputStream) {
    List<HrmRequest> hrmRequestList = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        CSVParser csvParser =
            new CSVParser(
                reader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()); ) {
      for (CSVRecord csvRecord : csvParser) {
        String fullName = csvRecord.get("Full Name");
        String role = csvRecord.get("Role");
        String phone = csvRecord.get("Phone");
        String gender = csvRecord.get("Gender");
        LocalDate birthDate =
            LocalDate.parse(csvRecord.get("Birth Date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String grade = csvRecord.get("Grade");
        String position = csvRecord.get("Position");
        String office = csvRecord.get("Office");
        String area = csvRecord.get("Area");
        String workingType = csvRecord.get("Working Type");
        String managerId = csvRecord.get("Manager Id");
        String employeeType = csvRecord.get("Employee Type");
        String personalEmail = csvRecord.get("Personal Email");
        hrmRequestList.add(
            HrmRequest.builder()
                .fullName(fullName)
                .role(role)
                .phone(phone)
                .gender(gender)
                .birthDate(birthDate)
                .grade(grade)
                .position(position)
                .office(office)
                .area(area)
                .workingType(workingType)
                .managerId(managerId)
                .employeeType(employeeType)
                .personalEmail(personalEmail)
                .build());
      }
      insertMultiEmployee(hrmRequestList);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<String> getListManagerByName(QueryParam queryParam) {
    return employeeRepository.getListManagerByName(queryParam);
  }

  private void insertMultiEmployee(List<HrmRequest> hrmRequestList) {
    List<HrmPojo> hrmPojos = new ArrayList<>();
    hrmRequestList.forEach(
        hrmRequest -> {
          if (hrmRequest.getFullName() == null
              || hrmRequest.getRole() == null
              || hrmRequest.getPhone() == null
              || hrmRequest.getGender() == null
              || hrmRequest.getBirthDate() == null
              || hrmRequest.getGrade() == null
              || hrmRequest.getPosition() == null
              || hrmRequest.getOffice() == null
              || hrmRequest.getArea() == null
              || hrmRequest.getWorkingType() == null
              || hrmRequest.getManagerId() == null
              || hrmRequest.getEmployeeType() == null
              || hrmRequest.getPersonalEmail() == null) {
            throw new CustomParameterConstraintException(CSV_NULL_DATA);
          } else if (!hrmRequest.getPhone().matches(PHONE_VALIDATION)) {
            throw new CustomParameterConstraintException(INVALID_PHONE_FORMAT);
          }
          HrmPojo hrmPojo = createHrmPojo(hrmRequest);
          int countList = 0;
          for (HrmPojo hrm : hrmPojos) {
            if (hrmPojo.getFullName().equalsIgnoreCase(hrm.getFullName())) {
              countList++;
            }
          }
          String employeeId =
              generalFunction.generateIdEmployee(hrmRequest.getFullName(), countList);
          String companyEmail = generalFunction.generateEmailEmployee(employeeId);
          hrmPojo.setEmployeeId(employeeId);
          hrmPojo.setCompanyEmail(companyEmail);
          hrmPojos.add(hrmPojo);
        });

    employeeRepository.insertMultiEmployee(hrmPojos);
  }

  private HrmPojo createHrmPojo(HrmRequest hrmRequest) {
    String password = passwordEncoder.encode(generalFunction.generateCommonLangPassword());
    String companyName = "HRM";

    HrmPojo hrmPojo =
        HrmPojo.builder()
            .password(password)
            .workStatus(EWorkStatus.ACTIVE.name())
            .companyName(companyName)
            .fullName(hrmRequest.getFullName())
            .role(hrmRequest.getRole())
            .phone(hrmRequest.getPhone())
            .gender(hrmRequest.getGender())
            .birthDate(hrmRequest.getBirthDate())
            .grade(hrmRequest.getGrade())
            .position(hrmRequest.getPosition())
            .office(hrmRequest.getOffice())
            .area(hrmRequest.getArea())
            .workingType(hrmRequest.getWorkingType())
            .managerId(hrmRequest.getManagerId())
            .employeeType(hrmRequest.getEmployeeType())
            .personalEmail(hrmRequest.getPersonalEmail())
            .build();
    //
    //    generalFunction.sendEmail(
    //        FROM_EMAIL,
    //        TO_EMAIL,
    //        SEND_PASSWORD_SUBJECT,
    //        String.format(SEND_PASSWORD_TEXT, employeeId, password));
    return hrmPojo;
  }
}
