package com.csproject.hrm.controllers;

import com.csproject.hrm.common.enums.ERole;
import com.csproject.hrm.dto.request.LoginRequest;
import com.csproject.hrm.dto.response.JwtResponse;
import com.csproject.hrm.entities.Employee;
import com.csproject.hrm.entities.EmployeeType;
import com.csproject.hrm.entities.RoleType;
import com.csproject.hrm.entities.WorkingType;
import com.csproject.hrm.jwt.JwtUtils;
import com.csproject.hrm.jwt.UserDetailsImpl;
import com.csproject.hrm.services.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = LoginController.class)
@WebAppConfiguration
public class LoginControllerTest {
  @Autowired ObjectMapper mapper;
  @Autowired JwtUtils jwtUtils;
  @MockBean LoginService loginService;

  EmployeeType employeeTypeRecord1 =
      EmployeeType.builder().id(1L).name("Trainee").description("Trainee").build();
  EmployeeType employeeTypeRecord2 =
      EmployeeType.builder().id(2L).name("Official Employee").build();

  RoleType roleTypeRecord1 = RoleType.builder().id(1L).ERole(ERole.ROLE_ADMIN).build();
  RoleType roleTypeRecord2 = RoleType.builder().id(2L).ERole(ERole.ROLE_MANAGER).build();

  WorkingType workingTypeRecord1 = WorkingType.builder().id(1L).name("Full Time").build();
  WorkingType workingTypeRecord2 = WorkingType.builder().id(1L).name("Full Time").build();

  Employee record1 =
      Employee.builder()
          .id("huynq100")
          .birthDate(LocalDate.parse("2000-12-08", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
          .companyEmail("huynq100@fpt.edu.vn")
          .fullName("Nguyen Quang Huy")
          .gender("Male")
          .password("$10$T7GKHVFE0rxQRQwjQ3FFCewtjqoonAdBaeIZf8oa6Uoi5BP4hwGuy")
          .phoneNumber("0912345678")
          .workingStatus(Boolean.TRUE)
          .employeeType(employeeTypeRecord1)
          .roleType(roleTypeRecord1)
          .workingType(workingTypeRecord1)
          .build();

  Employee record2 =
      Employee.builder()
          .id("hungnq100")
          .birthDate(LocalDate.parse("2000-12-08", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
          .companyEmail("hungnq100@fpt.edu.vn")
          .fullName("Nguyen Quang Hung")
          .gender("Male")
          .password("$10$T7GKHVFE0rxQRQwjQ3FFCewtjqoonAdBaeIZf8oa6Uoi5BP4hwGuy")
          .phoneNumber("0912345678")
          .workingStatus(Boolean.TRUE)
          .employeeType(employeeTypeRecord2)
          .roleType(roleTypeRecord2)
          .workingType(workingTypeRecord2)
          .build();

  LoginRequest loginRequestRecord1 =
      LoginRequest.builder()
          .email("hungnq100")
          .password("$10$T7GKHVFE0rxQRQwjQ3FFCewtjqoonAdBaeIZf8oa6Uoi5BP4hwGuy")
          .build();

  @Test
  public void login_success() throws Exception {
    Authentication authentication = loginService.getAuthentication(loginRequestRecord1);
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(loginRequestRecord1));

    //    mockMvc.perform(mockRequest)
    //            .andExpect(status().isOk())
    //            .andExpect(jsonPath("$", notNullValue()))
    //            .andExpect(jsonPath("$.name", is("Rayven Zambo")));

    JwtResponse jwtResponse =
        new JwtResponse(userDetails.getId(), userDetails.getEmail(), roles, jwt);
  }
}