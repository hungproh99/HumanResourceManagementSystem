package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.*;
import com.csproject.hrm.common.excel.ExcelExportEmployee;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.EmployeeNameAndID;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.HumanManagementService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class HumanManagementServiceImpl implements HumanManagementService {
  @Autowired EmployeeRepository employeeRepository;
  @Autowired EmployeeDetailRepository employeeDetailRepository;
  @Autowired WorkingPlaceRepository workingPlaceRepository;
  @Autowired GeneralFunction generalFunction;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired RoleRepository roleRepository;
  @Autowired AreaRepository areaRepository;
  @Autowired OfficeRepository officeRepository;
  @Autowired GradeRepository gradeRepository;
  @Autowired JobRepository jobRepository;
  @Autowired WorkingTypeRepository workingTypeRepository;
  @Autowired EmployeeTypeRepository employeeTypeRepository;

  @Override
  public HrmResponseList getListHumanResource(QueryParam queryParam, String employeeId) {
    List<HrmResponse> hrmResponses = employeeRepository.findAllEmployee(queryParam, employeeId);
    int total = employeeRepository.countAllEmployeeByCondition(queryParam);
    return new HrmResponseList(hrmResponses, total);
  }

  @Override
  public void insertEmployee(HrmRequest hrmRequest) {
    if (!roleRepository.existsById(hrmRequest.getRole())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Role not exist");
    } else if (!hrmRequest.getGender().equalsIgnoreCase("Male")
        && !hrmRequest.getGender().equalsIgnoreCase("Female")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Gender must be Female/Male");
    } else if (!areaRepository.existsById(hrmRequest.getArea())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Area not exist");
    } else if (!officeRepository.existsById(hrmRequest.getOffice())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Office not exist");
    } else if (!gradeRepository.existsById(hrmRequest.getGrade())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Grade not exist");
    } else if (!jobRepository.existsById(hrmRequest.getPosition())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Position not exist");
    } else if (!workingTypeRepository.existsById(hrmRequest.getWorkingType())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Working Type not exist");
    } else if (!employeeRepository.existsById(hrmRequest.getManagerId())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Manager Id not exist");
    } else if (!employeeTypeRepository.existsById(hrmRequest.getEmployeeType())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Employee Type not exist");
    } else if (hrmRequest.getBirthDate().isAfter(LocalDate.now().minus(18, ChronoUnit.YEARS))) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Birth Date must be enough 18 age");
    } else if (hrmRequest.getStartDate().isAfter(hrmRequest.getEndDate())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "End Date not after Start Date");
    } else if (hrmRequest.getEndDate().isBefore(LocalDate.now())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "End Date not before Current Date");
    } else if (hrmRequest.getSalary() == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Salary must be not null");
    } else if (hrmRequest.getBaseSalary() == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Base Salary must be not null");
    } else if (hrmRequest.getSalary().compareTo(BigDecimal.ZERO) < 0) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Salary must be positive");
    } else if (hrmRequest.getBaseSalary().compareTo(BigDecimal.ZERO) < 0) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Base Salary must be positive");
    } else if (hrmRequest
            .getBaseSalary()
            .compareTo(BigDecimal.valueOf(Double.parseDouble("5000000")))
        < 0) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Base Salary must greater than 5000000 VND");
    } else if (hrmRequest.getBaseSalary().compareTo(hrmRequest.getSalary()) >= 0) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Base Salary must less than Salary");
    }
    HrmPojo hrmPojo = createHrmPojo(hrmRequest);
    String employeeId = generalFunction.generateIdEmployee(hrmRequest.getFullName(), 0);
    String companyEmail = generalFunction.generateEmailEmployee(employeeId);
    hrmPojo.setEmployeeId(employeeId);
    hrmPojo.setCompanyEmail(companyEmail);

    generalFunction.sendEmailForNewEmployee(
        hrmPojo, FROM_EMAIL, hrmPojo.getPersonalEmail(), SEND_PASSWORD_SUBJECT);
    hrmPojo.setPassword(passwordEncoder.encode(hrmPojo.getPassword()));
    employeeRepository.insertEmployee(hrmPojo);
  }

  @Override
  public List<WorkingTypeDto> getListWorkingType() {
    List<WorkingTypeDto> workingTypeDto = employeeRepository.getListWorkingType();
    workingTypeDto.forEach(
        workingType -> {
          workingType.setName(EWorkingType.getLabel(workingType.getName()));
        });
    return workingTypeDto;
  }

  @Override
  public List<EmployeeTypeDto> getListEmployeeType() {
    List<EmployeeTypeDto> employeeTypeDto = employeeRepository.getListEmployeeType();
    employeeTypeDto.forEach(
        employeeType -> {
          employeeType.setName(EEmployeeType.getLabel(employeeType.getName()));
        });
    return employeeTypeDto;
  }

  @Override
  public List<RoleDto> getListRoleType() {
    List<RoleDto> roleDto = employeeRepository.getListRoleType();
    roleDto.forEach(
        role -> {
          role.setRole(ERole.getLabel(role.getRole()));
        });

    return roleDto;
  }

  @Override
  public List<OfficeDto> getListOffice() {
    List<OfficeDto> officeDto = workingPlaceRepository.getListOffice();
    officeDto.forEach(
        office -> {
          office.setName(EOffice.getLabel(office.getName()));
        });

    return officeDto;
  }

  @Override
  public List<AreaDto> getListArea() {
    List<AreaDto> areaDto = workingPlaceRepository.getListArea();
    areaDto.forEach(
        area -> {
          area.setName(EArea.getLabel(area.getName()));
        });
    return areaDto;
  }

  @Override
  public List<JobDto> getListPosition() {
    List<JobDto> jobDto = workingPlaceRepository.getListPosition();
    jobDto.forEach(
        job -> {
          job.setPosition(EJob.getLabel(job.getPosition()));
        });
    return jobDto;
  }

  @Override
  public List<GradeDto> getListGradeByPosition(Long id) {
    if (id == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA);
    } else if (!workingPlaceRepository.checkExistJobId(id)) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not exist job id " + id);
    }
    List<GradeDto> gradeDto = workingPlaceRepository.getListGradeByPosition(id);
    gradeDto.forEach(
        grade -> {
          grade.setName(EGradeType.getLabel(grade.getName()));
        });
    return gradeDto;
  }

  @Override
  public void exportEmployeeToCsv(Writer writer, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      for (String employeeId : list) {
        if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "Employee Id \"" + employeeId + "\" not exist");
        }
      }
      List<HrmResponse> hrmResponses =
          employeeRepository.findEmployeeByListId(QueryParam.defaultParam(), list);
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
    try (BufferedReader reader =
            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVParser csvParser =
            new CSVParser(
                reader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
      for (CSVRecord csvRecord : csvParser) {
        try {
          checkValidFormatCsv(csvParser);
          String fullName = csvRecord.get("Full Name");
          Long role = ERole.getValue(csvRecord.get("Role"));
          String phone = csvRecord.get("Phone");
          String gender = csvRecord.get("Gender");
          LocalDate birthDate =
              LocalDate.parse(
                  csvRecord.get("Birth Date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          Long grade = EGradeType.getValue(csvRecord.get("Grade"));
          Long position = EJob.getValue(csvRecord.get("Position"));
          Long office = EOffice.getValue(csvRecord.get("Office"));
          Long area = EArea.getValue(csvRecord.get("Area"));
          Long workingType = EWorkingType.getValue(csvRecord.get("Working Type"));
          String managerId = csvRecord.get("Manager Id");
          Long employeeType = EEmployeeType.getValue(csvRecord.get("Employee Type"));
          String personalEmail = csvRecord.get("Personal Email");
          LocalDate startDate =
              LocalDate.parse(
                  csvRecord.get("Start Date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          LocalDate endDate =
              LocalDate.parse(csvRecord.get("End Date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          BigDecimal baseSalary =
              BigDecimal.valueOf(Double.parseDouble(csvRecord.get("Base Salary")));
          BigDecimal salary = BigDecimal.valueOf(Double.parseDouble(csvRecord.get("Salary")));
          if (!roleRepository.existsById(role)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Role not exist");
          } else if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Gender must be Female/Male");
          } else if (!areaRepository.existsById(area)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Area not exist");
          } else if (!officeRepository.existsById(office)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Office not exist");
          } else if (!gradeRepository.existsById(grade)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Grade not exist");
          } else if (!jobRepository.existsById(position)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Position not exist");
          } else if (!workingTypeRepository.existsById(workingType)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Working Type not exist");
          } else if (!employeeRepository.existsById(managerId)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Manager Id not exist");
          } else if (!employeeTypeRepository.existsById(employeeType)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Employee Type not exist");
          } else if (!personalEmail.matches(EMAIL_VALIDATION)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Personal Email not valid");
          } else if (birthDate.isAfter(LocalDate.now().minus(18, ChronoUnit.YEARS))) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "Birth Date must be enough 18 age");
          } else if (startDate.isAfter(endDate)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "End Date not after Start Date");
          } else if (endDate.isBefore(LocalDate.now())) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "End Date not before Current Date");
          } else if (salary == null) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Salary must be not null");
          } else if (baseSalary == null) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Base Salary must be not null");
          } else if (salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Salary must be positive");
          } else if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Base Salary must be positive");
          } else if (baseSalary.compareTo(BigDecimal.valueOf(Double.parseDouble("5000000"))) < 0) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "Base Salary must greater than 5000000 VND");
          } else if (baseSalary.compareTo(salary) >= 0) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "Base Salary must less than Salary");
          }
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
                  .startDate(startDate)
                  .endDate(endDate)
                  .salary(salary)
                  .baseSalary(baseSalary)
                  .build());
        } catch (NumberFormatException e) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, WRONG_NUMBER_FORMAT);
        }
      }
      if (hrmRequestList.isEmpty()) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Nothing to insert");
      }
      insertMultiEmployee(hrmRequestList);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (NumberFormatException e) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Only input number in salary");
    }
  }

  //  @Override
  //  public List<String> getListManagerByName(String name) {
  //    if (name == null) {
  //      throw new CustomParameterConstraintException(FILL_NOT_FULL);
  //    }
  //    return employeeRepository.getListManagerByName(name);
  //  }

  //  @Override
  //  public List<EmployeeNameAndID> getListEmployeeByManagement(String employeeId) {
  //    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
  //      throw new CustomErrorException(
  //          HttpStatus.BAD_REQUEST, "Not exist this employee id " + employeeId);
  //    }
  //    return employeeRepository.getListEmployeeByNameAndId(name);
  //  }

  @Override
  public void exportEmployeeToExcel(HttpServletResponse response, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      for (String employeeId : list) {
        if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "Employee Id \"" + employeeId + "\" not exist");
        }
      }
      try {
        List<HrmResponse> hrmResponses =
            employeeRepository.findEmployeeByListId(QueryParam.defaultParam(), list);
        ExcelExportEmployee excelExportEmployee = new ExcelExportEmployee(hrmResponses);
        excelExportEmployee.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void importExcelToEmployee(Workbook workBook) {
    List<HrmRequest> hrmRequestList = new ArrayList<>();
    Sheet sheet = workBook.getSheetAt(0);
    if (getNumberOfNonEmptyCells(sheet, 0) <= 1) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format excel for import");
    }
    for (int rowIndex = 0; rowIndex <= getNumberOfNonEmptyCells(sheet, 0) - 1; rowIndex++) {
      Row row = sheet.getRow(rowIndex);
      if (rowIndex == 0) {
        checkValidFormatExcel(row);
        continue;
      }
      try {
        String fullName = row.getCell(0).getStringCellValue();
        Long role = ERole.getValue(row.getCell(1).getStringCellValue());
        String phone = "0" + (int) row.getCell(2).getNumericCellValue();
        String gender = row.getCell(3).getStringCellValue();
        LocalDate birthDate = row.getCell(4).getLocalDateTimeCellValue().toLocalDate();
        Long grade = EGradeType.getValue(row.getCell(5).getStringCellValue());
        Long position = EJob.getValue(row.getCell(6).getStringCellValue());
        Long office = EOffice.getValue(row.getCell(7).getStringCellValue());
        Long area = EArea.getValue(row.getCell(8).getStringCellValue());
        Long workingType = EWorkingType.getValue(row.getCell(9).getStringCellValue());
        String managerId = row.getCell(10).toString();
        Long employeeType = EEmployeeType.getValue(row.getCell(11).getStringCellValue());
        String personalEmail = row.getCell(12).toString();
        LocalDate startDate = row.getCell(13).getLocalDateTimeCellValue().toLocalDate();
        LocalDate endDate = row.getCell(14).getLocalDateTimeCellValue().toLocalDate();
        BigDecimal baseSalary = BigDecimal.valueOf(row.getCell(15).getNumericCellValue());
        BigDecimal salary = BigDecimal.valueOf(row.getCell(16).getNumericCellValue());

        if (row.getCell(0) == null
            || row.getCell(1) == null
            || row.getCell(2) == null
            || row.getCell(3) == null
            || row.getCell(4) == null
            || row.getCell(5) == null
            || row.getCell(6) == null
            || row.getCell(7) == null
            || row.getCell(8) == null
            || row.getCell(9) == null
            || row.getCell(10) == null
            || row.getCell(11) == null
            || row.getCell(12) == null
            || row.getCell(13) == null
            || row.getCell(14) == null
            || row.getCell(15) == null
            || row.getCell(16) == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Have empty field in excel");
        } else if (!roleRepository.existsById(role)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Role not exist");
        } else if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Gender must be Female/Male");
        } else if (!areaRepository.existsById(area)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Area not exist");
        } else if (!officeRepository.existsById(office)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Office not exist");
        } else if (!gradeRepository.existsById(grade)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Grade not exist");
        } else if (!jobRepository.existsById(position)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Position not exist");
        } else if (!workingTypeRepository.existsById(workingType)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Working Type not exist");
        } else if (!employeeRepository.existsById(managerId)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Manager Id not exist");
        } else if (!employeeTypeRepository.existsById(employeeType)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Employee Type not exist");
        } else if (!personalEmail.matches(EMAIL_VALIDATION)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Personal Email not valid");
        } else if (birthDate.isAfter(LocalDate.now().minus(18, ChronoUnit.YEARS))) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "Birth Date must be enough 18 age");
        } else if (startDate.isAfter(endDate)) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "End Date not after Start Date");
        } else if (endDate.isBefore(LocalDate.now())) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "End Date not before Current Date");
        } else if (salary == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Salary must be not null");
        } else if (baseSalary == null) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Base Salary must be not null");
        } else if (salary.compareTo(BigDecimal.ZERO) < 0) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Salary must be positive");
        } else if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Base Salary must be positive");
        } else if (baseSalary.compareTo(BigDecimal.valueOf(Double.parseDouble("5000000"))) < 0) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "Base Salary must greater than 5000000 VND");
        } else if (baseSalary.compareTo(salary) >= 0) {
          throw new CustomErrorException(
              HttpStatus.BAD_REQUEST, "Base Salary must less than Salary");
        }
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
                .startDate(startDate)
                .endDate(endDate)
                .salary(salary)
                .baseSalary(baseSalary)
                .build());
      } catch (NumberFormatException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Only input number in salary");
      }
    }
    if (hrmRequestList.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Nothing to insert");
    }
    insertMultiEmployee(hrmRequestList);
  }

  @Override
  public HrmResponseList getListHumanResourceOfManager(QueryParam queryParam, String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee Id \"" + employeeId + "\" not exist");
    }
    return employeeRepository.findAllEmployeeOfManager(queryParam, employeeId);
  }

  @Override
  public List<EmployeeNameAndID> getListManagerHigherOfArea(String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee Id \"" + employeeId + "\" not exist");
    }
    int level = employeeRepository.getLevelOfEmployee(employeeId);
    return employeeRepository.getListManagerHigherOfArea(employeeId, level);
  }

  @Override
  public List<EmployeeNameAndID> getListManagerLowerOfArea(String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Employee Id \"" + employeeId + "\" not exist");
    }
    int level = employeeRepository.getLevelOfEmployee(employeeId);
    return employeeRepository.getListManagerLowerOfArea(employeeId, level);
  }

  @Override
  public void updateWorkingStatusForListEmployee(LocalDate dateCheck) {
    List<String> employeeIdList = employeeRepository.findAllNewEmployeeDeactive(dateCheck);
    employeeRepository.updateWorkingStatusForListEmployee(Boolean.TRUE, employeeIdList);
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
            throw new CustomErrorException(CSV_NULL_DATA);
          } else if (!hrmRequest.getPhone().matches(PHONE_VALIDATION)) {
            throw new CustomErrorException(INVALID_PHONE_FORMAT);
          } else if (!hrmRequest.getFullName().matches(ALPHANUMERIC_VALIDATION)) {
            throw new CustomErrorException(INVALID_ALPHANUMERIC_VALIDATION);
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
          generalFunction.sendEmailForNewEmployee(
              hrmPojo, FROM_EMAIL, hrmPojo.getPersonalEmail(), SEND_PASSWORD_SUBJECT);
        });
    for (HrmPojo hrmPojo : hrmPojos) {
      hrmPojo.setPassword(passwordEncoder.encode(hrmPojo.getPassword()));
    }
    employeeRepository.insertMultiEmployee(hrmPojos);
  }

  private HrmPojo createHrmPojo(HrmRequest hrmRequest) {
    String password = generalFunction.generateCommonLangPassword();
    String companyName = "HRM";
    int level = 0;
    if (hrmRequest.getRole().equals(ERole.getValue(ERole.ROLE_MANAGER.name()))) {
      level = employeeRepository.getLevelOfEmployee(hrmRequest.getManagerId()) + 1;
    } else if (hrmRequest.getRole().equals(ERole.getValue(ERole.ROLE_USER.name()))) {
      level = -1;
    }
    boolean workStatus = false;
    if (hrmRequest.getStartDate().isBefore(LocalDate.now())) {
      workStatus = true;
    }
    LocalDateTime createDate = LocalDateTime.now();

    HrmPojo hrmPojo =
        HrmPojo.builder()
            .password(password)
            .workStatus(workStatus)
            .contractStatus(true)
            .placeStatus(true)
            .level(level)
            .createDate(createDate)
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
            .startDate(hrmRequest.getStartDate())
            .endDate(hrmRequest.getEndDate())
            .salary(hrmRequest.getSalary())
            .baseSalary(hrmRequest.getBaseSalary())
            .build();

    return hrmPojo;
  }

  private int getNumberOfNonEmptyCells(Sheet sheet, int columnIndex) {
    int numOfNonEmptyCells = 0;
    for (int i = 0; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (row != null) {
        Cell cell = row.getCell(columnIndex);
        if (cell != null && cell.getCellType() != CellType.BLANK) {
          numOfNonEmptyCells++;
        }
      }
    }
    return numOfNonEmptyCells;
  }

  private void checkValidFormatCsv(CSVParser csvParser) {
    Set<Map.Entry<String, Integer>> map = csvParser.getHeaderMap().entrySet();
    for (Map.Entry<String, Integer> i : map) {
      if (i.getKey().equals("Full Name")) {
        continue;
      } else if (i.getKey().equals("Role")) {
        continue;
      } else if (i.getKey().equals("Phone")) {
        continue;
      } else if (i.getKey().equals("Gender")) {
        continue;
      } else if (i.getKey().equals("Grade")) {
        continue;
      } else if (i.getKey().equals("Position")) {
        continue;
      } else if (i.getKey().equals("Office")) {
        continue;
      } else if (i.getKey().equals("Area")) {
        continue;
      } else if (i.getKey().equals("Working Type")) {
        continue;
      } else if (i.getKey().equals("Manager Id")) {
        continue;
      } else if (i.getKey().equals("Employee Type")) {
        continue;
      } else if (i.getKey().equals("Personal Email")) {
        continue;
      } else if (i.getKey().equals("Start Date")) {
        continue;
      } else if (i.getKey().equals("End Date")) {
        continue;
      } else if (i.getKey().equals("Birth Date")) {
        continue;
      } else if (i.getKey().equals("Base Salary")) {
        continue;
      } else if (i.getKey().equals("Salary")) {
        continue;
      } else {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
      }
    }
  }

  private void checkValidFormatExcel(Row row) {
    if (!row.getCell(0).getStringCellValue().equals("Full Name")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(1).getStringCellValue().equals("Role")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(2).getStringCellValue().equals("Phone")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(3).getStringCellValue().equals("Gender")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(4).getStringCellValue().equals("Birth Date")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(5).getStringCellValue().equals("Grade")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(6).getStringCellValue().equals("Position")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(7).getStringCellValue().equals("Office")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(8).getStringCellValue().equals("Area")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(9).getStringCellValue().equals("Working Type")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(10).getStringCellValue().equals("Manager Id")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(11).getStringCellValue().equals("Employee Type")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(12).getStringCellValue().equals("Personal Email")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(13).getStringCellValue().equals("Start Date")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(14).getStringCellValue().equals("End Date")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(15).getStringCellValue().equals("Base Salary")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    } else if (!row.getCell(16).getStringCellValue().equals("Salary")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Wrong format csv for import");
    }
  }
}
