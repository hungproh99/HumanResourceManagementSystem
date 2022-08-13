package com.csproject.hrm.services;

import com.csproject.hrm.common.enums.ERequestStatus;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.entities.Employee;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.repositories.*;
import com.csproject.hrm.services.impl.ApplicationsRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.csproject.hrm.common.sample.DataSample.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationRequestServiceTest {

  @Autowired @Mock EmployeeRepository employeeRepository;
  @Autowired @Mock ApplicationsRequestRepository applicationsRequestRepository;
  @Autowired @Mock EmployeeDetailRepository employeeDetailRepository;
  @Autowired @Mock TimekeepingRepository timekeepingRepository;
  @Autowired @Mock WorkingPlaceRepository workingPlaceRepository;
  @Autowired @Mock SalaryContractRepository salaryContractRepository;
  @Autowired @Mock SalaryMonthlyRepository salaryMonthlyRepository;
  @Autowired @Mock BonusSalaryRepository bonusSalaryRepository;
  @Autowired @Mock DeductionSalaryRepository deductionSalaryRepository;
  @Autowired @Mock AdvanceSalaryRepository advanceSalaryRepository;
  @Autowired @Mock WorkingContractRepository workingContractRepository;
  @Autowired @Mock GeneralFunction generalFunction;
  @Autowired @Mock SalaryCalculator salaryCalculator;
  @Autowired @Mock ChartRepository chartRepository;
  @Autowired @Mock RequestStatusRepository requestStatusRepository;
  @Autowired @Mock RequestNameRepository requestNameRepository;
  @Autowired @Mock Writer writer;
  @Autowired @Mock HttpServletResponse httpServletResponse;
  @Autowired @Mock ServletOutputStream outputStream;
  @InjectMocks ApplicationsRequestServiceImpl applicationsRequestService;

  //  @Test
  //  void testCreateTimekeepingRequest_Normal() {
  //    ApplicationsRequestCreateRequest record = DataSample.APPLICATIONS_REQUEST_TIMEKEEPING;
  //    record.setEmployeeId("");
  //    Throwable exception =
  //        assertThrows(
  //            CustomDataNotFoundException.class,
  //            () -> applicationsRequestService.createApplicationsRequest(record));
  //    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  //  }

  @Test
  void test_getAllApplicationRequestReceive() {
    String employeeId = "huynq100";
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(applicationsRequestRepository.getListApplicationRequestReceive(
            QueryParam.defaultParam(), employeeId))
        .thenReturn(Arrays.asList(APPLICATIONS_REQUEST_RESPONSE));
    when(applicationsRequestRepository.countListApplicationRequestReceive(
            QueryParam.defaultParam(), employeeId))
        .thenReturn(1);
    applicationsRequestService.getAllApplicationRequestReceive(
        QueryParam.defaultParam(), employeeId);
  }

  @Test
  void test_getAllApplicationRequestReceive_IdNotExist() {
    String employeeId = "huynq100";
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.getAllApplicationRequestReceive(
                QueryParam.defaultParam(), employeeId));
  }

  @Test
  void test_getAllApplicationRequestSend() {
    String employeeId = "huynq100";
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(applicationsRequestRepository.getListApplicationRequestSend(
            QueryParam.defaultParam(), employeeId))
        .thenReturn(Arrays.asList(APPLICATIONS_REQUEST_RESPONSE));
    when(applicationsRequestRepository.countListApplicationRequestSend(
            QueryParam.defaultParam(), employeeId))
        .thenReturn(1);
    applicationsRequestService.getAllApplicationRequestSend(QueryParam.defaultParam(), employeeId);
  }

  @Test
  void test_getAllApplicationRequestSend_IdNotExist() {
    String employeeId = "huynq100";
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.getAllApplicationRequestSend(
                QueryParam.defaultParam(), employeeId));
  }

  @Test
  void test_insertApplicationRequest() {
    String employeeId = "huynq100";
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(employeeRepository.findById(APPLICATIONS_REQUEST_REQUEST.getApprover()))
        .thenReturn(Optional.of(employee));
    when(requestStatusRepository.existsById(APPLICATIONS_REQUEST_REQUEST.getRequestStatusId()))
        .thenReturn(true);
    when(requestNameRepository.existsById(APPLICATIONS_REQUEST_REQUEST.getRequestNameId()))
        .thenReturn(true);
    applicationsRequestService.insertApplicationRequest(APPLICATIONS_REQUEST_REQUEST);
  }

  @Test
  void test_insertApplicationRequest_EmployeeIdNotExist() {
    String employeeId = "huynq100";
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.insertApplicationRequest(APPLICATIONS_REQUEST_REQUEST));
  }

  @Test
  void test_insertApplicationRequest_ApproverIdNotExist() {
    String employeeId = "huynq100";
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(employeeRepository.findById(APPLICATIONS_REQUEST_REQUEST.getApprover()))
        .thenReturn(Optional.empty());
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.insertApplicationRequest(APPLICATIONS_REQUEST_REQUEST));
  }

  @Test
  void test_insertApplicationRequest_RequestStatusNotExist() {
    String employeeId = "huynq100";
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(employeeRepository.findById(APPLICATIONS_REQUEST_REQUEST.getApprover()))
        .thenReturn(Optional.of(employee));
    when(requestStatusRepository.existsById(APPLICATIONS_REQUEST_REQUEST.getRequestStatusId()))
        .thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.insertApplicationRequest(APPLICATIONS_REQUEST_REQUEST));
  }

  @Test
  void test_insertApplicationRequest_RequestNameNotExist() {
    String employeeId = "huynq100";
    Employee employee = new Employee();
    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    when(employeeRepository.findById(APPLICATIONS_REQUEST_REQUEST.getApprover()))
        .thenReturn(Optional.of(employee));
    when(requestStatusRepository.existsById(APPLICATIONS_REQUEST_REQUEST.getRequestStatusId()))
        .thenReturn(true);
    when(requestNameRepository.existsById(APPLICATIONS_REQUEST_REQUEST.getRequestNameId()))
        .thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.insertApplicationRequest(APPLICATIONS_REQUEST_REQUEST));
  }

  @Test
  void test_updateCheckedApplicationRequest() {
    when(applicationsRequestRepository.checkExistRequestId(
            UPDATE_APPLICATION_REQUEST_REQUEST.getApplicationRequestId()))
        .thenReturn(true);
    when(employeeDetailRepository.checkEmployeeIDIsExists(
            UPDATE_APPLICATION_REQUEST_REQUEST.getApproverId()))
        .thenReturn(true);
    when(requestStatusRepository.existsById(
            ERequestStatus.getValue(UPDATE_APPLICATION_REQUEST_REQUEST.getRequestStatus())))
        .thenReturn(true);
    applicationsRequestService.updateCheckedApplicationRequest(
        UPDATE_APPLICATION_REQUEST_REQUEST, "huynq100");
  }

  @Test
  void test_updateCheckedApplicationRequest_RequestIdNotExist() {
    when(applicationsRequestRepository.checkExistRequestId(
            UPDATE_APPLICATION_REQUEST_REQUEST.getApplicationRequestId()))
        .thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.updateCheckedApplicationRequest(
                UPDATE_APPLICATION_REQUEST_REQUEST, "huynq100"));
  }

  @Test
  void test_updateCheckedApplicationRequest_EmployeeIdNotExist() {
    when(applicationsRequestRepository.checkExistRequestId(
            UPDATE_APPLICATION_REQUEST_REQUEST.getApplicationRequestId()))
        .thenReturn(true);
    when(employeeDetailRepository.checkEmployeeIDIsExists(
            UPDATE_APPLICATION_REQUEST_REQUEST.getApproverId()))
        .thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.updateCheckedApplicationRequest(
                UPDATE_APPLICATION_REQUEST_REQUEST, "huynq100"));
  }

  @Test
  void test_updateCheckedApplicationRequest_RequestStatusNotExist() {
    when(applicationsRequestRepository.checkExistRequestId(
            UPDATE_APPLICATION_REQUEST_REQUEST.getApplicationRequestId()))
        .thenReturn(true);
    when(employeeDetailRepository.checkEmployeeIDIsExists(
            UPDATE_APPLICATION_REQUEST_REQUEST.getApproverId()))
        .thenReturn(true);
    when(requestStatusRepository.existsById(
            ERequestStatus.getValue(UPDATE_APPLICATION_REQUEST_REQUEST.getRequestStatus())))
        .thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.updateCheckedApplicationRequest(
                UPDATE_APPLICATION_REQUEST_REQUEST, "huynq100"));
  }

  @Test
  void test_updateApproveApplicationRequest_updateWorkingTime() {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Date", "2022-08-02");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_WORKING_TIME));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_WORKING_TIME.getData()))
        .thenReturn(hashMap);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updateOT_HolidayAndWeekend() {
    LocalDate startDate = LocalDate.parse("2022-08-02");
    LocalDate endDate = LocalDate.parse("2022-08-03");
    List<LocalDate> listDate = new ArrayList<>();
    listDate.add(LocalDate.parse("2022-08-02"));
    listDate.add(LocalDate.parse("2022-08-03"));
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Start_Date", "2022-08-02");
    hashMap.put("End_Date", "2022-08-03");
    hashMap.put("Start_Time", "18:00");
    hashMap.put("End_Time", "20:00");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_OVER_TIME));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_OVER_TIME.getData()))
        .thenReturn(hashMap);
    when(salaryCalculator.getAllHolidayByRange(startDate, endDate)).thenReturn(listDate);
    when(salaryCalculator.getAllWeekendByRange(startDate, endDate)).thenReturn(listDate);
    when(timekeepingRepository.getListTimekeepingIdOvertimeTypeDto("huynq100", startDate, endDate))
        .thenReturn(Arrays.asList(TIMEKEEPING_ID_OVERTIME_TYPE_DTO));
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updateOT() {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Start_Date", "2022-08-02");
    hashMap.put("End_Date", "2022-08-02");
    hashMap.put("Start_Time", "18:00");
    hashMap.put("End_Time", "20:00");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_OVER_TIME));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_OVER_TIME.getData()))
        .thenReturn(hashMap);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updatePaidLeave() {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Start_Date", "2022-08-02");
    hashMap.put("End_Date", "2022-08-02");
    hashMap.put("Reason", "1");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_PAID_LEAVE));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_PAID_LEAVE.getData()))
        .thenReturn(hashMap);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updatePaidLeave_Range() {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Start_Date", "2022-08-02");
    hashMap.put("End_Date", "2022-08-03");
    hashMap.put("Reason", "1");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_PAID_LEAVE));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_PAID_LEAVE.getData()))
        .thenReturn(hashMap);
    when(timekeepingRepository.checkExistDateInTimekeeping(
            LocalDate.parse("2022-08-02"), "huynq100"))
        .thenReturn(false);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updateBonus() {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Bonus_Type", "1");
    hashMap.put("Value", "500000");
    hashMap.put("Date", "2022-08-02");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_BONUS));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_BONUS.getData())).thenReturn(hashMap);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updateSalaryIncrement() {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Start_Date", "2022-08-02");
    hashMap.put("Value", "500000");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_SALARY_INCREMENT));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_SALARY_INCREMENT.getData()))
        .thenReturn(hashMap);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updateAdvance() {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("Date", "2022-08-02");
    hashMap.put("Value", "500000");
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_ADVANCE));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_ADVANCE.getData())).thenReturn(hashMap);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updateWorkingTime_DataNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_WORKING_TIME));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_WORKING_TIME.getData()))
        .thenReturn(hashMap);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_updateOT_DataNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_OVER_TIME));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_OVER_TIME.getData()))
        .thenReturn(hashMap);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_updatePaidLeave_DataNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_PAID_LEAVE));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_PAID_LEAVE.getData()))
        .thenReturn(hashMap);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_updateBonus_DataNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_BONUS));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_BONUS.getData())).thenReturn(hashMap);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_updateSalaryIncrement_DataNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_SALARY_INCREMENT));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_SALARY_INCREMENT.getData()))
        .thenReturn(hashMap);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_updateAdvance_DataNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_ADVANCE));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_ADVANCE.getData())).thenReturn(hashMap);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_EmployeeIdNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_EMPLOYEE_ID_NULL));
    when(employeeRepository.getEmployeeNameByEmployeeId(null)).thenReturn(null);
    when(employeeRepository.getEmployeeNameByEmployeeId(null)).thenReturn(null);
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO_EMPLOYEE_ID_NULL.getData()))
        .thenReturn(hashMap);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_RequestIdNull() {
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(null));
  }

  @Test
  void test_updateApproveApplicationRequest_RequestIdNotExist() {
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_AlreadyApproveReject() {
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(true);
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateApproveApplicationRequest_ApplicationRequestEmpty() {
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.empty());
    assertThrows(
        CustomErrorException.class,
        () -> applicationsRequestService.updateApproveApplicationRequest(2L));
  }

  @Test
  void test_updateRejectApplicationRequest() {
    when(applicationsRequestRepository.checkExistRequestId(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO_WORKING_TIME));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    applicationsRequestService.updateRejectApplicationRequest(REJECT_APPLICATION_REQUEST_REQUEST);
  }

  @Test
  void test_updateRejectApplicationRequest_RequestIdNotExist() {
    when(applicationsRequestRepository.checkExistRequestId(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.updateRejectApplicationRequest(
                REJECT_APPLICATION_REQUEST_REQUEST));
  }

  @Test
  void test_updateRejectApplicationRequest_AlreadyApproveOrReject() {
    when(applicationsRequestRepository.checkExistRequestId(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(true);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.updateRejectApplicationRequest(
                REJECT_APPLICATION_REQUEST_REQUEST));
  }

  @Test
  void test_updateRejectApplicationRequest_ApplicationRequestEmpty() {
    when(applicationsRequestRepository.checkExistRequestId(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(
            REJECT_APPLICATION_REQUEST_REQUEST.getRequestId()))
        .thenReturn(Optional.empty());
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.updateRejectApplicationRequest(
                REJECT_APPLICATION_REQUEST_REQUEST));
  }

  @Test
  void test_exportApplicationRequestReceiveByCsv() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huynq100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(true);
    when(applicationsRequestRepository.getListApplicationRequestReceiveByListId(
            QueryParam.defaultParam(), "huynq100", list))
        .thenReturn(Arrays.asList(APPLICATIONS_REQUEST_RESPONSE));
    applicationsRequestService.exportApplicationRequestReceiveToCsv(
        writer, QueryParam.defaultParam(), "huynq100", list);
  }

  @Test
  void test_exportApplicationRequestReceiveByCsv_ListNull() {
    assertThrows(
        CustomDataNotFoundException.class,
        () ->
            applicationsRequestService.exportApplicationRequestReceiveToCsv(
                writer, QueryParam.defaultParam(), "huynq100", new ArrayList<>()));
  }

  @Test
  void test_exportApplicationRequestReceiveByCsv_EmployeeIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huynq100")).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestReceiveToCsv(
                writer, QueryParam.defaultParam(), "huynq100", list));
  }

  @Test
  void test_exportApplicationRequestReceiveByCsv_RequestIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huynq100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestReceiveToCsv(
                writer, QueryParam.defaultParam(), "huynq100", list));
  }

  @Test
  void test_exportApplicationRequestSendByCsv() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huynq100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(true);
    when(applicationsRequestRepository.getListApplicationRequestSendByListId(
            QueryParam.defaultParam(), "huynq100", list))
        .thenReturn(Arrays.asList(APPLICATIONS_REQUEST_RESPONSE));
    applicationsRequestService.exportApplicationRequestSendToCsv(
        writer, QueryParam.defaultParam(), "huynq100", list);
  }

  @Test
  void test_exportApplicationRequestSendByCsv_ListNull() {
    assertThrows(
        CustomDataNotFoundException.class,
        () ->
            applicationsRequestService.exportApplicationRequestSendToCsv(
                writer, QueryParam.defaultParam(), "huynq100", new ArrayList<>()));
  }

  @Test
  void test_exportApplicationRequestSendByCsv_EmployeeIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huynq100")).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestSendToCsv(
                writer, QueryParam.defaultParam(), "huynq100", list));
  }

  @Test
  void test_exportApplicationRequestSendByCsv_RequestIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huynq100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestSendToCsv(
                writer, QueryParam.defaultParam(), "huynq100", list));
  }

  @Test
  void test_exportApplicationRequestReceiveByExcel() throws IOException {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huyqn100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(true);
    doReturn(Arrays.asList(APPLICATIONS_REQUEST_RESPONSE))
        .when(applicationsRequestRepository)
        .getListApplicationRequestReceiveByListId(QueryParam.defaultParam(), "huyqn100", list);
    when(httpServletResponse.getOutputStream()).thenReturn(outputStream);
    applicationsRequestService.exportApplicationRequestReceiveByExcel(
        httpServletResponse, QueryParam.defaultParam(), "huyqn100", list);
  }

  @Test
  void test_exportApplicationRequestReceiveByExcel_ListNull() {
    assertThrows(
        CustomDataNotFoundException.class,
        () ->
            applicationsRequestService.exportApplicationRequestReceiveByExcel(
                httpServletResponse, QueryParam.defaultParam(), "huyqn100", new ArrayList<>()));
  }

  @Test
  void test_exportApplicationRequestReceiveByExcel_EmployeeIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huyqn100")).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestReceiveByExcel(
                httpServletResponse, QueryParam.defaultParam(), "huyqn100", list));
  }

  @Test
  void test_exportApplicationRequestReceiveByExcel_RequestIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huyqn100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestReceiveByExcel(
                httpServletResponse, QueryParam.defaultParam(), "huyqn100", list));
  }

  @Test
  void test_exportApplicationRequestSendByExcel() throws IOException {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huyqn100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(true);
    doReturn(Arrays.asList(APPLICATIONS_REQUEST_RESPONSE))
        .when(applicationsRequestRepository)
        .getListApplicationRequestSendByListId(QueryParam.defaultParam(), "huyqn100", list);
    when(httpServletResponse.getOutputStream()).thenReturn(outputStream);
    applicationsRequestService.exportApplicationRequestSendByExcel(
        httpServletResponse, QueryParam.defaultParam(), "huyqn100", list);
  }

  @Test
  void test_exportApplicationRequestSendByExcel_ListNull() {
    assertThrows(
        CustomDataNotFoundException.class,
        () ->
            applicationsRequestService.exportApplicationRequestSendByExcel(
                httpServletResponse, QueryParam.defaultParam(), "huyqn100", new ArrayList<>()));
  }

  @Test
  void test_exportApplicationRequestSendByExcel_EmployeeIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huyqn100")).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestSendByExcel(
                httpServletResponse, QueryParam.defaultParam(), "huyqn100", list));
  }

  @Test
  void test_exportApplicationRequestSendByExcel_RequestIdNotExist() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    when(employeeRepository.existsById("huyqn100")).thenReturn(true);
    when(applicationsRequestRepository.existsById(1L)).thenReturn(false);
    assertThrows(
        CustomErrorException.class,
        () ->
            applicationsRequestService.exportApplicationRequestSendByExcel(
                httpServletResponse, QueryParam.defaultParam(), "huyqn100", list));
  }

  @Test
  void test_updateAllApplicationRequestRemind_Reject() {
    LocalDateTime checkDate = LocalDateTime.now();
    LocalDateTime currDate = LocalDateTime.now();
    when(applicationsRequestRepository.getAllApplicationRequestToRemind(checkDate))
        .thenReturn(Arrays.asList(APPLICATION_REQUEST_REMIND_RESPONSE));
    when(employeeRepository.getEmployeeNameByEmployeeId(
            APPLICATION_REQUEST_REMIND_RESPONSE.getApprover()))
        .thenReturn("Pham Thi Lien");
    when(employeeRepository.getEmployeeEmailByEmployeeId(
            APPLICATION_REQUEST_REMIND_RESPONSE.getApprover()))
        .thenReturn("LienPt1@fpt.edu.vn");
    applicationsRequestService.updateAllApplicationRequestRemind(checkDate, currDate);
  }

  @Test
  void test_updateAllApplicationRequestRemind_SendMail() {
    LocalDateTime checkDate = LocalDateTime.now();
    LocalDateTime currDate = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
    when(applicationsRequestRepository.getAllApplicationRequestToRemind(checkDate))
        .thenReturn(Arrays.asList(APPLICATION_REQUEST_REMIND_RESPONSE));
    when(employeeRepository.getEmployeeNameByEmployeeId(
            APPLICATION_REQUEST_REMIND_RESPONSE.getApprover()))
        .thenReturn("Pham Thi Lien");
    when(employeeRepository.getEmployeeEmailByEmployeeId(
            APPLICATION_REQUEST_REMIND_RESPONSE.getApprover()))
        .thenReturn("LienPt1@fpt.edu.vn");
    applicationsRequestService.updateAllApplicationRequestRemind(checkDate, currDate);
  }
}
