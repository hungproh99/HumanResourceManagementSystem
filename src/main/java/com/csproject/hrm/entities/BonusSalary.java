package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

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

  @Column(name = "description")
  private String description;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Salary.class)
  @JoinColumn(name = "salary_id")
  private Salary salary;
}
