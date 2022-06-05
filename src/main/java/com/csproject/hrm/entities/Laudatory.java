package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "laudatory")
public class Laudatory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "laudatory_id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = WorkingInformation.class)
  @JoinColumn(name = "working_information_id")
  private WorkingInformation workingInformation;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "date")
  private LocalDate date;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "type_id")
  private LaudatoryType laudatoryType;

  @Column(name = "status")
  private String status;

  @Column(name = "reward")
  private String reward;
}
