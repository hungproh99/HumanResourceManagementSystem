package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "laudatory_type")
public class LaudatoryType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToOne(mappedBy = "laudatoryType", fetch = FetchType.LAZY)
  private Laudatory laudatory;
}