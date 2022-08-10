package com.csproject.hrm.controllers;

import com.csproject.hrm.common.configs.JacksonConfig;
import com.csproject.hrm.common.sample.DataSample;
import com.csproject.hrm.common.uri.Uri;
import com.csproject.hrm.config.TestSecurityConfig;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.impl.EmployeeDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.csproject.hrm.common.constant.Constants.AUTHORIZATION;
import static com.csproject.hrm.common.uri.Uri.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {EmployeeDetailController.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class EmployeeDetailControllerTest {

  private static final String REQUEST_MAPPING = Uri.REQUEST_MAPPING + REQUEST_DETAIL_MAPPING;
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private JwtUtils jwtUtils;
  @Autowired @MockBean private EmployeeDetailServiceImpl employeeDetailService;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void testFindMainDetail_Admin_Normal() throws Exception {
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("ADMIN")).thenReturn(true);

    EmployeeDetailResponse response = DataSample.DETAIL_RESPONSE;

    Mockito.when(employeeDetailService.findMainDetail(employeeId)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_MAIN_DETAIL + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindMainDetail_Manager_Normal() throws Exception {
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    EmployeeDetailResponse response = DataSample.DETAIL_RESPONSE;

    Mockito.when(employeeDetailService.findMainDetail(employeeId)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_MAIN_DETAIL + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "user ",
      roles = {"USER"})
  void testFindMainDetail_User_Normal() throws Exception {
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);

    EmployeeDetailResponse response = DataSample.DETAIL_RESPONSE;

    Mockito.when(employeeDetailService.findMainDetail(employeeId)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_MAIN_DETAIL + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindMainDetail_Manager_False() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    EmployeeDetailResponse response = DataSample.DETAIL_RESPONSE;

    Mockito.when(employeeDetailService.findMainDetail(null)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_MAIN_DETAIL)
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void testFindTaxAndInsurance_Admin_Normal() throws Exception {
    TaxAndInsuranceResponse response = DataSample.TAX_AND_INSURANCE_RESPONSE;

    Mockito.when(employeeDetailService.findTaxAndInsurance("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_TAX_AND_INSURANCE + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindTaxAndInsurance_Manager_Normal() throws Exception {
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    TaxAndInsuranceResponse response = DataSample.TAX_AND_INSURANCE_RESPONSE;

    Mockito.when(employeeDetailService.findTaxAndInsurance(employeeId)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_TAX_AND_INSURANCE + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  void testFindTaxAndInsurance_User_Normal() throws Exception {
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);

    TaxAndInsuranceResponse response = DataSample.TAX_AND_INSURANCE_RESPONSE;

    Mockito.when(employeeDetailService.findTaxAndInsurance(employeeId)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_TAX_AND_INSURANCE + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindTaxAndInsurance_Manager_False() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    EmployeeDetailResponse response = DataSample.DETAIL_RESPONSE;

    Mockito.when(employeeDetailService.findMainDetail(null)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_TAX_AND_INSURANCE)
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void testFindAdditionalInfo_Admin_Normal() throws Exception {
    EmployeeAdditionalInfo response = DataSample.EMPLOYEE_ADDITIONAL_INFO;

    Mockito.when(employeeDetailService.findAdditionalInfo("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_ADDITIONAL_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindAdditionalInfo_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    EmployeeAdditionalInfo response = DataSample.EMPLOYEE_ADDITIONAL_INFO;

    Mockito.when(employeeDetailService.findAdditionalInfo("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_ADDITIONAL_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  void testFindAdditionalInfo_User_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);
    EmployeeAdditionalInfo response = DataSample.EMPLOYEE_ADDITIONAL_INFO;

    Mockito.when(employeeDetailService.findAdditionalInfo("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_ADDITIONAL_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindAdditionalInfo_Manager_False() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    EmployeeAdditionalInfo response = DataSample.EMPLOYEE_ADDITIONAL_INFO;

    Mockito.when(employeeDetailService.findAdditionalInfo(null)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_ADDITIONAL_INFO)
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void testFindWorkingInfo_Admin_Normal() throws Exception {
    WorkingInfoResponse response = DataSample.WORKING_INFO_RESPONSE;

    Mockito.when(employeeDetailService.findWorkingInfo("lienpt2")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/working_info" + "?employeeID=lienpt2")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindWorkingInfo_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    WorkingInfoResponse response = DataSample.WORKING_INFO_RESPONSE;

    Mockito.when(employeeDetailService.findWorkingInfo("lienpt2")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/working_info" + "?employeeID=lienpt2")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  void testFindWorkingInfo_User_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);
    WorkingInfoResponse response = DataSample.WORKING_INFO_RESPONSE;

    Mockito.when(employeeDetailService.findWorkingInfo("lienpt2")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/working_info" + "?employeeID=lienpt2")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void testFindWorkingInfo_Manager_False() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    WorkingInfoResponse response = DataSample.WORKING_INFO_RESPONSE;

    Mockito.when(employeeDetailService.findWorkingInfo(null)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/working_info")
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  void testFindBank_User_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);
    BankResponse response = DataSample.BANK_RESPONSE;

    Mockito.when(employeeDetailService.findBankByEmployeeID("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_BANK_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  void testFindEducation_User_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);
    List<EducationResponse> response = DataSample.EDUCATION_RESPONSES;

    Mockito.when(employeeDetailService.findEducationByEmployeeID("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_EDU_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  void testFindWoringHistory_User_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);
    List<WorkingHistoryResponse> response = DataSample.WORKING_HISTORY_RESPONSES;

    Mockito.when(employeeDetailService.findWorkingHistoryByEmployeeID("huynq100"))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_WORKING_HISTORY_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  void testFindRelative_User_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("USER")).thenReturn(true);
    List<RelativeInformationResponse> response = DataSample.RELATIVE_INFORMATION_RESPONSES;

    Mockito.when(employeeDetailService.findRelativeByEmployeeID("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_RELATIVE_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateEmployeeDetail_Admin_Normal() throws Exception {
    EmployeeDetailRequest request = DataSample.EMPLOYEE_DETAIL_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateEmployeeDetail(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + URI_UPDATE_MAIN_DETAIL)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateAdditionalInfo_Admin_Normal() throws Exception {
    EmployeeAdditionalInfoRequest request = DataSample.EMPLOYEE_ADDITIONAL_INFO_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateAdditionalInfo(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/add_info/update")
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "user",
      roles = {"USER"})
  public void testUpdateAdditionalInfo_User_Normal() throws Exception {
    EmployeeAdditionalInfoRequest request = DataSample.EMPLOYEE_ADDITIONAL_INFO_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateAdditionalInfo(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/add_info/update")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateWoringHistory_User_Normal() throws Exception {
    WorkingHistoryRequest request = DataSample.WORKING_HISTORY_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateWorkingHistory(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + URI_UPDATE_WORKING_HISTORY_INFO)
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateRelative_Admin_Normal() throws Exception {
    RelativeInformationRequest request = DataSample.RELATIVE_INFORMATION_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateRelativeInfo(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + URI_UPDATE_RELATIVE_INFO)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateAvatar_Admin_Normal() throws Exception {
    AvatarRequest request =
        new AvatarRequest(
            "https://haycafe.vn/wp-content/uploads/2022/03/Avatar-hai-doc-600x600.jpg", "huynq100");
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateAvatar(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_avatar")
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateWorkingInfo_Admin_Normal() throws Exception {
    WorkingInfoRequest request = DataSample.WORKING_INFO_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateWorkingInfo(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_working_info")
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateBank_Admin_Normal() throws Exception {
    BankRequest request = DataSample.BANK_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateBankInfo(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + URI_UPDATE_BANK_INFO)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateEducation_Admin_Normal() throws Exception {
    EducationRequest request = DataSample.EDUCATION_REQUEST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateEducationInfo(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + URI_UPDATE_EDUCATION_INFO)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  public void testUpdateRole_Admin_Normal() throws Exception {
    RoleRequest request = new RoleRequest("2", "huynq100");
    ObjectMapper mapper = JacksonConfig.objectMapper();
    String requestJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

    Mockito.doNothing().when(employeeDetailService).updateRole(request);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.put(REQUEST_MAPPING + "/update_role")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIlVzZXJfRGF0YSI6eyJpZCI6ImFkbWluIiwiZW1haWwiOiJhZG1pbkBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImVuYWJsZWQiOnRydWUsImZ1bGxOYW1lIjoiQWRtaW4iLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJhY2NvdW50Tm9uRXhwaXJlZCI6dHJ1ZSwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJ1c2VybmFtZSI6bnVsbH0sImlhdCI6MTY2MDE1MTQyOCwiZXhwIjoxNjYwMjM3ODI4fQ.3hyPfbLrl_jveqbDBGX4v1LTglSNnqrVZEBhe71EWaCiDnu_BeRglFUeT1Banqzffg2HarQFLepROJ_uvdXDhw")
            .accept(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf());

    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void testFindRole_Admin_Normal() throws Exception {
    RoleResponse response = new RoleResponse("2", "Manager");

    Mockito.when(employeeDetailService.getRole("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + "/get_role_by_employeeid" + "?employeeId=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsIlVzZXJfRGF0YSI6eyJpZCI6ImFkbWluIiwiZW1haWwiOiJhZG1pbkBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImVuYWJsZWQiOnRydWUsImZ1bGxOYW1lIjoiQWRtaW4iLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJhY2NvdW50Tm9uRXhwaXJlZCI6dHJ1ZSwiY3JlZGVudGlhbHNOb25FeHBpcmVkIjp0cnVlLCJ1c2VybmFtZSI6bnVsbH0sImlhdCI6MTY2MDE1MTQyOCwiZXhwIjoxNjYwMjM3ODI4fQ.3hyPfbLrl_jveqbDBGX4v1LTglSNnqrVZEBhe71EWaCiDnu_BeRglFUeT1Banqzffg2HarQFLepROJ_uvdXDhw")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);
  }
}