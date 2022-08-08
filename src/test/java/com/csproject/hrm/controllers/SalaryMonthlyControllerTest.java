package com.csproject.hrm.controllers;

import com.csproject.hrm.common.configs.JacksonConfig;
import com.csproject.hrm.config.TestSecurityConfig;
import com.csproject.hrm.dto.dto.BonusTypeDto;
import com.csproject.hrm.dto.dto.DeductionTypeDto;
import com.csproject.hrm.dto.response.SalaryMonthlyDetailResponse;
import com.csproject.hrm.dto.response.SalaryMonthlyResponseList;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.SalaryMonthlyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.csproject.hrm.common.constant.Constants.AUTHORIZATION;
import static com.csproject.hrm.common.sample.DataSample.*;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SalaryMonthlyController.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class SalaryMonthlyControllerTest {
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private JwtUtils jwtUtils;
  @Autowired @MockBean private SalaryMonthlyService salaryMonthlyService;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListApplicationsRequestReceive() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(salaryMonthlyService.getAllSalaryMonthlyForPersonal(queryParam, employeeId))
        .thenReturn(SALARY_MONTHLY_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_personal_salary_monthly")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    SalaryMonthlyResponseList expected = SALARY_MONTHLY_RESPONSE_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    SalaryMonthlyResponseList salaryMonthlyResponseList =
        mapper.readValue(actual, SalaryMonthlyResponseList.class);
    assertEquals(expected, salaryMonthlyResponseList);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListApplicationsRequestReceive_Exception() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(salaryMonthlyService.getAllSalaryMonthlyForPersonal(queryParam, employeeId))
        .thenReturn(SALARY_MONTHLY_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_personal_salary_monthly")
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    assertThrows(NestedServletException.class, () -> mockMvc.perform(requestBuilder));
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListAllManagementSalaryMonthly() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(salaryMonthlyService.getAllSalaryMonthlyForManagement(queryParam, employeeId))
        .thenReturn(SALARY_MONTHLY_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_management_salary_monthly")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    SalaryMonthlyResponseList expected = SALARY_MONTHLY_RESPONSE_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    SalaryMonthlyResponseList salaryMonthlyResponseList =
        mapper.readValue(actual, SalaryMonthlyResponseList.class);
    assertEquals(expected, salaryMonthlyResponseList);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListAllManagementSalaryMonthly_Exception() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(salaryMonthlyService.getAllSalaryMonthlyForManagement(queryParam, employeeId))
        .thenReturn(SALARY_MONTHLY_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_management_salary_monthly")
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    assertThrows(NestedServletException.class, () -> mockMvc.perform(requestBuilder));
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getSalaryMonthlyDetail_Exception() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    Long salaryId = 1L;
    when(salaryMonthlyService.getSalaryMonthlyDetailBySalaryMonthlyId(salaryId))
        .thenReturn(SALARY_MONTHLY_DETAIL_RESPONSE);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_salary_monthly_detail")
            .accept("*/*")
            .param("salaryId", objectMapper.writeValueAsString(salaryId))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    SalaryMonthlyDetailResponse expected = SALARY_MONTHLY_DETAIL_RESPONSE;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    SalaryMonthlyDetailResponse salaryMonthlyDetailResponse =
        mapper.readValue(actual, SalaryMonthlyDetailResponse.class);
    assertEquals(expected, salaryMonthlyDetailResponse);
  }

  @Test
  void test_updateDeductionSalary() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    doNothing().when(salaryMonthlyService).updateDeductionSalary(DEDUCTION_SALARY_REQUEST);
    salaryMonthlyService.updateDeductionSalary(DEDUCTION_SALARY_REQUEST);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_deduction_salary")
            .accept("*/*")
            .content(objectMapper.writeValueAsString(DEDUCTION_SALARY_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).updateDeductionSalary(DEDUCTION_SALARY_REQUEST);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_deleteDeductionSalary() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    Long deductionId = 1L;
    doNothing().when(salaryMonthlyService).deleteDeductionSalary(deductionId);
    salaryMonthlyService.deleteDeductionSalary(deductionId);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.delete(REQUEST_MAPPING + "/delete_deduction_salary")
            .accept("*/*")
            .param("deductionId", objectMapper.writeValueAsString(deductionId))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).deleteDeductionSalary(deductionId);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_updateBonusSalary() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    doNothing().when(salaryMonthlyService).updateBonusSalary(BONUS_SALARY_REQUEST);
    salaryMonthlyService.updateBonusSalary(BONUS_SALARY_REQUEST);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_bonus_salary")
            .accept("*/*")
            .content(objectMapper.writeValueAsString(BONUS_SALARY_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).updateBonusSalary(BONUS_SALARY_REQUEST);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_deleteBonusSalary() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    Long bonusId = 1L;
    doNothing().when(salaryMonthlyService).deleteBonusSalary(bonusId);
    salaryMonthlyService.deleteBonusSalary(bonusId);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.delete(REQUEST_MAPPING + "/delete_bonus_salary")
            .accept("*/*")
            .param("bonusId", objectMapper.writeValueAsString(bonusId))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).deleteBonusSalary(bonusId);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_updateAdvanceSalary() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    doNothing().when(salaryMonthlyService).updateAdvanceSalary(ADVANCE_SALARY_REQUEST);
    salaryMonthlyService.updateAdvanceSalary(ADVANCE_SALARY_REQUEST);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_advance_salary")
            .accept("*/*")
            .content(objectMapper.writeValueAsString(ADVANCE_SALARY_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).updateAdvanceSalary(ADVANCE_SALARY_REQUEST);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_deleteAdvanceSalary() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    Long advanceId = 1L;
    doNothing().when(salaryMonthlyService).deleteAdvanceSalary(advanceId);
    salaryMonthlyService.deleteAdvanceSalary(advanceId);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.delete(REQUEST_MAPPING + "/delete_advance_salary")
            .accept("*/*")
            .param("advanceId", objectMapper.writeValueAsString(advanceId))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).deleteAdvanceSalary(advanceId);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_updateApproveSalaryMonthly() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    Long salaryMonthlyId = 1L;
    doNothing().when(salaryMonthlyService).updateApproveSalaryMonthly(salaryMonthlyId);
    salaryMonthlyService.updateApproveSalaryMonthly(salaryMonthlyId);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_approve_salary_monthly")
            .accept("*/*")
            .param("salaryMonthlyId", objectMapper.writeValueAsString(salaryMonthlyId))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).updateApproveSalaryMonthly(salaryMonthlyId);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_updateCheckedSalaryMonthly() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    doNothing()
        .when(salaryMonthlyService)
        .updateCheckedSalaryMonthly(UPDATE_SALARY_MONTHLY_REQUEST, employeeId);
    salaryMonthlyService.updateCheckedSalaryMonthly(UPDATE_SALARY_MONTHLY_REQUEST, employeeId);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_checked_salary_monthly")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .content(objectMapper.writeValueAsString(UPDATE_SALARY_MONTHLY_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1))
        .updateCheckedSalaryMonthly(UPDATE_SALARY_MONTHLY_REQUEST, employeeId);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_updateCheckedSalaryMonthly_Exception() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    doNothing()
        .when(salaryMonthlyService)
        .updateCheckedSalaryMonthly(UPDATE_SALARY_MONTHLY_REQUEST, employeeId);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_checked_salary_monthly")
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .content(objectMapper.writeValueAsString(UPDATE_SALARY_MONTHLY_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    assertThrows(NestedServletException.class, () -> mockMvc.perform(requestBuilder));
  }

  @Test
  void test_updateRejectSalaryMonthly() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    doNothing().when(salaryMonthlyService).updateRejectSalaryMonthly(REJECT_SALARY_MONTHLY_REQUEST);
    salaryMonthlyService.updateRejectSalaryMonthly(REJECT_SALARY_MONTHLY_REQUEST);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_reject_salary_monthly")
            .accept("*/*")
            .content(objectMapper.writeValueAsString(UPDATE_APPLICATION_REQUEST_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(salaryMonthlyService, times(1)).updateRejectSalaryMonthly(REJECT_SALARY_MONTHLY_REQUEST);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_getListDeductionType() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_deduction_type")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(salaryMonthlyService.getListDeductionTypeDto()).thenReturn(DEDUCTION_TYPE_DTO_LIST);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<DeductionTypeDto> expected = DEDUCTION_TYPE_DTO_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<DeductionTypeDto> deductionTypeDtoList =
        mapper.readerForListOf(DeductionTypeDto.class).readValue(actual);
    assertEquals(expected, deductionTypeDtoList);
  }

  @Test
  void test_getListBonusType() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_bonus_type")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(salaryMonthlyService.getListBonusTypeDto()).thenReturn(BONUS_TYPE_DTO_LIST);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<BonusTypeDto> expected = BONUS_TYPE_DTO_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<BonusTypeDto> bonusTypeDtoList =
        mapper.readerForListOf(BonusTypeDto.class).readValue(actual);
    assertEquals(expected, bonusTypeDtoList);
  }
}
