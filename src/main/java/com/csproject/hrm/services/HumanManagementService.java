package com.csproject.hrm.services;

import com.csproject.hrm.common.enums.EWorkStatus;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.request.UpdateHrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.ContractRepository;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.impl.HumanManagementServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class HumanManagementService implements HumanManagementServiceImpl {
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
        || hrmRequest.getEmployeeType() == null) {
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
  public void insertMultiEmployee(List<HrmRequest> hrmRequestList) {
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
              || hrmRequest.getEmployeeType() == null) {
            throw new CustomParameterConstraintException(FILL_NOT_FULL);
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
  public List<JobDto> getListJob() {
    return contractRepository.getListJob();
  }

  @Override
  public void updateEmployeeById(UpdateHrmRequest updateHrmRequest, String employeeId) {
    employeeRepository.updateEmployeeById(updateHrmRequest, employeeId);
  }

  @Override
  public void exportEmployeeToCsv(Writer writer, QueryParam queryParam, List<String> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<HrmResponse> hrmResponses = employeeRepository.findEmployeeByListId(queryParam, list);
      try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
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
      } catch (IOException e) {
      }
    }
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
