package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private ERole ERole;

  @OneToOne(mappedBy = "roleType", fetch = FetchType.LAZY)
  private Employee employee;
}
