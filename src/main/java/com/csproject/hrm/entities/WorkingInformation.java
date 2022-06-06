package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "working_information")
public class WorkingInformation {
  @Id
  @Column(name = "working_information_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = WorkingContract.class)
  @JoinColumn(name = "working_contract_id")
  private WorkingContract workingContract;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToMany(mappedBy = "workingInformation", fetch = FetchType.LAZY)
  private List<Laudatory> laudatories;

  @OneToMany(mappedBy = "workingInformation", fetch = FetchType.LAZY)
  private List<Discipline> disciplines;

  @OneToMany(mappedBy = "workingInformation", fetch = FetchType.LAZY)
  private List<Salary> salaries;
}