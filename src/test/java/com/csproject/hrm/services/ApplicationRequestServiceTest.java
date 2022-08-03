package com.csproject.hrm.services;

import com.csproject.hrm.common.sample.DataSample;
import com.csproject.hrm.dto.request.ApplicationsRequestRequestC;
import com.csproject.hrm.exception.CustomDataNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.csproject.hrm.common.constant.Constants.NO_EMPLOYEE_WITH_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {ApplicationsRequestService.class})
public class ApplicationRequestServiceTest {
  @Autowired ApplicationsRequestService applicationsRequestService;

  @Test
  void testCreateTimekeepingRequest_Normal() {
    ApplicationsRequestRequestC record = DataSample.APPLICATIONS_REQUEST_TIMEKEEPING;
    record.setEmployeeId("");
    Throwable exception =
        assertThrows(
            CustomDataNotFoundException.class,
            () -> applicationsRequestService.createApplicationsRequest(record));
    assertEquals(NO_EMPLOYEE_WITH_ID, exception.getMessage());
  }
}