package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.AreaDto;
import com.csproject.hrm.dto.dto.JobDto;
import com.csproject.hrm.dto.dto.OfficeDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepositoryCustom {
  List<OfficeDto> getListOffice();

  List<AreaDto> getListArea();

  List<JobDto> getListJob();
}
