package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "deduction_salary")
public class DeductionSalary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "deduction_id")
  private Long id;

  @Column(name = "value")
  private BigDecimal value;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = DeductionType.class)
  @JoinColumn(name = "deduction_type_id")
  private DeductionType deductionType;

  @Column(name = "description")
  private String description;

  @Column(name = "date")
  private LocalDate date;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = SalaryMonthly.class)
  @JoinColumn(name = "salary_id")
  private SalaryMonthly salaryMonthly;
}
