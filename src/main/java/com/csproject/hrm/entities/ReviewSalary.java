package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "review_salary")
public class ReviewSalary {
  @Id
  @Column(name = "review_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "employee_id")
  private String employeeId;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = SalaryMonthly.class)
  @JoinColumn(name = "salary_id")
  private SalaryMonthly salaryMonthly;
}
