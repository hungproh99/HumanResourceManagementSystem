package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "leave_company_reason")
public class LeaveCompanyReason {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reason_id")
  private Long id;

  @Column(name = "reason_name")
  private String reasonName;

  @OneToOne(mappedBy = "leaveCompanyReason", fetch = FetchType.LAZY)
  private WorkingContract workingContract;
}