package com.csproject.hrm.services;

import com.csproject.hrm.common.enums.ERequestStatus;
import com.csproject.hrm.common.general.GeneralFunction;
import com.csproject.hrm.common.general.SalaryCalculator;
import com.csproject.hrm.entities.Employee;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static com.csproject.hrm.common.sample.DataSample.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO.getData())).thenReturn(hashMap);
    applicationsRequestService.updateApproveApplicationRequest(2L);
  }

  @Test
  void test_updateApproveApplicationRequest_updateWorkingTime_DateNull() {
    HashMap<String, String> hashMap = new HashMap<>();
    when(applicationsRequestRepository.checkExistRequestId(2L)).thenReturn(true);
    when(applicationsRequestRepository.checkAlreadyApproveOrReject(2L)).thenReturn(false);
    when(applicationsRequestRepository.getApplicationRequestDtoByRequestId(2L))
        .thenReturn(Optional.of(APPLICATION_REQUEST_DTO));
    when(employeeRepository.getEmployeeNameByEmployeeId("huynq100")).thenReturn("Nguyen Quang Huy");
    when(employeeRepository.getEmployeeNameByEmployeeId("lienpt1")).thenReturn("Pham Thi Lien");
    when(generalFunction.splitData(APPLICATION_REQUEST_DTO.getData())).thenReturn(hashMap);
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
}
