package com.csproject.hrm.entities;

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
@Table(name = "discipline")
public class Discipline {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "discipline_id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = WorkingInformation.class)
  @JoinColumn(name = "working_information_id")
  private WorkingInformation workingInformation;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "date")
  private String date;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "discipline_type")
  private DisciplineType disciplineType;

  @Column(name = "status")
  private String status;

  @Column(name = "punishment")
  private String punishment;
}
