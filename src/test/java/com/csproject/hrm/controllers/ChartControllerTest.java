package com.csproject.hrm.controllers;

import com.csproject.hrm.common.sample.ChartDataSample;
import com.csproject.hrm.common.utils.DateUtils;
import com.csproject.hrm.config.TestSecurityConfig;
import com.csproject.hrm.dto.chart.*;
import com.csproject.hrm.dto.response.EmployeeNameAndID;
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
import java.time.LocalDate;
import java.util.List;

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
      value = "manager",
      roles = {"MANAGER"})
  void testGetGeneralEmployeeDataForChart_Manager_Normal() throws Exception {
    String employeeId = "huynq100";
    String areaName = chartService.getAreaNameByEmployeeID(employeeId);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    GeneralDataCharts response = ChartDataSample.GENERAL_DATA_CHARTS;

    Mockito.when(chartService.getGeneralEmployeeDataForChartByAreaName(employeeId, areaName));

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_general_data_chart")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetOrganizational_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    OrganizationalChart response = ChartDataSample.ORGANIZATIONAL_CHART;

    Mockito.when(chartService.getOrganizational()).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_organizational")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetLeaveCompanyReason_Manager_Normal() throws Exception {
    String employeeId = "huynq100";
    String areaName = chartService.getAreaNameByEmployeeID(employeeId);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<LeaveCompanyChart> response = ChartDataSample.LEAVE_COMPANY_CHARTS;

    when(chartService.getLeaveCompanyReasonByYearAndAreaName(2022, areaName)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_leave_company_reason_chart?year=2022")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetPaidLeaveReason_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<PaidLeaveChart> response = ChartDataSample.PAID_LEAVE_CHARTS;

    when(chartService.getPaidLeaveReasonByYearAndManagerID(
            request.getHeader(AUTHORIZATION), 2022, "huynq100"))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + "/get_paid_leave_reason_chart?year=2022&employeeId=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetSalaryStructure_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<GeneralSalaryChart> response = ChartDataSample.SALARY_STRUCTURE;

    when(chartService.getSalaryStructureByDateAndEmployeeID(
            DateUtils.convert("2022-08-01"), "huynq100"))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + "/get_salary_structure_chart?date=2022-08-01&employeeId=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetSalaryStructure_Manager_EmployeeIDIsEmpty() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<GeneralSalaryChart> response = ChartDataSample.SALARY_STRUCTURE;

    when(chartService.getSalaryStructureByDateAndEmployeeID(DateUtils.convert("2022-08-01"), ""))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + "/get_salary_structure_chart?date=2022-08-01&employeeId=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetSalaryHistory_Yearly_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<GeneralSalaryChart> response = ChartDataSample.SALARY_HISTORY_YEARLY;

    when(chartService.getSalaryHistoryByDateAndEmployeeIDAndType(
            chartService.getStartDateOfContract("huynq100"), "huynq100", "yearly"))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + "/get_salary_history_chart?type=yearly&employeeId=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetSalaryHistory_Yearly_Manager_EmployeeIDIsEmpty() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<GeneralSalaryChart> response = ChartDataSample.SALARY_HISTORY_YEARLY;

    when(chartService.getSalaryHistoryByDateAndEmployeeIDAndType(
            chartService.getStartDateOfContract("huynq100"), "", "yearly"))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + "/get_salary_history_chart?type=yearly&employeeId=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetSalaryHistory_Monthly_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<GeneralSalaryChart> response = ChartDataSample.SALARY_HISTORY_MONTHLY;

    when(chartService.getSalaryHistoryByDateAndEmployeeIDAndType(
            DateUtils.convert("2022-10-01"), "huynq100", "monthly"))
        .thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING
                    + "/get_salary_history_chart?type=monthly&date=2022-10-01&employeeId=huynq100")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetAllEmployeeByManagerID_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<EmployeeNameAndID> response = ChartDataSample.EMPLOYEE_NAME_AND_IDS;

    when(employeeDetailService.getAllEmployeeByManagerID("huynq100")).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_employee_by_manager")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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
  void testGetAllHoliday_Manager_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);

    List<LocalDate> response = ChartDataSample.DATE_LIST;

    when(holidayCalenderService.getAllHolidayByYear(LocalDate.of(2022, 1, 1)).isEmpty())
        .thenReturn(false);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_holiday?year=2022")
            .accept("*/*")
            .header(
                AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZW5hYmxlZCI6dHJ1ZSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiYWNjb3VudE5vbkxvY2tlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGx9LCJpYXQiOjE2NjAxNTQxNDMsImV4cCI6MTY2MDI0MDU0M30.imi_WcXHDUxND1T4feWl7_YX3jXNJbkIC-zgA2lZNk2g_bowc9P2z-nMeRbGvTXWS1Wv3VGHOWmMRTHEGl2D6A")
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