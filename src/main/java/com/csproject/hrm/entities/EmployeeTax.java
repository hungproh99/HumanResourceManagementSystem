package com.csproject.hrm.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee_tax")
public class EmployeeTax {
  @Id
  @Column(name = "employee_tax_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "tax_code")
  private String taxCode;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "policy_name_id")
  private PolicyName policyName;

  @Column(name = "tax_status")
  @Type(type = "boolean")
  private Boolean tax_status;
}