package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EWorkingType;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.SalaryContractDto;
import com.csproject.hrm.dto.dto.WorkingTimeDataDto;
import com.csproject.hrm.dto.response.HrmResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.repositories.SalaryContractRepository;
import com.csproject.hrm.repositories.SalaryMonthlyRepository;
import com.csproject.hrm.repositories.TimekeepingRepository;
import com.csproject.hrm.services.SalaryMonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.csproject.hrm.common.constant.Constants.NOT_EXIST_USER_WITH;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class SalaryMonthlyServiceImpl implements SalaryMonthlyService {

  @Autowired EmployeeRepository employeeRepository;
  @Autowired SalaryMonthlyRepository salaryMonthlyRepository;
  @Autowired SalaryContractRepository salaryContractRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired GeneralFunction generalFunction;

  @Override
  public SalaryMonthlyDetailResponse getSalaryMonthlyDetailByEmployeeId(
      String employeeId, LocalDate startDate, LocalDate endDate) {

    return null;
  }

  @Override
  public void insertSalaryMonthlyByEmployeeIdList(
      List<String> employeeIdList, LocalDate startDate, LocalDate endDate) {}

  private void insertSalaryMonthlyByEmployeeId(
      String employeeId, LocalDate startDate, LocalDate endDate) {
    Optional<HrmResponse> hrmResponse = employeeRepository.getEmployeeByEmployeeId(employeeId);
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    if (hrmResponse.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    } else if (salaryContractDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Don't have contract of " + employeeId);
    }
    Long salaryContractId =
        salaryMonthlyRepository.getSalaryIdByEmployeeIdAndDate(employeeId, startDate, endDate);

    WorkingTimeDataDto workingTimeDataDto = generalFunction.readWorkingTimeData();
    Double standardHourWork =
        Double.parseDouble(
            String.valueOf(
                MINUTES.between(workingTimeDataDto.getStartTime(), workingTimeDataDto.getEndTime())
                    / 60));
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(startDate, endDate, employeeId);
    int paidLeaveDay =
        timekeepingRepository.countTimePaidLeavePerMonthByEmployeeId(
            startDate, endDate, employeeId);
    int unpaidLeaveDay =
        timekeepingRepository.countTimeDayOffPerMonthByEmployeeId(startDate, endDate, employeeId);
    if (hrmResponse.get().getWorking_name().equalsIgnoreCase(EWorkingType.FULL_TIME.name())) {
      
    }
  }
}
