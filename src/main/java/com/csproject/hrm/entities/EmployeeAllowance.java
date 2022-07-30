package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee_allowance")
public class EmployeeAllowance {
  @Id
  @Column(name = "employee_allowance_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "policy_name_id")
  private PolicyName policyName;

  @Column(name = "allowance_status")
  @Type(type = "boolean")
  private Boolean allowance_status;
}
