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
@Table(name = "employee_insurance")
public class EmployeeInsurance {
  @Id
  @Column(name = "employee_insurance_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "address")
  private String address;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "policy_name_id")
  private PolicyName policyName;

  @Column(name = "insurance_status")
  @Type(type = "boolean")
  private Boolean insurance_status;
}
