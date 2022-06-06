package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "discipline_type")
public class DisciplineType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToOne(mappedBy = "disciplineType", fetch = FetchType.LAZY)
  private Discipline discipline;
}