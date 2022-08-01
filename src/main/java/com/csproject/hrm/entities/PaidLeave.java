package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "paid_leave")
public class PaidLeave {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "paid_leave_reason_id")
  private PaidLeaveReason paidLeaveReason;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "timekeeping_id")
  private Timekeeping timekeeping;
}