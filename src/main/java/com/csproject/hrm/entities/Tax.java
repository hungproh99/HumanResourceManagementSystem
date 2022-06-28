package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ETax;
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
@Table(name = "tax")
public class Tax {
  @Id
  @Column(name = "tax_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "tax_name")
  private ETax tax;

  @Column(name = "percent")
  private String percent;

  @Column(name = "description")
  private String description;
}