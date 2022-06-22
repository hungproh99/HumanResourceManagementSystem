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
@Table(name = "timekeeping")
public class Timekeeping {
  @Id
  @Column(name = "timekeeping_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "timekeeping", fetch = FetchType.LAZY)
  private List<CheckInCheckOut> checkInCheckOut;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Column(name = "date")
  private LocalDate date;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "timekeeping_status_id")
  private TimekeepingStatus timekeepingStatus;

  @Column(name = "amount_work_per_day")
  private String amountWorkPerDay;
}