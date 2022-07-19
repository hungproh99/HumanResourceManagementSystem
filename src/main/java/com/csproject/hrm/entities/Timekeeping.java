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

  @OneToMany(mappedBy = "timekeeping", fetch = FetchType.LAZY)
  private List<ListTimekeepingStatus> listTimekeepingStatuses;

  @Column(name = "point_ot_day")
  private Double pointOTDay;

  @Column(name = "point_work_day")
  private Double pointWorkDay;

  @OneToOne(mappedBy = "timekeeping", fetch = FetchType.LAZY)
  private Overtime overTime;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "paid_leave_reason_id")
  private PaidLeaveReason paidLeaveReason;
}