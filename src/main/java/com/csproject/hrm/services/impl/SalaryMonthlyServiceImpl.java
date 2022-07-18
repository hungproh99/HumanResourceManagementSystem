package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EOvertime;
import com.csproject.hrm.common.enums.ESalaryMonthly;
import com.csproject.hrm.common.enums.EWorkingType;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.SalaryContractDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyDto;
import com.csproject.hrm.dto.dto.WorkingTimeDataDto;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
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
  public SalaryMonthlyResponseList getAllSalaryMonthly(
      QueryParam queryParam, String employeeId, boolean isManager) {
    List<String> employeeIdList = new ArrayList<>();
    if (isManager) {
      HrmResponseList hrmResponseList =
          employeeRepository.findAllEmployeeOfManager(QueryParam.defaultParam(), employeeId);
      for (HrmResponse hrmResponse : hrmResponseList.getHrmResponse()) {
        employeeIdList.add(hrmResponse.getEmployee_id());
      }
    } else {
      employeeIdList.add(employeeId);
    }
    return salaryMonthlyRepository.getAllSalaryMonthly(queryParam, employeeIdList);
  }

  @Override
  public SalaryMonthlyDetailResponse getSalaryMonthlyDetailByEmployeeId(
      String employeeId, LocalDate startDate, LocalDate endDate) {
    Optional<HrmResponse> hrmResponse = employeeRepository.getEmployeeByEmployeeId(employeeId);
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    Long salaryMonthlyId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, endDate, ESalaryMonthly.PENDING.name());
    if (hrmResponse.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    } else if (salaryContractDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Don't have contract of " + employeeId);
    }
    WorkingTimeDataDto workingTimeDataDto = generalFunction.readWorkingTimeData();
    int standardDayOfWork =
        Integer.parseInt(
            String.valueOf(
                DAYS.between(workingTimeDataDto.getStartTime(), workingTimeDataDto.getEndTime())));

    Double standardPoint =
        getStandardPointOfWork(standardDayOfWork, hrmResponse.get().getWorking_name());

    PointResponse pointResponse =
        getPointResponseListByEmployeeId(
            employeeId, standardDayOfWork, hrmResponse.get().getWorking_name(), startDate, endDate);

    OTResponseList otResponseList = getOtResponseList(employeeId, startDate, endDate);

    BonusSalaryResponseList bonusSalaryResponseList = getBonusSalaryResponseList(salaryMonthlyId);

    DeductionSalaryResponseList deductionSalaryResponseList =
        getDeductionSalaryResponseList(salaryMonthlyId);

    AdvanceSalaryResponseList advanceSalaryResponseList =
        getAdvanceSalaryResponseList(salaryMonthlyId);

    EmployeeTaxResponseList employeeTaxResponseList =
        getEmployeeTaxResponseList(employeeId, salaryContractDto.get().getBase_salary());

    EmployeeInsuranceResponseList employeeInsuranceResponseList =
        getEmployeeInsuranceResponseList(employeeId, salaryContractDto.get().getBase_salary());

    BigDecimal salaryPerDay =
        salaryContractDto
            .get()
            .getAdditional_salary()
            .divide(BigDecimal.valueOf(standardDayOfWork));

    BigDecimal finalSalary =
        salaryContractDto
            .get()
            .getBase_salary()
            .add(salaryContractDto.get().getAdditional_salary())
            .add(salaryPerDay.multiply(BigDecimal.valueOf(otResponseList.getTotalOTPoint())))
            .add(bonusSalaryResponseList.getTotal())
            .subtract(deductionSalaryResponseList.getTotal())
            .subtract(advanceSalaryResponseList.getTotal())
            .subtract(employeeTaxResponseList.getTotal())
            .subtract(employeeInsuranceResponseList.getTotal());

    return SalaryMonthlyDetailResponse.builder()
        .salary_monthly_id(salaryMonthlyId)
        .employee_id(employeeId)
        .full_name(hrmResponse.get().getFull_name())
        .position(hrmResponse.get().getPosition_name())
        .start_date(startDate)
        .end_date(endDate)
        .base_salary(salaryContractDto.get().getBase_salary())
        .final_salary(finalSalary)
        .standardPoint(standardPoint)
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
  public void upsertSalaryMonthlyByEmployeeIdList(
      List<String> employeeIdList, LocalDate startDate, LocalDate endDate) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    employeeIdList.forEach(
        employeeId -> {
          SalaryMonthlyDetailResponse salaryMonthlyDetailResponse =
              getSalaryMonthlyDetailByEmployeeId(employeeId, startDate, endDate);

          salaryMonthlyDtoList.add(
              SalaryMonthlyDto.builder()
                  .salaryMonthlyId(salaryMonthlyDetailResponse.getSalary_monthly_id())
                  .standardPoint(salaryMonthlyDetailResponse.getStandardPoint())
                  .actualPoint(salaryMonthlyDetailResponse.getPointResponses().getTotalWorkPoint())
                  .otPoint(salaryMonthlyDetailResponse.getOtResponseList().getTotalOTPoint())
                  .totalDeduction(
                      salaryMonthlyDetailResponse.getDeductionSalaryResponseList().getTotal())
                  .totalBonus(salaryMonthlyDetailResponse.getBonusSalaryResponseList().getTotal())
                  .totalInsurance(
                      salaryMonthlyDetailResponse.getEmployeeInsuranceResponseList().getTotal())
                  .totalTax(salaryMonthlyDetailResponse.getEmployeeTaxResponseList().getTotal())
                  .totalAdvance(
                      salaryMonthlyDetailResponse.getAdvanceSalaryResponseList().getTotal())
                  .finalSalary(salaryMonthlyDetailResponse.getFinal_salary())
                  .build());
        });
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  private Double getStandardPointOfWork(int standardDayOfWork, String workingName) {
    if (workingName.equalsIgnoreCase(EWorkingType.FULL_TIME.name())) {
      return standardDayOfWork * 1D;
    } else if (workingName.equalsIgnoreCase(EWorkingType.PART_TIME.name())) {
      return standardDayOfWork * 0.5D;
    }
    return 0D;
  }

  private PointResponse getPointResponseListByEmployeeId(
      String employeeId,
      int standardDayOfWork,
      String workingName,
      LocalDate startDate,
      LocalDate endDate) {

    if (standardDayOfWork == 0) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Standard day is 0 of " + employeeId);
    }
    Double standardHourWork = Double.parseDouble(String.valueOf(standardDayOfWork / 60));
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(startDate, endDate, employeeId);

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
    return new PointResponse(actualWorkingHour, paidLeaveHour, unpaidLeaveHour, actualWorkingPoint);
  }

  private OTResponseList getOtResponseList(
      String employeeId, LocalDate startDate, LocalDate endDate) {
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
    return new OTResponseList(otResponses, totalOTPoint);
  }

  private BonusSalaryResponseList getBonusSalaryResponseList(Long salaryMonthlyId) {
    List<BonusSalaryResponse> bonusSalaryResponses =
        bonusSalaryRepository.getListBonusMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalBonus =
        bonusSalaryRepository.sumListBonusMonthlyBySalaryMonthlyId(salaryMonthlyId);
    return new BonusSalaryResponseList(bonusSalaryResponses, totalBonus);
  }

  private DeductionSalaryResponseList getDeductionSalaryResponseList(Long salaryMonthlyId) {
    List<DeductionSalaryResponse> deductionSalaryResponses =
        deductionSalaryRepository.getListDeductionMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalDeduction =
        deductionSalaryRepository.sumListDeductionMonthlyBySalaryMonthlyId(salaryMonthlyId);
    return new DeductionSalaryResponseList(deductionSalaryResponses, totalDeduction);
  }

  private AdvanceSalaryResponseList getAdvanceSalaryResponseList(Long salaryMonthlyId) {
    List<AdvanceSalaryResponse> advanceSalaryResponses =
        advanceSalaryRepository.getListAdvanceMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalAdvance =
        advanceSalaryRepository.sumListAdvanceMonthlyBySalaryMonthlyId(salaryMonthlyId);
    return new AdvanceSalaryResponseList(advanceSalaryResponses, totalAdvance);
  }

  private EmployeeTaxResponseList getEmployeeTaxResponseList(
      String employeeId, BigDecimal baseSalary) {
    List<EmployeeTaxResponse> employeeTaxResponses =
        generalFunction.readTaxDataByEmployeeId(employeeId, baseSalary);
    BigDecimal totalTax = BigDecimal.ZERO;
    for (EmployeeTaxResponse employeeTaxResponse : employeeTaxResponses) {
      totalTax = totalTax.add(employeeTaxResponse.getValue());
    }
    return new EmployeeTaxResponseList(employeeTaxResponses, totalTax);
  }

  private EmployeeInsuranceResponseList getEmployeeInsuranceResponseList(
      String employeeId, BigDecimal baseSalary) {
    List<EmployeeInsuranceResponse> employeeInsuranceResponses =
        generalFunction.readInsuranceDataByEmployeeId(employeeId, baseSalary);
    BigDecimal totalInsurance = BigDecimal.ZERO;
    for (EmployeeInsuranceResponse employeeInsuranceResponse : employeeInsuranceResponses) {
      totalInsurance = totalInsurance.add(employeeInsuranceResponse.getValue());
    }
    return new EmployeeInsuranceResponseList(employeeInsuranceResponses, totalInsurance);
  }
}
