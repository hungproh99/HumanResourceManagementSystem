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
@Table(name = "list_timekeeping_status")
public class ListTimekeepingStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "list_id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Timekeeping.class)
  @JoinColumn(name = "timekeeping_id")
  private Timekeeping timekeeping;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "timekeeping_status_id")
  private TimekeepingStatus timekeepingStatus;
}
