package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bonus_salary")
public class BonusSalary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bonus_id")
  private Long id;

  @Column(name = "value")
  private BigDecimal value;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = BonusType.class)
  @JoinColumn(name = "bonus_type_id")
  private BonusType bonusType;

  @Column(name = "description")
  private String description;

  @Column(name = "date")
  private LocalDate date;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = SalaryMonthly.class)
  @JoinColumn(name = "salary_id")
  private SalaryMonthly salaryMonthly;
}