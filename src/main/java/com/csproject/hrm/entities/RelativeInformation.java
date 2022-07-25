package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "relative_information")
public class RelativeInformation {
  @Id
  @Column(name = "relative_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "parent_name")
  private String parentName;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "relative_type")
  private RelativeType relativeType;
  
  @Column(name = "status")
  private String status;
  
  @Column(name = "contact")
  private String contact;

  @Column(name = "is_dependent")
  @Type(type = "boolean")
  private Boolean isDependentABoolean;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;
}