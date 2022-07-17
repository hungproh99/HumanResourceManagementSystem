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
@Table(name = "salary_monthly")
public class SalaryMonthly {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "salary_id")
  private Long id;

  @Column(name = "final_salary")
  private BigDecimal finalSalary;

  @Column(name = "standard_point")
  private Double standardPoint;

  @Column(name = "actual_point")
  private Double actualPoint;

  @Column(name = "ot_point")
  private Double otPoint;

  @Column(name = "total_deduction")
  private BigDecimal totalDeduction;

  @Column(name = "total_bonus")
  private BigDecimal totalBonus;

  @Column(name = "total_insurance_payment")
  private BigDecimal totalInsurancePayment;

  @Column(name = "total_tax_payment")
  private BigDecimal totalTaxPayment;

  @Column(name = "total_advance")
  private BigDecimal totalAdvance;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = SalaryContract.class)
  @JoinColumn(name = "salary_contract_id")
  private SalaryContract salaryContract;

  @OneToMany(mappedBy = "salaryMonthly", fetch = FetchType.LAZY)
  private List<BonusSalary> bonusSalaries;

  @OneToMany(mappedBy = "salaryMonthly", fetch = FetchType.LAZY)
  private List<DeductionSalary> deductionSalaries;

  @OneToMany(mappedBy = "salaryMonthly", fetch = FetchType.LAZY)
  private List<AdvancesSalary> advancesSalaries;
}
