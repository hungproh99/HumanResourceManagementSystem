package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "working_history")
public class WorkingHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "working_history_id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Column(name = "company_name")
  private String companyName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "type_id")
  private ContractType contractType;

  @Column(name = "position")
  private String position;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;
}