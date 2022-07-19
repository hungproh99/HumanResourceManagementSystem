package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EOvertime;
import com.csproject.hrm.common.enums.ESalaryMonthly;
import com.csproject.hrm.common.enums.EWorkingType;
import com.csproject.hrm.common.excel.ExcelExportSalaryMonthly;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.dto.dto.SalaryContractDto;
import com.csproject.hrm.dto.dto.SalaryMonthlyDto;
import com.csproject.hrm.dto.dto.WorkingTimeDataDto;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.SalaryMonthlyService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.csproject.hrm.common.constant.Constants.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

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
      employeeIdList.add(employeeId);
    } else {
      employeeIdList.add(employeeId);
    }
    return salaryMonthlyRepository.getAllSalaryMonthly(queryParam, employeeIdList, isManager);
  }

  @Override
  public SalaryMonthlyDetailResponse getSalaryMonthlyDetailBySalaryMonthlyId(Long salaryMonthlyId) {
    Optional<SalaryMonthlyResponse> salaryMonthlyResponse =
        salaryMonthlyRepository.getSalaryMonthlyBySalaryId(salaryMonthlyId);
    if (salaryMonthlyResponse.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + salaryMonthlyId);
    }
    String employeeId = salaryMonthlyResponse.get().getEmployeeId();
    LocalDate startDate = salaryMonthlyResponse.get().getStartDate();
    LocalDate endDate = salaryMonthlyResponse.get().getEndDate();
    Optional<HrmResponse> hrmResponse = employeeRepository.getEmployeeByEmployeeId(employeeId);
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    if (hrmResponse.isEmpty()) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
    } else if (salaryContractDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Don't have contract of " + employeeId);
    }
    Double standardPoint = salaryMonthlyResponse.get().getStandardPoint();

    PointResponse pointResponse =
        getPointResponseListByEmployeeId(
            employeeId,
            hrmResponse.get().getWorking_name(),
            startDate,
            endDate);

    OTResponseList otResponseList =
        new OTResponseList(
            getOtResponseList(employeeId, startDate, endDate),
            salaryMonthlyResponse.get().getOtPoint());

    BonusSalaryResponseList bonusSalaryResponseList =
        new BonusSalaryResponseList(
            getBonusSalaryResponseList(salaryMonthlyId),
            salaryMonthlyResponse.get().getTotalBonus());

    DeductionSalaryResponseList deductionSalaryResponseList =
        new DeductionSalaryResponseList(
            getDeductionSalaryResponseList(salaryMonthlyId),
            salaryMonthlyResponse.get().getTotalDeduction());

    AdvanceSalaryResponseList advanceSalaryResponseList =
        new AdvanceSalaryResponseList(
            getAdvanceSalaryResponseList(salaryMonthlyId),
            salaryMonthlyResponse.get().getTotalAdvance());

    EmployeeTaxResponseList employeeTaxResponseList =
        getEmployeeTaxResponseList(employeeId, salaryContractDto.get().getBase_salary());

    EmployeeInsuranceResponseList employeeInsuranceResponseList =
        getEmployeeInsuranceResponseList(employeeId, salaryContractDto.get().getBase_salary());

    return SalaryMonthlyDetailResponse.builder()
        .salary_monthly_id(salaryMonthlyId)
        .employee_id(employeeId)
        .full_name(salaryMonthlyResponse.get().getFullName())
        .position(salaryMonthlyResponse.get().getPosition())
        .start_date(startDate)
        .end_date(endDate)
        .base_salary(salaryContractDto.get().getBase_salary())
        .final_salary(salaryMonthlyResponse.get().getFinalSalary())
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
  public void upsertSalaryMonthlyByEmployeeIdList(LocalDate startDate, LocalDate endDate) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    List<String> employeeIdList = employeeRepository.getAllEmployeeIdActive();
    employeeIdList.forEach(
        employeeId -> {
          Optional<HrmResponse> hrmResponse =
              employeeRepository.getEmployeeByEmployeeId(employeeId);
          Optional<SalaryContractDto> salaryContractDto =
              salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
          Long salaryMonthlyId =
              salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
                  employeeId, startDate, endDate, ESalaryMonthly.PENDING.name());
          if (hrmResponse.isEmpty()) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, NOT_EXIST_USER_WITH + employeeId);
          } else if (salaryContractDto.isEmpty()) {
            throw new CustomErrorException(
                HttpStatus.BAD_REQUEST, "Don't have salary contract of " + employeeId);
          }
          int standardDayOfWork =
              Integer.parseInt(String.valueOf(DAYS.between(startDate, endDate))) + 1;
          Double standardPoint =
              getStandardPointOfWork(standardDayOfWork, hrmResponse.get().getWorking_name());
          Double actualWorkingPoint =
              getPointResponseListByEmployeeId(
                      employeeId,
                      hrmResponse.get().getWorking_name(),
                      startDate,
                      endDate)
                  .getTotalWorkPoint();
          Double totalOTPoint =
              overtimeRepository.sumListOTDetailResponseByEmployeeIdAndDate(
                  startDate, endDate, employeeId);
          BigDecimal totalBonus =
              bonusSalaryRepository.sumListBonusMonthlyBySalaryMonthlyId(salaryMonthlyId);
          BigDecimal totalDeduction =
              deductionSalaryRepository.sumListDeductionMonthlyBySalaryMonthlyId(salaryMonthlyId);
          BigDecimal totalAdvance =
              advanceSalaryRepository.sumListAdvanceMonthlyBySalaryMonthlyId(salaryMonthlyId);
          BigDecimal totalTax =
              getEmployeeTaxResponseList(employeeId, salaryContractDto.get().getBase_salary())
                  .getTotal();
          BigDecimal totalInsurance =
              getEmployeeInsuranceResponseList(employeeId, salaryContractDto.get().getBase_salary())
                  .getTotal();
          BigDecimal salaryPerDay =
              salaryContractDto
                  .get()
                  .getAdditional_salary()
                  .divide(BigDecimal.valueOf(standardDayOfWork), 3, RoundingMode.HALF_UP);
          BigDecimal finalSalary =
              salaryContractDto
                  .get()
                  .getBase_salary()
                  .add(salaryContractDto.get().getAdditional_salary())
                  .add(
                      salaryPerDay.multiply(
                          BigDecimal.valueOf(totalOTPoint != null ? totalOTPoint : 0D)))
                  .add(totalBonus != null ? totalBonus : BigDecimal.ZERO)
                  .subtract(totalDeduction != null ? totalDeduction : BigDecimal.ZERO)
                  .subtract(totalAdvance != null ? totalAdvance : BigDecimal.ZERO)
                  .subtract(totalTax != null ? totalTax : BigDecimal.ZERO)
                  .subtract(totalInsurance != null ? totalInsurance : BigDecimal.ZERO);

          salaryMonthlyDtoList.add(
              SalaryMonthlyDto.builder()
                  .salaryMonthlyId(salaryMonthlyId)
                  .standardPoint(standardPoint)
                  .actualPoint(actualWorkingPoint != null ? actualWorkingPoint : 0D)
                  .otPoint(totalOTPoint != null ? totalOTPoint : 0D)
                  .totalDeduction(totalBonus != null ? totalBonus : BigDecimal.ZERO)
                  .totalBonus(totalDeduction != null ? totalDeduction : BigDecimal.ZERO)
                  .totalInsurance(totalAdvance != null ? totalAdvance : BigDecimal.ZERO)
                  .totalTax(totalTax != null ? totalTax : BigDecimal.ZERO)
                  .totalAdvance(totalInsurance != null ? totalInsurance : BigDecimal.ZERO)
                  .finalSalary(finalSalary)
                  .build());
        });
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void exportSalaryMonthlyToCsv(Writer writer, QueryParam queryParam, List<Long> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<SalaryMonthlyResponse> salaryMonthlyResponses =
          salaryMonthlyRepository.getListSalaryMonthlyToExport(queryParam, list);
      try (CSVPrinter csvPrinter =
          new CSVPrinter(
              writer,
              CSVFormat.DEFAULT.withHeader(
                  "Employee Id",
                  "Full Name",
                  "Position",
                  "Standard Point",
                  "Actual Point",
                  "Overtime Point",
                  "Total Deduction",
                  "Total Bonus",
                  "Total Insurance",
                  "Total Tax",
                  "Total Advance",
                  "Final Salary",
                  "Start Date",
                  "End Date",
                  "Salary Status"))) {

        for (SalaryMonthlyResponse salaryMonthlyResponse : salaryMonthlyResponses) {
          csvPrinter.printRecord(
              salaryMonthlyResponse.getEmployeeId(),
              salaryMonthlyResponse.getFullName(),
              salaryMonthlyResponse.getPosition(),
              salaryMonthlyResponse.getStandardPoint(),
              salaryMonthlyResponse.getActualPoint(),
              salaryMonthlyResponse.getOtPoint(),
              salaryMonthlyResponse.getTotalDeduction(),
              salaryMonthlyResponse.getTotalBonus(),
              salaryMonthlyResponse.getTotalInsurance(),
              salaryMonthlyResponse.getTotalTax(),
              salaryMonthlyResponse.getTotalAdvance(),
              salaryMonthlyResponse.getFinalSalary(),
              salaryMonthlyResponse.getStartDate(),
              salaryMonthlyResponse.getEndDate(),
              salaryMonthlyResponse.getSalaryStatus());
        }
        csvPrinter.flush();
      } catch (IOException e) {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, CAN_NOT_WRITE_CSV);
      }
    }
  }

  @Override
  public void exportSalaryMonthlyExcel(
      HttpServletResponse response, QueryParam queryParam, List<Long> list) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      try {
        List<SalaryMonthlyResponse> salaryMonthlyResponses =
            salaryMonthlyRepository.getListSalaryMonthlyToExport(queryParam, list);
        ExcelExportSalaryMonthly excelExportSalaryMonthly =
            new ExcelExportSalaryMonthly(salaryMonthlyResponses);
        excelExportSalaryMonthly.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private Double getStandardPointOfWork(int standardDayOfWork, String workingName) {
    if (workingName.equalsIgnoreCase(EWorkingType.getLabel(EWorkingType.FULL_TIME.name()))) {
      return standardDayOfWork * 1D;
    } else if (workingName.equalsIgnoreCase(EWorkingType.getLabel(EWorkingType.PART_TIME.name()))) {
      return standardDayOfWork * 0.5D;
    }
    return 0D;
  }

  private PointResponse getPointResponseListByEmployeeId(
      String employeeId,
      String workingName,
      LocalDate startDate,
      LocalDate endDate) {
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(startDate, endDate, employeeId)
                != null
            ? timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(
                startDate, endDate, employeeId)
            : 0D;

    int paidLeaveDay =
        timekeepingRepository.countTimePaidLeavePerMonthByEmployeeId(
            startDate, endDate, employeeId);
    int unpaidLeaveDay =
        timekeepingRepository.countTimeDayOffPerMonthByEmployeeId(startDate, endDate, employeeId);
    Double actualWorkingHour = 0D, paidLeaveHour = 0D, unpaidLeaveHour = 0D;
    if (workingName.equalsIgnoreCase(EWorkingType.getLabel(EWorkingType.FULL_TIME.name()))) {
      actualWorkingHour = actualWorkingPoint;
      paidLeaveHour = paidLeaveDay * 1D;
      unpaidLeaveHour = unpaidLeaveDay * 1D;
    } else if (workingName.equalsIgnoreCase(EWorkingType.getLabel(EWorkingType.PART_TIME.name()))) {
      actualWorkingHour = actualWorkingPoint;
      paidLeaveHour = paidLeaveDay * 0.5D;
      unpaidLeaveHour = unpaidLeaveDay * 0.5D;
    }
    return new PointResponse(actualWorkingHour, paidLeaveHour, unpaidLeaveHour, actualWorkingPoint);
  }

  private List<OTResponse> getOtResponseList(
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
    return otResponses;
  }

  private List<BonusSalaryResponse> getBonusSalaryResponseList(Long salaryMonthlyId) {
    return bonusSalaryRepository.getListBonusMonthlyBySalaryMonthlyId(salaryMonthlyId);
  }

  private List<DeductionSalaryResponse> getDeductionSalaryResponseList(Long salaryMonthlyId) {
    return deductionSalaryRepository.getListDeductionMonthlyBySalaryMonthlyId(salaryMonthlyId);
  }

  private List<AdvanceSalaryResponse> getAdvanceSalaryResponseList(Long salaryMonthlyId) {
    return advanceSalaryRepository.getListAdvanceMonthlyBySalaryMonthlyId(salaryMonthlyId);
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
