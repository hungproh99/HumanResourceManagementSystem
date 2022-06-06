package com.csproject.hrm.services;

import com.csproject.hrm.common.enums.EWorkStatus;
import com.csproject.hrm.dto.request.HrmPojo;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.exception.CustomParameterConstraintException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.services.impl.HumanManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.csproject.hrm.common.constant.Constants.*;

@Service
public class HumanManagementService implements HumanManagementServiceImpl {
  @Autowired EmployeeRepository employeeRepository;

  @Autowired LoginService loginService;
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

    employeeRepository.insertEmployee(hrmPojo);
  }

  @Override
  public void insertMultiEmployee(List<HrmRequest> hrmRequestList) {
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
          employeeRepository.insertEmployee(hrmPojo);
        });
  }

  private HrmPojo createHrmPojo(HrmRequest hrmRequest) {
    String employeeId = generateIdEmployee(hrmRequest.getFullName());
    String companyEmail = generateEmailEmployee(employeeId);
    String password = passwordEncoder.encode(loginService.generateCommonLangPassword());
    String companyName = "HRM";

    HrmPojo hrmPojo =
        HrmPojo.builder()
            .employeeId(employeeId)
            .companyEmail(companyEmail)
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

    loginService.sendEmail(
        FROM_EMAIL,
        TO_EMAIL,
        SEND_PASSWORD_SUBJECT,
        String.format(SEND_PASSWORD_TEXT, employeeId, password));
    return hrmPojo;
  }

  private String generateEmailEmployee(String id) {
    return id + DOMAIN_EMAIL;
  }

  private String generateIdEmployee(String fullName) {
    String[] splitSpace = fullName.split("\\s+");
    String standForName = splitSpace[splitSpace.length - 1];
    for (int index = 0; index < splitSpace.length - 1; index++) {
      standForName += splitSpace[index].substring(ZERO_NUMBER, ONE_NUMBER);
    }
    int count = employeeRepository.countEmployeeSameStartName(standForName);
    String id = standForName + (count + 1);
    return id;
  }
}
