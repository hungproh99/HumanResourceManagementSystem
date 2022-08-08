package com.csproject.hrm.controllers;

import com.csproject.hrm.common.configs.JacksonConfig;
import com.csproject.hrm.config.TestSecurityConfig;
import com.csproject.hrm.dto.response.ListApplicationsRequestResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.ApplicationsRequestService;
import com.csproject.hrm.services.ChartService;
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

import static com.csproject.hrm.common.constant.Constants.AUTHORIZATION;
import static com.csproject.hrm.common.sample.DataSample.*;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ApplicationsRequestController.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class ApplicationRequestControllerTest {
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private JwtUtils jwtUtils;
  @Autowired @MockBean private ApplicationsRequestService applicationsRequestService;

  @Autowired @MockBean private ChartService chartService;

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
    when(applicationsRequestService.getAllApplicationRequestReceive(queryParam, employeeId))
        .thenReturn(LIST_APPLICATIONS_REQUEST_RESPONSE);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_all_application_request_receive")
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

    ListApplicationsRequestResponse expected = LIST_APPLICATIONS_REQUEST_RESPONSE;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    ListApplicationsRequestResponse listApplicationsRequestResponse =
        mapper.readValue(actual, ListApplicationsRequestResponse.class);
    assertEquals(expected, listApplicationsRequestResponse);
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
    when(applicationsRequestService.getAllApplicationRequestReceive(queryParam, employeeId))
        .thenReturn(LIST_APPLICATIONS_REQUEST_RESPONSE);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_all_application_request_receive")
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
  void test_getListApplicationsRequestSend() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(applicationsRequestService.getAllApplicationRequestSend(queryParam, employeeId))
        .thenReturn(LIST_APPLICATIONS_REQUEST_RESPONSE);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_all_application_request_send")
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

    ListApplicationsRequestResponse expected = LIST_APPLICATIONS_REQUEST_RESPONSE;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    ListApplicationsRequestResponse listApplicationsRequestResponse =
        mapper.readValue(actual, ListApplicationsRequestResponse.class);
    assertEquals(expected, listApplicationsRequestResponse);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListApplicationsRequestSend_Exception() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(applicationsRequestService.getAllApplicationRequestSend(queryParam, employeeId))
        .thenReturn(LIST_APPLICATIONS_REQUEST_RESPONSE);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_all_application_request_send")
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
  void test_updateCheckedApplicationRequest() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    doNothing()
        .when(applicationsRequestService)
        .updateCheckedApplicationRequest(UPDATE_APPLICATION_REQUEST_REQUEST, employeeId);
    applicationsRequestService.updateCheckedApplicationRequest(
        UPDATE_APPLICATION_REQUEST_REQUEST, employeeId);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/update_checked_application_request")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .content(objectMapper.writeValueAsString(UPDATE_APPLICATION_REQUEST_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(applicationsRequestService, times(1))
        .updateCheckedApplicationRequest(UPDATE_APPLICATION_REQUEST_REQUEST, employeeId);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_updateCheckedApplicationRequest_Exception() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    doNothing()
        .when(applicationsRequestService)
        .updateCheckedApplicationRequest(UPDATE_APPLICATION_REQUEST_REQUEST, employeeId);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/update_checked_application_request")
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .content(objectMapper.writeValueAsString(UPDATE_APPLICATION_REQUEST_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    assertThrows(NestedServletException.class, () -> mockMvc.perform(requestBuilder));
  }

  @Test
  void test_updateApproveApplicationRequest() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    Long requestId = 1L;
    doNothing().when(applicationsRequestService).updateApproveApplicationRequest(requestId);
    applicationsRequestService.updateApproveApplicationRequest(requestId);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/update_approve_application_request")
            .accept("*/*")
            .param("requestId", objectMapper.writeValueAsString(requestId))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(applicationsRequestService, times(1)).updateApproveApplicationRequest(requestId);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_updateRejectApplicationRequest() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    doNothing().when(applicationsRequestService).updateRejectApplicationRequest(REJECT_APPLICATION_REQUEST_REQUEST);
    applicationsRequestService.updateRejectApplicationRequest(REJECT_APPLICATION_REQUEST_REQUEST);
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/update_reject_application_request")
            .accept("*/*")
                .content(objectMapper.writeValueAsString(UPDATE_APPLICATION_REQUEST_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(applicationsRequestService, times(1)).updateRejectApplicationRequest(REJECT_APPLICATION_REQUEST_REQUEST);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }
}
