package com.csproject.hrm.controllers;

import com.csproject.hrm.common.configs.JacksonConfig;
import com.csproject.hrm.common.sample.DataSample;
import com.csproject.hrm.common.uri.Uri;
import com.csproject.hrm.dto.request.*;
import com.csproject.hrm.dto.response.*;
import com.csproject.hrm.services.EmployeeDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.csproject.hrm.common.uri.Uri.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {EmployeeDetailController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class EmployeeDetailControllerTest {

  private static final String REQUEST_MAPPING = Uri.REQUEST_MAPPING + REQUEST_DETAIL_MAPPING;
  //  private static String jwtToken;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private EmployeeDetailService employeeDetailService;

  @Test
  @WithMockUser(roles = "ADMIN")
  void testFindMainDetail_Normal() throws Exception {
    EmployeeDetailResponse response = DataSample.DETAIL_RESPONSE;

    Mockito.when(employeeDetailService.findMainDetail(response.getEmployee_id()))
        .thenReturn(Optional.of(response));

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + URI_GET_MAIN_DETAIL + "?employeeID=huynq100")
            .accept("*/*")
            .characterEncoding(StandardCharsets.UTF_8);

    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    EmployeeDetailResponse expected = DataSample.DETAIL_RESPONSE;

    ObjectMapper mapper = JacksonConfig.objectMapper();
    EmployeeDetailResponse detailResponse = mapper.readValue(actual, EmployeeDetailResponse.class);

    assertEquals(expected, detailResponse);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testFindTaxAndInsurance_Normal() throws Exception {
    TaxAndInsuranceResponse response = DataSample.TAX_AND_INSURANCE_RESPONSE;

    Mockito.when(employeeDetailService.findTaxAndInsurance("huynq100"))
        .thenReturn(Optional.of(response));

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_TAX_AND_INSURANCE + "?employeeID=huynq100")
            .accept("*/*")
            .characterEncoding(StandardCharsets.UTF_8);

    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    TaxAndInsuranceResponse expected = DataSample.TAX_AND_INSURANCE_RESPONSE;

    ObjectMapper mapper = JacksonConfig.objectMapper();
    TaxAndInsuranceResponse detailResponse =
        mapper.readValue(actual, TaxAndInsuranceResponse.class);

    assertEquals(expected, detailResponse);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testFindAdditionalInfo_Normal() throws Exception {
    EmployeeAdditionalInfo response = DataSample.EMPLOYEE_ADDITIONAL_INFO;

    Mockito.when(employeeDetailService.findAdditionalInfo("huynq100"))
        .thenReturn(Optional.of(response));

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(
                REQUEST_MAPPING + URI_GET_ADDITIONAL_INFO + "?employeeID=huynq100")
            .accept("*/*")
            .characterEncoding(StandardCharsets.UTF_8);

    String actual =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    EmployeeAdditionalInfo expected = DataSample.EMPLOYEE_ADDITIONAL_INFO;

    ObjectMapper mapper = JacksonConfig.objectMapper();
    EmployeeAdditionalInfo detailResponse = mapper.readValue(actual, EmployeeAdditionalInfo.class);

    assertEquals(expected, detailResponse);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateEmployeeDetail_Normal() throws Exception {
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

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    MockHttpServletResponse response = result.getResponse();

    assertThat(response.getContentAsString(), containsString("This request is successful"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateAdditionalInfo_Normal() throws Exception {
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

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    MockHttpServletResponse response = result.getResponse();

    assertThat(response.getContentAsString(), containsString("This request is successful"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateBank_Normal() throws Exception {
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

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    MockHttpServletResponse response = result.getResponse();

    assertThat(response.getContentAsString(), containsString("This request is successful"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateEducation_Normal() throws Exception {
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

    MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

    MockHttpServletResponse response = result.getResponse();

    assertThat(response.getContentAsString(), containsString("This request is successful"));
  }
}