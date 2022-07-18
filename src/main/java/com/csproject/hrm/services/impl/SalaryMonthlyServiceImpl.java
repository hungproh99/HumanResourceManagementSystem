package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EOvertime;
import com.csproject.hrm.common.enums.EWorkingType;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.SalaryContractDto;
import com.csproject.hrm.dto.dto.WorkingTimeDataDto;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.SalaryMonthlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.csproject.hrm.common.constant.Constants.NOT_EXIST_USER_WITH;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class SalaryMonthlyServiceImpl implements SalaryMonthlyService {

  @Autowired EmployeeRepository employeeRepository;
  @Autowired SalaryMonthlyRepository salaryMonthlyRepository;
  @Autowired SalaryContractRepository salaryContractRepository;
  @Autowired TimekeepingRepository timekeepingRepository;
  @Autowired OvertimeRepository overtimeRepository;
  @Autowired GeneralFunction generalFunction;
  @Autowired BonusSalaryRepository bonusSalaryRepository;
  @Autowired DeductionSalaryRepository deductionSalaryRepository;
  @Autowired AdvanceSalaryRepository advanceSalaryRepository;

  @Override
  public SalaryMonthlyDetailResponse getSalaryMonthlyDetailByEmployeeId(
      String employeeId, LocalDate startDate, LocalDate endDate) {
    Optional<HrmResponse> hrmResponse = employeeRepository.getEmployeeByEmployeeId(employeeId);
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    Long salaryMonthlyId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, endDate);
    if (hrmResponse.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    } else if (salaryContractDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Don't have contract of " + employeeId);
    }

    PointResponse pointResponse =
        getPointResponseListByEmployeeId(
            employeeId, hrmResponse.get().getWorking_name(), startDate, endDate);

    List<String> listOtType = overtimeRepository.getListOvertimeType();
    List<OTResponse> otResponses = new ArrayList<>();
    for (String otType : listOtType) {
      List<OTDetailResponse> listOTDetail =
          overtimeRepository.getListOTDetailResponseByEmployeeIdAndDateAndOtType(
              startDate, endDate, employeeId, EOvertime.getValue(otType));
      Double totalPointByType =
          overtimeRepository.sumListOTDetailResponseByEmployeeIdAndDateAndOtType(
              startDate, endDate, employeeId, EOvertime.getValue(otType));
      otResponses.add(new OTResponse(otType, listOTDetail, totalPointByType));
    }
    Double totalOTPoint =
        overtimeRepository.sumListOTDetailResponseByEmployeeIdAndDate(
            startDate, endDate, employeeId);
    OTResponseList otResponseList = new OTResponseList(otResponses, totalOTPoint);

    List<BonusSalaryResponse> bonusSalaryResponses =
        bonusSalaryRepository.getListBonusMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalBonus =
        bonusSalaryRepository.sumListBonusMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BonusSalaryResponseList bonusSalaryResponseList =
        new BonusSalaryResponseList(bonusSalaryResponses, totalBonus);

    List<DeductionSalaryResponse> deductionSalaryResponses =
        deductionSalaryRepository.getListDeductionMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalDeduction =
        deductionSalaryRepository.sumListDeductionMonthlyBySalaryMonthlyId(salaryMonthlyId);
    DeductionSalaryResponseList deductionSalaryResponseList =
        new DeductionSalaryResponseList(deductionSalaryResponses, totalDeduction);

    List<AdvanceSalaryResponse> advanceSalaryResponses =
        advanceSalaryRepository.getListAdvanceMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalAdvance =
        advanceSalaryRepository.sumListAdvanceMonthlyBySalaryMonthlyId(salaryMonthlyId);
    AdvanceSalaryResponseList advanceSalaryResponseList =
        new AdvanceSalaryResponseList(advanceSalaryResponses, totalAdvance);

    List<EmployeeTaxResponse> employeeTaxResponses =
        generalFunction.readTaxDataByEmployeeId(
            employeeId, salaryContractDto.get().getBase_salary());
    BigDecimal totalTax = BigDecimal.ZERO;
    for (EmployeeTaxResponse employeeTaxResponse : employeeTaxResponses) {
      totalTax = totalTax.add(employeeTaxResponse.getValue());
    }
    EmployeeTaxResponseList employeeTaxResponseList =
        new EmployeeTaxResponseList(employeeTaxResponses, totalTax);

    List<EmployeeInsuranceResponse> employeeInsuranceResponses =
        generalFunction.readInsuranceDataByEmployeeId(
            employeeId, salaryContractDto.get().getBase_salary());
    BigDecimal totalInsurance = BigDecimal.ZERO;
    for (EmployeeInsuranceResponse employeeInsuranceResponse : employeeInsuranceResponses) {
      totalInsurance = totalInsurance.add(employeeInsuranceResponse.getValue());
    }
    EmployeeInsuranceResponseList employeeInsuranceResponseList =
        new EmployeeInsuranceResponseList(employeeInsuranceResponses, totalInsurance);

//    BigDecimal salaryPerDay =
//        salaryContractDto
//            .get()
//            .getAdditional_salary()
//            .divide(BigDecimal.valueOf(standardDayOfWork));

    BigDecimal finalSalary =
        salaryContractDto
            .get()
            .getBase_salary()
            .add(salaryContractDto.get().getAdditional_salary())
//            .add(salaryPerDay.multiply(BigDecimal.valueOf(totalOTPoint)))
            .add(totalBonus)
            .subtract(totalDeduction)
            .subtract(totalAdvance)
            .subtract(totalTax)
            .subtract(totalInsurance);

    return SalaryMonthlyDetailResponse.builder()
        .salary_monthly_id(salaryMonthlyId)
        .employee_id(employeeId)
        .full_name(hrmResponse.get().getFull_name())
        .position(hrmResponse.get().getPosition_name())
        .start_date(startDate)
        .end_date(endDate)
        .base_salary(salaryContractDto.get().getBase_salary())
        .final_salary(finalSalary)
        .pointResponses(pointResponse)
        .otResponseList(otResponseList)
        .bonusSalaryResponseList(bonusSalaryResponseList)
        .deductionSalaryResponseList(deductionSalaryResponseList)
        .advanceSalaryResponseList(advanceSalaryResponseList)
        .employeeInsuranceResponseList(employeeInsuranceResponseList)
        .employeeTaxResponseList(employeeTaxResponseList)
        .build();
  }

  @Override
  public void insertSalaryMonthlyByEmployeeIdList(
      List<String> employeeIdList, LocalDate startDate, LocalDate endDate) {
    employeeIdList.forEach(employeeId -> {});
  }

  private PointResponse getPointResponseListByEmployeeId(
      String employeeId, String workingName, LocalDate startDate, LocalDate endDate) {
    WorkingTimeDataDto workingTimeDataDto = generalFunction.readWorkingTimeData();
    int standardDayOfWork =
        Integer.parseInt(
            String.valueOf(
                DAYS.between(workingTimeDataDto.getStartTime(), workingTimeDataDto.getEndTime())));
    if (standardDayOfWork == 0) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Standard day is 0 of " + employeeId);
    }
    Double standardHourWork = Double.parseDouble(String.valueOf(standardDayOfWork / 60));
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(startDate, endDate, employeeId);
    Double otPoint =
        timekeepingRepository.countPointOTPerMonthByEmployeeId(startDate, endDate, employeeId);
    Double totalPointWork = actualWorkingPoint + otPoint;

    int paidLeaveDay =
        timekeepingRepository.countTimePaidLeavePerMonthByEmployeeId(
            startDate, endDate, employeeId);
    int unpaidLeaveDay =
        timekeepingRepository.countTimeDayOffPerMonthByEmployeeId(startDate, endDate, employeeId);
    Double actualWorkingHour = 0D, paidLeaveHour = 0D, unpaidLeaveHour = 0D;
    if (workingName.equalsIgnoreCase(EWorkingType.FULL_TIME.name())) {
      actualWorkingHour = actualWorkingPoint * standardHourWork;
      paidLeaveHour = paidLeaveDay * 1D;
      unpaidLeaveHour = unpaidLeaveDay * 1D;
    } else if (workingName.equalsIgnoreCase(EWorkingType.PART_TIME.name())) {
      actualWorkingHour = actualWorkingPoint * standardHourWork;
      paidLeaveHour = paidLeaveDay * 0.5D;
      unpaidLeaveHour = unpaidLeaveDay * 0.5D;
    }
    return new PointResponse(actualWorkingHour, paidLeaveHour, unpaidLeaveHour, totalPointWork);
  }
}
