package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionSalaryRequest {
  @Positive(message = "Deduction Salary Id must be a positive number!")
  private Long deductionSalaryId;

  private BigDecimal value;

  private String description;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  @Positive(message = "Deduction Type Id must be a positive number!")
  private Long deductionTypeId;
}
