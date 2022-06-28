package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "salary")
public class Salary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "salary_id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = WorkingContract.class)
  @JoinColumn(name = "working_contract_id")
  private WorkingContract workingContract;

  @Column(name = "base_salary")
  private BigDecimal baseSalary;

  @Column(name = "final_salary")
  private BigDecimal finalSalary;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToMany(mappedBy = "salary", fetch = FetchType.LAZY)
  private List<BonusSalary> bonusSalaries;
}
