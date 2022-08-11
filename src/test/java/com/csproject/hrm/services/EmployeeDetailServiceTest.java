package com.csproject.hrm.services;

import com.csproject.hrm.common.sample.DataSample;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.services.impl.EmployeeDetailServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.csproject.hrm.common.constant.Constants.NO_EMPLOYEE_WITH_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeDetailServiceTest {
  @Autowired @Mock EmployeeDetailRepository employeeDetailRepository;
  @InjectMocks EmployeeDetailServiceImpl employeeDetailService;

  @DisplayName("Test findMainDetail Normal")
  @Test
  void testFindMainDetail_Normal() {
    EmployeeDetailResponse expected = DataSample.DETAIL_RESPONSE_SERVICE;
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeDetailRepository.findMainDetail(employeeId)).thenReturn(expected);
    EmployeeDetailResponse actual = employeeDetailService.findMainDetail(employeeId);
    assertEquals(expected, actual);
  }

  @DisplayName("Test findMainDetail EmployeeID Is null")
  @Test
  void testFindMainDetail_EmployeeIDIsNull() {
    Throwable exception =
        assertThrows(NullPointerException.class, () -> employeeDetailService.findMainDetail(null));
    assertEquals("Param employeeID is null!", exception.getMessage());
  }

  @DisplayName("Test findMainDetail EmployeeID Is not exist in db")
  @Test
  void testFindMainDetail_EmployeeIDIsNotExist() {
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class, () -> employeeDetailService.findMainDetail(""));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test findTaxAndInsurance Normal")
  @Test
  void testFindTaxAndInsurance_Normal() {
    TaxAndInsuranceResponse expected = DataSample.TAX_AND_INSURANCE_RESPONSE;
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeDetailRepository.findTaxAndInsurance(employeeId)).thenReturn(expected);
    TaxAndInsuranceResponse actual = employeeDetailService.findTaxAndInsurance(employeeId);
    assertEquals(expected, actual);
  }

  @DisplayName("Test findTaxAndInsurance EmployeeID Is null")
  @Test
  void testFindTaxAndInsurance_EmployeeIDIsNull() {
    Throwable exception =
        assertThrows(NullPointerException.class, () -> employeeDetailService.findMainDetail(null));
    assertEquals("Param employeeID is null!", exception.getMessage());
  }

  @DisplayName("Test findTaxAndInsurance EmployeeID Is not exist in db")
  @Test
  void testFindTaxAndInsurance_EmployeeIDIsNotExist() {
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class, () -> employeeDetailService.findMainDetail(""));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test findAdditionalInfo Normal")
  @Test
  void testFindAdditionalInfo_Normal() {
    EmployeeAdditionalInfo expected = DataSample.EMPLOYEE_ADDITIONAL_INFO;
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeDetailRepository.findAdditionalInfo(employeeId)).thenReturn(expected);
    EmployeeAdditionalInfo actual = employeeDetailService.findAdditionalInfo(employeeId);
    assertEquals(expected, actual);
  }

  @DisplayName("Test findAdditionalInfo EmployeeID Is null")
  @Test
  void testFindAdditionalInfo_EmployeeIDIsNull() {
    Throwable exception =
        assertThrows(
            NullPointerException.class, () -> employeeDetailService.findAdditionalInfo(null));
    assertEquals("Param employeeID is null!", exception.getMessage());
  }

  @DisplayName("Test findAdditionalInfo EmployeeID Is not exist in db")
  @Test
  void testFindAdditionalInfo_EmployeeIDIsNotExist() {
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class, () -> employeeDetailService.findAdditionalInfo(""));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test findBankByEmployeeID Normal")
  @Test
  void testFindBankInfo_Normal() {
    BankResponse expected = DataSample.BANK_RESPONSE;
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeDetailRepository.findBankByEmployeeID(employeeId)).thenReturn(expected);
    BankResponse actual = employeeDetailService.findBankByEmployeeID(employeeId);
    assertEquals(expected, actual);
  }

  @DisplayName("Test findBankByEmployeeID EmployeeID Is null")
  @Test
  void testFindBankInfo_EmployeeIDIsNull() {
    Throwable exception =
        assertThrows(
            NullPointerException.class, () -> employeeDetailService.findBankByEmployeeID(null));
    assertEquals("Param employeeID is null!", exception.getMessage());
  }

  @DisplayName("Test findBankByEmployeeID EmployeeID Is not exist in db")
  @Test
  void testFindBankInfo_EmployeeIDIsNotExist() {
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class,
            () -> employeeDetailService.findBankByEmployeeID(""));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test findEducationByEmployeeID Normal")
  @Test
  void testFindEducation_Normal() {
    List<EducationResponse> expected = DataSample.EDUCATION_RESPONSES;
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeDetailRepository.findEducationByEmployeeID(employeeId)).thenReturn(expected);
    List<EducationResponse> actual = employeeDetailService.findEducationByEmployeeID(employeeId);
    assertEquals(expected, actual);
  }

  @DisplayName("Test findEducationByEmployeeID EmployeeID Is null")
  @Test
  void testFindEducation_EmployeeIDIsNull() {
    Throwable exception =
        assertThrows(
            NullPointerException.class,
            () -> employeeDetailService.findEducationByEmployeeID(null));
    assertEquals("Param employeeID is null!", exception.getMessage());
  }

  @DisplayName("Test findEducationByEmployeeID EmployeeID Is not exist in db")
  @Test
  void testFindEducation_EmployeeIDIsNotExist() {
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class,
            () -> employeeDetailService.findEducationByEmployeeID(""));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test findWorkingHistoryByEmployeeID Normal")
  @Test
  void testFindWorkingHistory_Normal() {
    List<WorkingHistoryResponse> expected = DataSample.WORKING_HISTORY_RESPONSES;
    String employeeId = "huynq100";
    when(employeeDetailRepository.checkEmployeeIDIsExists(employeeId)).thenReturn(true);
    when(employeeDetailRepository.findWorkingHistoryByEmployeeID(employeeId)).thenReturn(expected);
    List<WorkingHistoryResponse> actual =
        employeeDetailService.findWorkingHistoryByEmployeeID("huynq100");
    assertEquals(expected, actual);
  }

  @DisplayName("Test findWorkingHistoryByEmployeeID EmployeeID Is null")
  @Test
  void testFindWorkingHistory_EmployeeIDIsNull() {
    Throwable exception =
        assertThrows(
            NullPointerException.class,
            () -> employeeDetailService.findWorkingHistoryByEmployeeID(null));
    assertEquals("Param employeeID is null!", exception.getMessage());
  }

  @DisplayName("Test findWorkingHistoryByEmployeeID EmployeeID Is not exist in db")
  @Test
  void testFindWorkingHistory_EmployeeIDIsNotExist() {
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class,
            () -> employeeDetailService.findWorkingHistoryByEmployeeID(""));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test updateEmployeeDetail EmployeeID Is not exist in db")
  @Test
  void testUpdateEmployeeDetail_EmployeeIDIsNotExist() {
    EmployeeDetailRequest record = DataSample.EMPLOYEE_DETAIL_REQUEST;
    record.setEmployee_id("");
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class,
            () -> employeeDetailService.updateEmployeeDetail(record));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test updateAdditionalInfo EmployeeID Is not exist in db")
  @Test
  void testUpdateAdditionalInfo_EmployeeIDIsNotExist() {
    EmployeeAdditionalInfoRequest record = DataSample.EMPLOYEE_ADDITIONAL_INFO_REQUEST;
    record.setEmployee_id("");
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class,
            () -> employeeDetailService.updateAdditionalInfo(record));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test updateBankInfo EmployeeID Is not exist in db")
  @Test
  void testUpdateBankInfo_EmployeeIDIsNotExist() {
    BankRequest record = DataSample.BANK_REQUEST;
    record.setEmployeeId("");
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class, () -> employeeDetailService.updateBankInfo(record));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }

  @DisplayName("Test updateEducationInfo EmployeeID Is not exist in db")
  @Test
  void testUpdateEducationInfo_EmployeeIDIsNotExist() {
    EducationRequest record = DataSample.EDUCATION_REQUEST;
    record.setEmployeeId("");
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class,
            () -> employeeDetailService.updateEducationInfo(record));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }
}