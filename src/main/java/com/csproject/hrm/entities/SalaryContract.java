package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "salary_contract")
public class SalaryContract {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "salary_contract_id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = WorkingContract.class)
  @JoinColumn(name = "working_contract_id")
  private WorkingContract workingContract;

  @Column(name = "salary_contract_status")
  @Type(type = "boolean")
  private Boolean salaryStatus;

  @Column(name = "base_salary")
  private BigDecimal baseSalary;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToMany(mappedBy = "salaryContract", fetch = FetchType.LAZY)
  private List<SalaryMonthly> salaryMonthlies;
}