package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "working_place")
public class WorkingPlace {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "working_place_id")
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "working_contract_id")
  private WorkingContract workingContract;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "area_id")
  private Area area;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "office_id")
  private Office office;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "job_id")
  private Job job;
}