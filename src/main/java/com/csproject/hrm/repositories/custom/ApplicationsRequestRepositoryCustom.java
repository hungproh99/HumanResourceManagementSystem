package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.response.ApplicationsRequestRespone;
import com.csproject.hrm.jooq.QueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationsRequestRepositoryCustom {
  List<ApplicationsRequestRespone> getListApplicationRequest(QueryParam queryParam);
}
