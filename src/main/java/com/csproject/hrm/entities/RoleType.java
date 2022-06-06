package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role_type")
public class RoleType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "role")
  private com.csproject.hrm.common.enums.ERole ERole;

  @OneToOne(mappedBy = "roleType", fetch = FetchType.LAZY)
  private Employee employee;
}