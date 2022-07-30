package com.csproject.hrm.services.impl;

import com.csproject.hrm.common.enums.EArea;
import com.csproject.hrm.common.enums.EOvertime;
import com.csproject.hrm.common.enums.ESalaryMonthly;
import com.csproject.hrm.common.enums.EWorkingType;
import com.csproject.hrm.common.excel.ExcelExportSalaryMonthly;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.RejectSalaryMonthlyRequest;
import com.csproject.hrm.dto.request.UpdateSalaryMonthlyRequest;
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
  @Autowired SalaryCalculator salaryCalculator;
  @Autowired WorkingPlaceRepository workingPlaceRepository;
  @Autowired EmployeeDetailRepository employeeDetailRepository;

  @Override
  public SalaryMonthlyResponseList getAllSalaryMonthlyForPersonal(
      QueryParam queryParam, String employeeId) {
    if (!employeeDetailRepository.checkEmployeeIDIsExists(employeeId)) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Not exist employee " + employeeId);
    }
    return salaryMonthlyRepository.getAllPersonalSalaryMonthly(queryParam, employeeId);
  }

  @Override
  public SalaryMonthlyResponseList getAllSalaryMonthlyForManagement(
      QueryParam queryParam, String employeeId) {
    List<AreaDto> areaDtoList = workingPlaceRepository.getListArea();
    String isEnoughLevel = "False";
    for (AreaDto areaDto : areaDtoList) {
      if (areaDto.getName().equalsIgnoreCase(EArea.HR.name())
          && areaDto.getManager_id().equalsIgnoreCase(employeeId)) {
        isEnoughLevel = "True";
        break;
      }
    }
    return salaryMonthlyRepository.getAllManagementSalaryMonthly(
        queryParam, employeeId, isEnoughLevel);
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
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    if (salaryContractDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Error with contract salary of " + employeeId);
    }
    Double maxPointPerDay = 0D;
    if (salaryContractDto.get().getWorking_type().equalsIgnoreCase(EWorkingType.FULL_TIME.name())) {
      maxPointPerDay = 1D;
    } else if (salaryContractDto
        .get()
        .getWorking_type()
        .equalsIgnoreCase(EWorkingType.PART_TIME.name())) {
      maxPointPerDay = 0.5D;
    }
    Double standardPoint = salaryMonthlyResponse.get().getStandardPoint();
    Double actualWorkingPoint = salaryMonthlyResponse.get().getActualPoint();
    PointResponse pointResponse =
        getPointResponseListByEmployeeId(
            employeeId, maxPointPerDay, actualWorkingPoint, startDate, endDate);

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

    BigDecimal baseSalary = salaryContractDto.get().getBase_salary();
    BigDecimal additionalSalary = salaryContractDto.get().getAdditional_salary();
    EmployeeTaxResponseList employeeTaxResponseList =
        getEmployeeTaxResponseList(employeeId, baseSalary, additionalSalary);

    EmployeeInsuranceResponseList employeeInsuranceResponseList =
        getEmployeeInsuranceResponseList(employeeId, salaryContractDto.get().getBase_salary());

    EmployeeAllowanceResponseList employeeAllowanceResponseList =
        getEmployeeAllowanceResponseList(employeeId);

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
        .salaryStatus(salaryMonthlyResponse.get().getSalaryStatus())
        .pointResponses(pointResponse)
        .otResponseList(otResponseList)
        .bonusSalaryResponseList(bonusSalaryResponseList)
        .deductionSalaryResponseList(deductionSalaryResponseList)
        .advanceSalaryResponseList(advanceSalaryResponseList)
        .employeeAllowanceResponseList(employeeAllowanceResponseList)
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
          salaryMonthlyDtoList.add(upsertSalaryMonthly(startDate, endDate, employeeId));
        });
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void exportPersonalSalaryMonthlyToCsv(
      Writer writer, QueryParam queryParam, List<Long> list, String employeeId) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<SalaryMonthlyResponse> salaryMonthlyResponses =
          salaryMonthlyRepository.getListPersonalSalaryMonthlyToExport(
              queryParam, list, employeeId);
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
  public void exportPersonalSalaryMonthlyExcel(
      HttpServletResponse response, QueryParam queryParam, List<Long> list, String employeeId) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      try {
        List<SalaryMonthlyResponse> salaryMonthlyResponses =
            salaryMonthlyRepository.getListPersonalSalaryMonthlyToExport(
                queryParam, list, employeeId);
        ExcelExportSalaryMonthly excelExportSalaryMonthly =
            new ExcelExportSalaryMonthly(salaryMonthlyResponses);
        excelExportSalaryMonthly.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void exportManagementSalaryMonthlyToCsv(
      Writer writer, QueryParam queryParam, List<Long> list, String employeeId) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      List<SalaryMonthlyResponse> salaryMonthlyResponses =
          salaryMonthlyRepository.getListManagementSalaryMonthlyToExport(
              queryParam, list, employeeId);
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
  public void exportManagementSalaryMonthlyExcel(
      HttpServletResponse response, QueryParam queryParam, List<Long> list, String employeeId) {
    if (list.size() == 0) {
      throw new CustomDataNotFoundException(NO_DATA);
    } else {
      try {
        List<SalaryMonthlyResponse> salaryMonthlyResponses =
            salaryMonthlyRepository.getListManagementSalaryMonthlyToExport(
                queryParam, list, employeeId);
        ExcelExportSalaryMonthly excelExportSalaryMonthly =
            new ExcelExportSalaryMonthly(salaryMonthlyResponses);
        excelExportSalaryMonthly.export(response);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void updateRejectSalaryMonthly(RejectSalaryMonthlyRequest rejectSalaryMonthlyRequest) {
    if (rejectSalaryMonthlyRequest.getSalaryMonthlyId() == null) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, NO_DATA + " " + rejectSalaryMonthlyRequest.getSalaryMonthlyId());
    } else if (!salaryMonthlyRepository.checkExistSalaryMonthly(
        rejectSalaryMonthlyRequest.getSalaryMonthlyId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Not exist with salary id " + rejectSalaryMonthlyRequest.getSalaryMonthlyId());
    }
    salaryMonthlyRepository.updateRejectSalaryMonthly(rejectSalaryMonthlyRequest);
  }

  @Override
  public void updateApproveSalaryMonthly(Long salaryMonthlyId) {
    if (salaryMonthlyId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NO_DATA + " " + salaryMonthlyId);
    } else if (!salaryMonthlyRepository.checkExistSalaryMonthly(salaryMonthlyId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Not exist with salary id " + salaryMonthlyId);
    }
    salaryMonthlyRepository.updateStatusSalaryMonthlyBySalaryMonthlyId(
        salaryMonthlyId, ESalaryMonthly.APPROVED.name());
  }

  @Override
  public void updateCheckedSalaryMonthly(
      UpdateSalaryMonthlyRequest updateSalaryMonthlyRequest, String employeeId) {
    if (updateSalaryMonthlyRequest.getSalaryMonthlyId() == null
        || updateSalaryMonthlyRequest.getSalaryStatus() == null
        || updateSalaryMonthlyRequest.getApproverId() == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILL_NOT_FULL);
    } else if (!salaryMonthlyRepository.checkExistSalaryMonthly(
        updateSalaryMonthlyRequest.getSalaryMonthlyId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Not exist with salary id " + updateSalaryMonthlyRequest.getSalaryMonthlyId());
    } else if (!employeeDetailRepository.checkEmployeeIDIsExists(
        updateSalaryMonthlyRequest.getApproverId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Not exist with employee id " + updateSalaryMonthlyRequest.getApproverId());
    }
    salaryMonthlyRepository.updateCheckedSalaryMonthly(
        updateSalaryMonthlyRequest, Boolean.FALSE, employeeId);
  }

  @Override
  public void updateDeductionSalary(DeductionSalaryDto deductionSalaryDto) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    if (deductionSalaryDto.getDeductionSalaryId() == null
        || deductionSalaryDto.getDate() == null
        || deductionSalaryDto.getDeductionTypeId() == null
        || deductionSalaryDto.getDescription() == null
        || deductionSalaryDto.getValue() == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILL_NOT_FULL);
    } else if (!deductionSalaryRepository.checkExistDeductionSalary(
        deductionSalaryDto.getDeductionSalaryId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Not exist deduction salary with id is " + deductionSalaryDto.getDeductionSalaryId());
    }
    Optional<SalaryMonthlyInfoDto> salaryMonthlyInfoDto =
        deductionSalaryRepository.getSalaryMonthlyInfoByDeductionSalary(
            deductionSalaryDto.getDeductionSalaryId());
    deductionSalaryRepository.updateDeductionSalaryByDeductionSalaryId(deductionSalaryDto);
    salaryMonthlyDtoList.add(
        upsertSalaryMonthly(
            salaryMonthlyInfoDto.get().getStartDate(),
            salaryMonthlyInfoDto.get().getEndDate(),
            salaryMonthlyInfoDto.get().getEmployeeId()));
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void deleteDeductionSalary(Long deductionSalaryId) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    if (deductionSalaryId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NULL_PARAMETER);
    } else if (!deductionSalaryRepository.checkExistDeductionSalary(deductionSalaryId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Not exist deduction salary with id is " + deductionSalaryId);
    }
    Optional<SalaryMonthlyInfoDto> salaryMonthlyInfoDto =
        deductionSalaryRepository.getSalaryMonthlyInfoByDeductionSalary(deductionSalaryId);
    deductionSalaryRepository.deleteDeductionSalaryByDeductionSalaryId(deductionSalaryId);
    salaryMonthlyDtoList.add(
        upsertSalaryMonthly(
            salaryMonthlyInfoDto.get().getStartDate(),
            salaryMonthlyInfoDto.get().getEndDate(),
            salaryMonthlyInfoDto.get().getEmployeeId()));
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void updateBonusSalary(BonusSalaryDto bonusSalaryDto) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    if (bonusSalaryDto.getBonusSalaryId() == null
        || bonusSalaryDto.getDate() == null
        || bonusSalaryDto.getBonusTypeId() == null
        || bonusSalaryDto.getDescription() == null
        || bonusSalaryDto.getValue() == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILL_NOT_FULL);
    } else if (!bonusSalaryRepository.checkExistBonusSalary(bonusSalaryDto.getBonusSalaryId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Not exist bonus salary with id is " + bonusSalaryDto.getBonusSalaryId());
    }
    Optional<SalaryMonthlyInfoDto> salaryMonthlyInfoDto =
        bonusSalaryRepository.getSalaryMonthlyInfoByBonusSalary(bonusSalaryDto.getBonusSalaryId());
    bonusSalaryRepository.updateBonusSalaryByBonusSalaryId(bonusSalaryDto);
    salaryMonthlyDtoList.add(
        upsertSalaryMonthly(
            salaryMonthlyInfoDto.get().getStartDate(),
            salaryMonthlyInfoDto.get().getEndDate(),
            salaryMonthlyInfoDto.get().getEmployeeId()));
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void deleteBonusSalary(Long bonusSalaryId) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    if (bonusSalaryId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NULL_PARAMETER);
    } else if (!bonusSalaryRepository.checkExistBonusSalary(bonusSalaryId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Not exist bonus salary with id is " + bonusSalaryId);
    }
    Optional<SalaryMonthlyInfoDto> salaryMonthlyInfoDto =
        bonusSalaryRepository.getSalaryMonthlyInfoByBonusSalary(bonusSalaryId);
    bonusSalaryRepository.deleteBonusSalaryByBonusSalaryId(bonusSalaryId);
    salaryMonthlyDtoList.add(
        upsertSalaryMonthly(
            salaryMonthlyInfoDto.get().getStartDate(),
            salaryMonthlyInfoDto.get().getEndDate(),
            salaryMonthlyInfoDto.get().getEmployeeId()));
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void updateAdvanceSalary(AdvanceSalaryDto advanceSalaryDto) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    if (advanceSalaryDto.getAdvanceId() == null
        || advanceSalaryDto.getDate() == null
        || advanceSalaryDto.getDescription() == null
        || advanceSalaryDto.getValue() == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, FILL_NOT_FULL);
    } else if (!advanceSalaryRepository.checkExistAdvanceSalary(advanceSalaryDto.getAdvanceId())) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST,
          "Not exist advance salary with id is " + advanceSalaryDto.getAdvanceId());
    }
    Optional<SalaryMonthlyInfoDto> salaryMonthlyInfoDto =
        advanceSalaryRepository.getSalaryMonthlyInfoByAdvanceSalary(
            advanceSalaryDto.getAdvanceId());
    advanceSalaryRepository.updateAdvanceSalaryByAdvanceId(advanceSalaryDto);
    salaryMonthlyDtoList.add(
        upsertSalaryMonthly(
            salaryMonthlyInfoDto.get().getStartDate(),
            salaryMonthlyInfoDto.get().getEndDate(),
            salaryMonthlyInfoDto.get().getEmployeeId()));
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void deleteAdvanceSalary(Long advanceSalaryId) {
    List<SalaryMonthlyDto> salaryMonthlyDtoList = new ArrayList<>();
    if (advanceSalaryId == null) {
      throw new CustomErrorException(HttpStatus.BAD_REQUEST, NULL_PARAMETER);
    } else if (!advanceSalaryRepository.checkExistAdvanceSalary(advanceSalaryId)) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Not exist bonus salary with id is " + advanceSalaryId);
    }
    Optional<SalaryMonthlyInfoDto> salaryMonthlyInfoDto =
        advanceSalaryRepository.getSalaryMonthlyInfoByAdvanceSalary(advanceSalaryId);
    advanceSalaryRepository.deleteAdvanceSalaryByAdvanceId(advanceSalaryId);
    salaryMonthlyDtoList.add(
        upsertSalaryMonthly(
            salaryMonthlyInfoDto.get().getStartDate(),
            salaryMonthlyInfoDto.get().getEndDate(),
            salaryMonthlyInfoDto.get().getEmployeeId()));
    salaryMonthlyRepository.updateSalaryMonthlyByListEmployee(salaryMonthlyDtoList);
  }

  @Override
  public void updateAllSalaryRemind(LocalDate checkDate) {
    List<SalaryMonthlyRemindResponse> salaryMonthlyRemindResponseList =
        salaryMonthlyRepository.getAllSalaryMonthlyToRemind(checkDate);

    salaryMonthlyRemindResponseList.forEach(
        salaryMonthlyRemindResponse -> {
          String createName =
              salaryMonthlyRemindResponse.getFullName()
                  + "-"
                  + salaryMonthlyRemindResponse.getEmployeeId();

          String approveName =
              employeeRepository.getEmployeeNameByEmployeeId(
                  salaryMonthlyRemindResponse.getApprover());

          String approveEmail =
              employeeRepository.getEmployeeEmailByEmployeeId(
                  salaryMonthlyRemindResponse.getApprover());

          generalFunction.sendEmailRemindSalary(
              approveName,
              createName,
              salaryMonthlyRemindResponse.getStartDate().getMonth()
                  + "-"
                  + salaryMonthlyRemindResponse.getStartDate().getYear(),
              salaryMonthlyRemindResponse.getCheckedBy(),
              salaryMonthlyRemindResponse.getSalaryMonthlyId().toString(),
              FROM_EMAIL,
              TO_EMAIL,
              "Remind Request");

          salaryMonthlyRepository.updateAllSalaryMonthlyRemind(
              salaryMonthlyRemindResponse.getSalaryMonthlyId(), Boolean.TRUE);
        });
  }

  @Override
  public List<DeductionTypeDto> getListDeductionTypeDto() {
    return deductionSalaryRepository.getListDeductionTypeDto();
  }

  @Override
  public List<BonusTypeDto> getListBonusTypeDto() {
    return bonusSalaryRepository.getListBonusTypeDto();
  }

  private SalaryMonthlyDto upsertSalaryMonthly(
      LocalDate startDate, LocalDate endDate, String employeeId) {
    Optional<SalaryContractDto> salaryContractDto =
        salaryContractRepository.getSalaryContractByEmployeeId(employeeId);
    Double actualWorkingPoint =
        timekeepingRepository.countPointDayWorkPerMonthByEmployeeId(startDate, endDate, employeeId);

    Long salaryMonthlyId =
        salaryMonthlyRepository.getSalaryMonthlyIdByEmployeeIdAndDate(
            employeeId, startDate, endDate, actualWorkingPoint, ESalaryMonthly.PENDING.name());
    if (salaryContractDto.isEmpty()) {
      throw new CustomErrorException(
          HttpStatus.BAD_REQUEST, "Don't have salary contract of " + employeeId);
    }
    Double maxPointPerDay = 0D;
    if (salaryContractDto.get().getWorking_type().equalsIgnoreCase(EWorkingType.FULL_TIME.name())) {
      maxPointPerDay = 1D;
    } else if (salaryContractDto
        .get()
        .getWorking_type()
        .equalsIgnoreCase(EWorkingType.PART_TIME.name())) {
      maxPointPerDay = 0.5D;
    }
    List<LocalDate> getListHoliday = salaryCalculator.getAllHolidayByYear(startDate);
    List<LocalDate> getListWeekend = salaryCalculator.getAllWeekendByYear(startDate);
    int countHolidayInMonth = 0, countWeekendInMonth = 0;
    for (LocalDate date : getListHoliday) {
      if (date.isAfter(startDate) && date.isBefore(endDate)) {
        countHolidayInMonth++;
      }
    }
    for (LocalDate date : getListWeekend) {
      if (date.isAfter(startDate) && date.isBefore(endDate)) {
        countWeekendInMonth++;
      }
    }
    int standardDayOfWork =
        Integer.parseInt(String.valueOf(DAYS.between(startDate, endDate)))
            + 1
            - countHolidayInMonth
            - countWeekendInMonth;

    Double standardPoint = standardDayOfWork * maxPointPerDay;

    Double totalOTPoint =
        overtimeRepository.sumListOTDetailResponseByEmployeeIdAndDate(
            startDate, endDate, employeeId);
    BigDecimal totalBonus =
        bonusSalaryRepository.sumListBonusMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalDeduction =
        deductionSalaryRepository.sumListDeductionMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal totalAdvance =
        advanceSalaryRepository.sumListAdvanceMonthlyBySalaryMonthlyId(salaryMonthlyId);
    BigDecimal baseSalary = salaryContractDto.get().getBase_salary();
    BigDecimal additionalSalary = salaryContractDto.get().getAdditional_salary();
    BigDecimal totalTax =
        getEmployeeTaxResponseList(employeeId, baseSalary, additionalSalary).getTotal();
    BigDecimal totalInsurance =
        getEmployeeInsuranceResponseList(employeeId, salaryContractDto.get().getBase_salary())
            .getTotal();
    BigDecimal totalAllowance = getEmployeeAllowanceResponseList(employeeId).getTotal();
    BigDecimal salaryPerDay =
        (salaryContractDto
                .get()
                .getAdditional_salary()
                .add(salaryContractDto.get().getBase_salary()))
            .divide(BigDecimal.valueOf(standardDayOfWork), 3, RoundingMode.HALF_UP);
    BigDecimal finalSalary =
        salaryContractDto
            .get()
            .getBase_salary()
            .add(
                salaryPerDay.multiply(
                    BigDecimal.valueOf(actualWorkingPoint != null ? actualWorkingPoint : 0D)))
            .add(
                salaryPerDay.multiply(BigDecimal.valueOf(totalOTPoint != null ? totalOTPoint : 0D)))
            .add(totalBonus != null ? totalBonus : BigDecimal.ZERO)
            .subtract(totalDeduction != null ? totalDeduction : BigDecimal.ZERO)
            .subtract(totalAdvance != null ? totalAdvance : BigDecimal.ZERO)
            .subtract(totalTax != null ? totalTax : BigDecimal.ZERO)
            .subtract(totalInsurance != null ? totalInsurance : BigDecimal.ZERO)
            .add(totalAllowance != null ? totalAllowance : BigDecimal.ZERO);

    return SalaryMonthlyDto.builder()
        .salaryMonthlyId(salaryMonthlyId)
        .standardPoint(standardPoint != null ? standardPoint : 0D)
        .actualPoint(actualWorkingPoint != null ? actualWorkingPoint : 0D)
        .otPoint(totalOTPoint != null ? totalOTPoint : 0D)
        .totalBonus(totalBonus != null ? totalBonus : BigDecimal.ZERO)
        .totalDeduction(totalDeduction != null ? totalDeduction : BigDecimal.ZERO)
        .totalAdvance(totalAdvance != null ? totalAdvance : BigDecimal.ZERO)
        .totalTax(totalTax != null ? totalTax : BigDecimal.ZERO)
        .totalInsurance(totalInsurance != null ? totalInsurance : BigDecimal.ZERO)
        .finalSalary(finalSalary)
        .build();
  }

  private PointResponse getPointResponseListByEmployeeId(
      String employeeId,
      Double maxPointPerDay,
      Double actualWorkingPoint,
      LocalDate startDate,
      LocalDate endDate) {
    int paidLeaveDay =
        timekeepingRepository.countTimePaidLeavePerMonthByEmployeeId(
            startDate, endDate, employeeId);
    int unpaidLeaveDay =
        timekeepingRepository.countTimeDayOffPerMonthByEmployeeId(startDate, endDate, employeeId);

    return new PointResponse(
        actualWorkingPoint - paidLeaveDay * maxPointPerDay,
        unpaidLeaveDay * maxPointPerDay,
        paidLeaveDay * maxPointPerDay,
        actualWorkingPoint);
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
      String employeeId, BigDecimal baseSalary, BigDecimal additionalSalary) {
    BigDecimal monthlySalary = baseSalary.add(additionalSalary);
    BigDecimal totalInsurance = getEmployeeInsuranceResponseList(employeeId, baseSalary).getTotal();
    List<EmployeeTaxResponse> employeeTaxResponses =
        generalFunction.readTaxDataByEmployeeId(employeeId, monthlySalary, totalInsurance);
    BigDecimal totalTax = BigDecimal.ZERO;
    if (employeeTaxResponses.isEmpty()) {
      return new EmployeeTaxResponseList(employeeTaxResponses, totalTax);
    }
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
    if (employeeInsuranceResponses.isEmpty()) {
      return new EmployeeInsuranceResponseList(employeeInsuranceResponses, totalInsurance);
    }
    for (EmployeeInsuranceResponse employeeInsuranceResponse : employeeInsuranceResponses) {
      totalInsurance = totalInsurance.add(employeeInsuranceResponse.getValue());
    }
    return new EmployeeInsuranceResponseList(employeeInsuranceResponses, totalInsurance);
  }

  private EmployeeAllowanceResponseList getEmployeeAllowanceResponseList(String employeeId) {
    List<EmployeeAllowanceResponse> employeeAllowanceResponses =
        generalFunction.readAllowanceData(employeeId);
    BigDecimal totalAllowance = BigDecimal.ZERO;
    if (employeeAllowanceResponses.isEmpty()) {
      return new EmployeeAllowanceResponseList(employeeAllowanceResponses, totalAllowance);
    }
    for (EmployeeAllowanceResponse employeeAllowanceResponse : employeeAllowanceResponses) {
      totalAllowance = totalAllowance.add(employeeAllowanceResponse.getValue());
    }
    return new EmployeeAllowanceResponseList(employeeAllowanceResponses, totalAllowance);
  }
}
