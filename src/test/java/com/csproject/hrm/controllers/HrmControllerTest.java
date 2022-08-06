package com.csproject.hrm.controllers;

import com.csproject.hrm.common.configs.JacksonConfig;
import com.csproject.hrm.dto.response.HrmResponseList;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.services.HumanManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

import static com.csproject.hrm.common.sample.DataSample.HRM_RESPONSE_LIST;
import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {HrmController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class HrmControllerTest {
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private JwtUtils jwtUtils;
  @Autowired @MockBean private HumanManagementService humanManagementService;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void test_getAllEmployee_Admin() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    when(humanManagementService.getListHumanResource(queryParam)).thenReturn(HRM_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_employee")
            .accept("*/*")
            .with(user("huynq100").roles("ADMIN"))
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
  void test_getAllEmployee_Manager() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    String headerAuth =
        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String jwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodXlucTEwMCIsIlVzZXJfRGF0YSI6eyJpZCI6Imh1eW5xMTAwIiwiZW1haWwiOiJodXlucTEwMEBmcHQuZWR1LnZuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfTUFOQUdFUiJ9XSwiZnVsbE5hbWUiOiJOZ3V5ZW4gUXVhbmcgSHV5IiwiZW5hYmxlZCI6dHJ1ZSwidXNlcm5hbWUiOm51bGwsImFjY291bnROb25Mb2NrZWQiOnRydWUsImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiYWNjb3VudE5vbkV4cGlyZWQiOnRydWV9LCJpYXQiOjE2NTk3MjkwMzEsImV4cCI6MTY1OTgxNTQzMX0.7rkpFrZckf8K6pTSayMMDkNqht-9FOQRutILkVl9AkRVhHSZCTtIiDK5eWlXq2s3Jn1vYX5zihoyomC31y7nyQ";
    String employeeId = "huynq100";
    HttpServletRequest request = mock(HttpServletRequest.class);
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    //    Mockito.when(request.isUserInRole("MANAGER")).thenReturn(Boolean.TRUE);
    //    Mockito.when(request.getHeader(AUTHORIZATION)).thenReturn(headerAuth);
    //    Mockito.when(jwtUtils.getIdFromJwtToken(jwt)).thenReturn(employeeId);
    when(humanManagementService.getListHumanResource(queryParam)).thenReturn(HRM_RESPONSE_LIST);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_all_employee")
            .accept("*/*")
            .with(user("user").roles("MANAGER>"))
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
}
