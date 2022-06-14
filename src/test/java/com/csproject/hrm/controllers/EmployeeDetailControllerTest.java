package com.csproject.hrm.controllers;

import com.csproject.hrm.common.sample.DataSample;
import com.csproject.hrm.common.utils.DateUtils;
import com.csproject.hrm.dto.response.EmployeeDetailResponse;
import com.csproject.hrm.repositories.EmployeeDetailRepository;
import com.csproject.hrm.repositories.EmployeeRepository;
import com.csproject.hrm.repositories.custom.EmployeeRepositoryCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeDetailController.class)
public class EmployeeDetailControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	EmployeeDetailRepository detailRepository;
	
	@Test
	public void getAllRecords_success() throws Exception {
		List<EmployeeDetailResponse> records = new ArrayList<>(Arrays.asList(DataSample.DETAIL_RESPONSE));
		
		Mockito.when(detailRepository.findMainDetail("ANT1")).thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				                .get("/hrm/api/employee/detail/main")
				                .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$", hasSize(1)))
		       .andExpect((ResultMatcher) jsonPath("$[1].name", is("Nguyen Thi An")));
	}
}