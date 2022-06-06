package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "contract_type")
public class ContractType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToOne(mappedBy = "contractType", fetch = FetchType.LAZY)
  private WorkingHistory workingHistory;

  @OneToOne(mappedBy = "contractType", fetch = FetchType.LAZY)
  private WorkingContract workingContract;
}