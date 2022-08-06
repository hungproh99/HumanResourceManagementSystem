package com.csproject.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionSalaryRequest {
  @Positive(message = "deductionSalaryId must be a positive number!")
  private Long deductionSalaryId;

  @Positive(message = "value must be a positive number!")
  private BigDecimal value;

  private String description;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  @Positive(message = "deductionTypeId must be a positive number!")
  private Long deductionTypeId;
}
