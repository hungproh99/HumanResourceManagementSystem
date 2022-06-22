package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepositoryCustom {
  List<OfficeDto> getListOffice();

  List<AreaDto> getListArea();

  List<JobDto> getListPosition();

  List<GradeDto> getListGradeByPosition(Long jodId);
}