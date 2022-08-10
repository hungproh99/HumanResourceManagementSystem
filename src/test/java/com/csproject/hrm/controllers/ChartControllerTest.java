package com.csproject.hrm.controllers;

import com.csproject.hrm.common.sample.ChartDataSample;
import com.csproject.hrm.config.TestSecurityConfig;
import com.csproject.hrm.dto.chart.GeneralDataCharts;
import com.csproject.hrm.dto.chart.OrganizationalChart;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.impl.*;
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

import static com.csproject.hrm.common.constant.Constants.AUTHORIZATION;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ChartController.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class ChartControllerTest {
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private ChartServiceImpl chartService;
  @Autowired @MockBean private EmployeeDetailServiceImpl employeeDetailService;
  @Autowired @MockBean private HolidayCalenderServiceImpl holidayCalenderService;
  @Autowired @MockBean private JwtUtils jwtUtils;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void testGetGeneralEmployeeDataForChart_Admin_Normal() throws Exception {
    String employeeId = "huynq100";
    String areaName = chartService.getAreaNameByEmployeeID(employeeId);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("ADMIN")).thenReturn(true);

    GeneralDataCharts response = ChartDataSample.GENERAL_DATA_CHARTS;

    Mockito.when(chartService.getGeneralEmployeeDataForChartByAreaName(areaName))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_general_data_chart")
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
  void testGetOrganizational_Admin_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("ADMIN")).thenReturn(true);

    OrganizationalChart response = ChartDataSample.ORGANIZATIONAL_CHART;

    Mockito.when(chartService.getOrganizational()).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_organizational")
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
}