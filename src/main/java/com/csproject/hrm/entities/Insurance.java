package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "insurance")
public class Insurance {
  @Id
  @Column(name = "insurance_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Column(name = "insurance_name")
  private String name;

  @Column(name = "address")
  private String address;

  @Column(name = "percent")
  private String percent;

  @Column(name = "description")
  private String description;
}