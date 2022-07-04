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
@Table(name = "working_place")
public class WorkingPlace {
  @Id
  @Column(name = "working_place_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Area.class)
  @JoinColumn(name = "area_id")
  private Area area;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Office.class)
  @JoinColumn(name = "office_id")
  private Office office;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Job.class)
  @JoinColumn(name = "job_id")
  private Job job;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = GradeType.class)
  @JoinColumn(name = "grade_id")
  private GradeType grade;

  @Column(name = "working_place_status")
  @Type(type = "boolean")
  private Boolean workingPlaceStatus;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = WorkingContract.class)
  @JoinColumn(name = "working_contract_id")
  private WorkingContract workingContract;
}
