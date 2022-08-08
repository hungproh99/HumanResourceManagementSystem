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
import com.csproject.hrm.exception.CustomParameterConstraintException;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "role not exist");
    } else if (!hrmRequest.getGender().equalsIgnoreCase("Male")
        && !hrmRequest.getGender().equalsIgnoreCase("Female")) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "gender must be Female/Male");
    } else if (!areaRepository.existsById(hrmRequest.getArea())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "area not exist");
    } else if (!officeRepository.existsById(hrmRequest.getOffice())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "office not exist");
    } else if (!gradeRepository.existsById(hrmRequest.getGrade())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "grade not exist");
    } else if (!jobRepository.existsById(hrmRequest.getPosition())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "position not exist");
    } else if (!workingTypeRepository.existsById(hrmRequest.getWorkingType())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "workingType not exist");
    } else if (!employeeRepository.existsById(hrmRequest.getManagerId())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "managerId not exist");
    } else if (!employeeTypeRepository.existsById(hrmRequest.getEmployeeType())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "employeeType not exist");
    } else if (hrmRequest.getBirthDate().plus(18, ChronoUnit.YEARS).isBefore(LocalDate.now())) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "birthDate must be enough 18 age");
    }
    HrmPojo hrmPojo = createHrmPojo(hrmRequest);
    String employeeId = generalFunction.generateIdEmployee(hrmRequest.getFullName(), 0);
    String companyEmail = generalFunction.generateEmailEmployee(employeeId);
    hrmPojo.setEmployeeId(employeeId);
    hrmPojo.setCompanyEmail(companyEmail);

    generalFunction.sendEmailForNewEmployee(
        List.of(hrmPojo), FROM_EMAIL, TO_EMAIL, SEND_PASSWORD_SUBJECT);
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
  public List<RoleDto> getListRoleType(boolean isAdmin) {
    List<RoleDto> roleDto = employeeRepository.getListRoleType(isAdmin);
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
              HttpStatus.BAD_REQUEST, "employeeId \"" + employeeId + "\" not exist");
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

          if (!roleRepository.existsById(role)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "role not exist");
          } else if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "gender must be Female/Male");
          } else if (!areaRepository.existsById(area)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "area not exist");
          } else if (!officeRepository.existsById(office)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "office not exist");
          } else if (!gradeRepository.existsById(grade)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "grade not exist");
          } else if (!jobRepository.existsById(position)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "position not exist");
          } else if (!workingTypeRepository.existsById(workingType)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "workingType not exist");
          } else if (!employeeRepository.existsById(managerId)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "managerId not exist");
          } else if (!employeeTypeRepository.existsById(employeeType)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "employeeType not exist");
          } else if (!personalEmail.matches(EMAIL_VALIDATION)) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "personalEmail not valid");
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
                  .build());
        } catch (NumberFormatException e) {
          throw new CustomErrorException(HttpStatus.BAD_REQUEST, WRONG_NUMBER_FORMAT);
        }
      }
      insertMultiEmployee(hrmRequestList);
    } catch (IOException e) {
      throw new RuntimeException(e);
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
              HttpStatus.BAD_REQUEST, "employeeId \"" + employeeId + "\" not exist");
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
    for (int rowIndex = 0; rowIndex < getNumberOfNonEmptyCells(sheet, 0) - 1; rowIndex++) {
      Row row = sheet.getRow(rowIndex);
      if (rowIndex == 0) {
        continue;
      }
      try {
        String fullName = row.getCell(0).toString();
        Long role = ERole.getValue(getValue(row.getCell(1)).toString());
        String phone = "0" + (int) row.getCell(2).getNumericCellValue();
        String gender = row.getCell(3).toString();
        LocalDate birthDate = row.getCell(4).getLocalDateTimeCellValue().toLocalDate();
        Long grade = EGradeType.getValue(getValue(row.getCell(5)).toString());
        Long position = EJob.getValue(getValue(row.getCell(6)).toString());
        Long office = EOffice.getValue(getValue(row.getCell(7)).toString());
        Long area = EArea.getValue(getValue(row.getCell(8)).toString());
        Long workingType = EWorkingType.getValue(getValue(row.getCell(9)).toString());
        String managerId = row.getCell(10).toString();
        Long employeeType = EEmployeeType.getValue(getValue(row.getCell(11)).toString());
        String personalEmail = row.getCell(12).toString();
        LocalDate startDate = row.getCell(13).getLocalDateTimeCellValue().toLocalDate();
        LocalDate endDate = row.getCell(14).getLocalDateTimeCellValue().toLocalDate();
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
                .build());
      } catch (NumberFormatException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, WRONG_NUMBER_FORMAT);
      }
    }
    insertMultiEmployee(hrmRequestList);
  }

  @Override
  public HrmResponseList getListHumanResourceOfManager(QueryParam queryParam, String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "employeeId \"" + employeeId + "\" not exist");
    }
    return employeeRepository.findAllEmployeeOfManager(queryParam, employeeId);
  }

  @Override
  public List<EmployeeNameAndID> getListManagerHigherOfArea(String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "employeeId \"" + employeeId + "\" not exist");
    }
    int level = employeeRepository.getLevelOfEmployee(employeeId);
    return employeeRepository.getListManagerHigherOfArea(employeeId, level);
  }

  @Override
  public List<EmployeeNameAndID> getListManagerLowerOfArea(String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "employeeId \"" + employeeId + "\" not exist");
    }
    int level = employeeRepository.getLevelOfEmployee(employeeId);
    return employeeRepository.getListManagerLowerOfArea(employeeId, level);
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
    //    generalFunction.sendEmailForNewEmployee(hrmPojos, FROM_EMAIL, TO_EMAIL,
    // SEND_PASSWORD_SUBJECT);
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
      level = employeeRepository.getLevelOfEmployee(hrmRequest.getManagerId()) - 1;
    } else if (hrmRequest.getRole().equals(ERole.getValue(ERole.ROLE_USER.name()))) {
      level = -1;
    }

    HrmPojo hrmPojo =
        HrmPojo.builder()
            .password(password)
            .workStatus(true)
            .contractStatus(true)
            .placeStatus(true)
            .level(level)
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

  private Object getValue(Cell cell) {
    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue();
      case NUMERIC:
        return String.valueOf((int) cell.getNumericCellValue());
      case BOOLEAN:
        return cell.getBooleanCellValue();
      case ERROR:
        return cell.getErrorCellValue();
      case FORMULA:
        return cell.getCellFormula();
      case BLANK:
      case _NONE:
        return null;
      default:
        break;
    }
    return null;
  }
}
