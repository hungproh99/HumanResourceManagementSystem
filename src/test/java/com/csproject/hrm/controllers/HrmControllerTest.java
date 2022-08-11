package com.csproject.hrm.controllers;

import com.csproject.hrm.common.configs.JacksonConfig;
import com.csproject.hrm.config.TestSecurityConfig;
import com.csproject.hrm.dto.dto.*;
import com.csproject.hrm.dto.request.HrmRequest;
import com.csproject.hrm.dto.response.EmployeeNameAndID;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.impl.HumanManagementServiceImpl;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
@SpringBootTest(classes = {HrmController.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class HrmControllerTest {
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private JwtUtils jwtUtils;
  @Autowired @MockBean private HumanManagementServiceImpl humanManagementService;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void test_getAllEmployee_Admin() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("ADMIN")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListHumanResource(queryParam, employeeId))
        .thenReturn(HRM_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_employee")
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

    HrmResponseList expected = HRM_RESPONSE_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    HrmResponseList hrmResponseList = mapper.readValue(actual, HrmResponseList.class);
    assertEquals(expected, hrmResponseList);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getAllEmployee_Manager_Success() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListHumanResourceOfManager(queryParam, employeeId))
        .thenReturn(HRM_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_employee")
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

    HrmResponseList expected = HRM_RESPONSE_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    HrmResponseList hrmResponseList = mapper.readValue(actual, HrmResponseList.class);
    assertEquals(expected, hrmResponseList);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getAllEmployee_Manager_Exception() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListHumanResourceOfManager(queryParam, employeeId))
        .thenReturn(HRM_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_employee")
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    assertThrows(NestedServletException.class, () -> mockMvc.perform(requestBuilder));
  }

  @Test
  void test_addEmployee() throws Exception {
    HrmRequest hrmRequest = HRM_REQUEST;
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    doNothing().when(humanManagementService).insertEmployee(any(HrmRequest.class));
    humanManagementService.insertEmployee(hrmRequest);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/add_employee")
            .accept("*/*")
            .content(objectMapper.writeValueAsString(HRM_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);

    verify(humanManagementService, times(1)).insertEmployee(hrmRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  }

  @Test
  void test_importCSVToEmployee() throws Exception {
    String path = new File("C:/Users/ADMIN/Downloads/Import.csv").getCanonicalPath();
    File initialFile = new File(path);
    InputStream targetStream = new FileInputStream(initialFile);
    MockMultipartFile sampleFile =
        new MockMultipartFile("file", "Import.csv", "text/csv", targetStream);

    MockMultipartHttpServletRequestBuilder multipartRequest =
        MockMvcRequestBuilders.multipart(REQUEST_MAPPING + "/import_csv_employee");

    mockMvc.perform(multipartRequest.file(sampleFile)).andExpect(status().isOk());
  }

  @Test
  void test_importCSVToEmployee_NotCsv() throws Exception {
    String path = new File("C:/Users/ADMIN/Downloads/Import.csv").getCanonicalPath();
    File initialFile = new File(path);
    InputStream targetStream = new FileInputStream(initialFile);
    MockMultipartFile sampleFile =
        new MockMultipartFile("file", "Import.csv", "text/html", targetStream);

    MockMultipartHttpServletRequestBuilder multipartRequest =
        MockMvcRequestBuilders.multipart(REQUEST_MAPPING + "/import_csv_employee");

    assertThrows(
        NestedServletException.class, () -> mockMvc.perform(multipartRequest.file(sampleFile)));
  }

  @Test
  void test_importExcelToEmployee_Xlsx() throws Exception {
    String path = new File("C:/Users/ADMIN/Downloads/Import.xlsx").getCanonicalPath();
    File initialFile = new File(path);
    InputStream targetStream = new FileInputStream(initialFile);
    MockMultipartFile sampleFile =
        new MockMultipartFile(
            "file",
            "Import.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            targetStream);

    MockMultipartHttpServletRequestBuilder multipartRequest =
        MockMvcRequestBuilders.multipart(REQUEST_MAPPING + "/import_excel_employee");

    mockMvc.perform(multipartRequest.file(sampleFile)).andExpect(status().isOk());
  }

  @Test
  void test_importExcelToEmployee_Xls() throws Exception {
    String path = new File("C:/Users/ADMIN/Downloads/Import1.xls").getCanonicalPath();
    File initialFile = new File(path);
    InputStream targetStream = new FileInputStream(initialFile);
    MockMultipartFile sampleFile =
        new MockMultipartFile("file", "Import.xls", "application/vnd.ms-excel", targetStream);

    MockMultipartHttpServletRequestBuilder multipartRequest =
        MockMvcRequestBuilders.multipart(REQUEST_MAPPING + "/import_excel_employee");

    mockMvc.perform(multipartRequest.file(sampleFile)).andExpect(status().isOk());
  }

  @Test
  void test_importExcelToEmployee_NotXlsxAndXls() throws Exception {

    String path = new File("C:/Users/ADMIN/Downloads/Import.xlsx").getCanonicalPath();
    File initialFile = new File(path);
    InputStream targetStream = new FileInputStream(initialFile);
    MockMultipartFile sampleFile =
        new MockMultipartFile("file", "Import.xlsx", "text/plain", targetStream);

    MockMultipartHttpServletRequestBuilder multipartRequest =
        MockMvcRequestBuilders.multipart(REQUEST_MAPPING + "/import_excel_employee");
    assertThrows(
        NestedServletException.class, () -> mockMvc.perform(multipartRequest.file(sampleFile)));
  }

  @Test
  void test_getListWorkingType() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_working_type")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(humanManagementService.getListWorkingType()).thenReturn(LIST_WORKING_TYPE);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<WorkingTypeDto> expected = LIST_WORKING_TYPE;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<WorkingTypeDto> workingTypeDtoList =
        mapper.readerForListOf(WorkingTypeDto.class).readValue(actual);
    assertEquals(expected, workingTypeDtoList);
  }

  @Test
  void test_getListEmployeeType() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_employee_type")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(humanManagementService.getListEmployeeType()).thenReturn(LIST_EMPLOYEE_TYPE);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<EmployeeTypeDto> expected = LIST_EMPLOYEE_TYPE;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<EmployeeTypeDto> employeeTypeDtoList =
        mapper.readerForListOf(EmployeeTypeDto.class).readValue(actual);
    assertEquals(expected, employeeTypeDtoList);
  }

  @Test
  void test_getListArea() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_area")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(humanManagementService.getListArea()).thenReturn(LIST_AREA_DTO);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<AreaDto> expected = LIST_AREA_DTO;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<AreaDto> areaDtoList = mapper.readerForListOf(AreaDto.class).readValue(actual);
    assertEquals(expected, areaDtoList);
  }

  @Test
  void test_getListJob() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_job")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(humanManagementService.getListPosition()).thenReturn(LIST_JOB_DTO);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<JobDto> expected = LIST_JOB_DTO;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<JobDto> jobDtoList = mapper.readerForListOf(JobDto.class).readValue(actual);
    assertEquals(expected, jobDtoList);
  }

  @Test
  void test_getListGrade() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_grade/{job_id}", 1)
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(humanManagementService.getListGradeByPosition(1L)).thenReturn(LIST_GRADE_DTO);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<GradeDto> expected = LIST_GRADE_DTO;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<GradeDto> gradeDtoList = mapper.readerForListOf(GradeDto.class).readValue(actual);
    assertEquals(expected, gradeDtoList);
  }

  @Test
  void test_getListOffice() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_office")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(humanManagementService.getListOffice()).thenReturn(LIST_OFFICE_DTO);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<OfficeDto> expected = LIST_OFFICE_DTO;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<OfficeDto> officeDtoList = mapper.readerForListOf(OfficeDto.class).readValue(actual);
    assertEquals(expected, officeDtoList);
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void test_getListRoleType() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/list_role_type")
            .accept("*/*")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    when(humanManagementService.getListRoleType()).thenReturn(LIST_ROLE_DTO);
    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    List<RoleDto> expected = LIST_ROLE_DTO;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<RoleDto> roleDtoList = mapper.readerForListOf(RoleDto.class).readValue(actual);
    assertEquals(expected, roleDtoList);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListManagerHigherOfArea() throws Exception {
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListManagerHigherOfArea(employeeId))
        .thenReturn(NAME_AND_ID_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_manager_higher_of_area")
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

    List<EmployeeNameAndID> expected = NAME_AND_ID_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<EmployeeNameAndID> employeeNameAndIDList =
        mapper.readerForListOf(EmployeeNameAndID.class).readValue(actual);
    assertEquals(expected, employeeNameAndIDList);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListManagerHigherOfArea_Exception() throws Exception {
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListManagerHigherOfArea(employeeId))
        .thenReturn(NAME_AND_ID_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_manager_higher_of_area")
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
  void test_getListManagerLowerOfArea() throws Exception {
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListManagerLowerOfArea(employeeId)).thenReturn(NAME_AND_ID_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_manager_lower_of_area")
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

    List<EmployeeNameAndID> expected = NAME_AND_ID_LIST;
    ObjectMapper mapper = JacksonConfig.objectMapper();
    List<EmployeeNameAndID> employeeNameAndIDList =
        mapper.readerForListOf(EmployeeNameAndID.class).readValue(actual);
    assertEquals(expected, employeeNameAndIDList);
  }

  @Test
  @WithMockUser(
      value = "manager",
      roles = {"MANAGER"})
  void test_getListManagerLowerOfArea_Exception() throws Exception {
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("MANAGER")).thenReturn(true);
    when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListManagerLowerOfArea(employeeId)).thenReturn(NAME_AND_ID_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_manager_lower_of_area")
            .accept("*/*")
            .header(
                "header",
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8);
    assertThrows(NestedServletException.class, () -> mockMvc.perform(requestBuilder));
  }

  //  @Test
  //  void test_downloadCsvEmployee() throws Exception {
  //    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
  //    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
  //    PrintWriter printWriter = mock(PrintWriter.class);
  //    //    httpServletResponse.setContentType("text/csv; charset=UTF-8");
  //    //    httpServletResponse.addHeader(
  //    //        "Content-Disposition",
  //    //        "attachment; filename=\"employees_" + timestamp.getTime() + ".csv\"");
  //    List<String> list = LIST_EMPLOYEE_ID;
  //    ObjectMapper objectMapper = new ObjectMapper();
  //    objectMapper.registerModule(new JavaTimeModule());
  //    when(httpServletResponse.getWriter()).thenReturn(printWriter);
  //    doNothing().when(humanManagementService).exportEmployeeToCsv(printWriter, list);
  //    humanManagementService.exportEmployeeToCsv(printWriter, list);
  //
  //    RequestBuilder requestBuilder =
  //        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/download_csv_employee")
  //            .accept("*/*")
  //            .content(objectMapper.writeValueAsString(LIST_EMPLOYEE_ID))
  //            .contentType(MediaType.APPLICATION_JSON_VALUE)
  //            .characterEncoding(StandardCharsets.UTF_8);
  //    verify(humanManagementService, times(1)).exportEmployeeToCsv(printWriter, list);
  //    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  //  }
  //
  //  @Test
  //  void test_downloadExcelEmployee() throws Exception {
  //    ObjectMapper objectMapper = new ObjectMapper();
  //    objectMapper.registerModule(new JavaTimeModule());
  //
  //    RequestBuilder requestBuilder =
  //        MockMvcRequestBuilders.post(REQUEST_MAPPING + "/download_excel_employee")
  //            .accept("*/*")
  ////            .contentType("application/octet-stream")
  //            .content(objectMapper.writeValueAsString(LIST_EMPLOYEE_ID));
  //
  //    mockMvc.perform(requestBuilder).andExpect(status().isOk());
  //  }
}
