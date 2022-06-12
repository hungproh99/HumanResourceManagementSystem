package com.csproject.hrm.dto.response;

import com.csproject.hrm.entities.ContractType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHistoryResponse {
  private Long working_history_id;
  private String company_name;
  private Long type_id;
  private String position;
  private LocalDate start_date;
  private LocalDate end_date;
}