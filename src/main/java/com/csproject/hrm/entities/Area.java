package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EArea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "area")
public class Area {
  @Id
  @Column(name = "area_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private EArea eArea;

  @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
  private List<WorkingPlace> workingPlace;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "manager_id")
  private Employee employee;
}
