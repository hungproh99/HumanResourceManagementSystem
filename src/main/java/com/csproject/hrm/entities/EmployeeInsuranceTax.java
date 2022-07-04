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
@Table(name = "employee_insurance_tax")
public class EmployeeInsuranceTax {
  @Id
  @Column(name = "employee_insurance_tax_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "address")
  private String address;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "policy_type_id")
  private PolicyType policyType;

  @Column(name = "status")
  @Type(type = "boolean")
  private Boolean status;
}
