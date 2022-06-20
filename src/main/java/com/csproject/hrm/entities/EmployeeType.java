package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EEmployeeType;
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
@Table(name = "employee_type")
public class EmployeeType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private EEmployeeType eEmployeeType;

  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "employeeType", fetch = FetchType.LAZY)
  private Employee employee;
}
