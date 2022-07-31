package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "paid_leave_reason")
public class PaidLeaveReason {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reason_id")
  private Long id;

  @Column(name = "reason_name")
  private String reasonName;

  @OneToOne(mappedBy = "paidLeaveReason", fetch = FetchType.LAZY)
  private PaidLeave paidLeave;
}