package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "leave_company")
public class LeaveCompany {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "leave_company_reason_id")
  private LeaveCompanyReason leaveCompanyReason;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "working_contract_id")
  private WorkingContract workingContract;
}