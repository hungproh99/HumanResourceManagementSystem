package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EInsurance;
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
@Table(name = "insurance")
public class Insurance {
  @Id
  @Column(name = "insurance_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "insurance_name")
  private EInsurance insurance;

  @Column(name = "address")
  private String address;

  @Column(name = "percent")
  private String percent;

  @Column(name = "description")
  private String description;
}