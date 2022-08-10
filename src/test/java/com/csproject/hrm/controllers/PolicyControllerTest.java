package com.csproject.hrm.controllers;

import com.csproject.hrm.common.sample.PolicyDataSample;
import com.csproject.hrm.config.TestSecurityConfig;
import com.csproject.hrm.dto.response.ListPolicyResponse;
import com.csproject.hrm.dto.response.PolicyCategoryResponse;
import com.csproject.hrm.jooq.Context;
import com.csproject.hrm.jooq.QueryParam;
import com.csproject.hrm.services.impl.PolicyServiceImpl;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.csproject.hrm.common.uri.Uri.REQUEST_MAPPING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PolicyController.class, TestSecurityConfig.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class PolicyControllerTest {
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired @MockBean private PolicyServiceImpl policyService;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(
      value = "admin",
      roles = {"ADMIN"})
  void testGetListPolicy_Admin_Normal() throws Exception {
    MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();
    Context context = new Context();
    QueryParam queryParam = context.queryParam(allRequestParams.toSingleValueMap());
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("ADMIN")).thenReturn(true);

    ListPolicyResponse response = PolicyDataSample.LIST_POLICY_RESPONSE;

    Mockito.when(policyService.getListPolicyByCategoryID(queryParam)).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_policy")
            .accept("*/*")
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
  void testGetListPolicyCategory_Admin_Normal() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.isUserInRole("ADMIN")).thenReturn(true);

    List<PolicyCategoryResponse> response = PolicyDataSample.POLICY_CATEGORY_RESPONSES;

    Mockito.when(policyService.getAllPolicyCategory()).thenReturn(response);

    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(REQUEST_MAPPING + "/get_list_category")
            .accept("*/*")
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