package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.EOffice;
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
@Table(name = "office")
public class Office {
  @Id
  @Column(name = "office_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private EOffice eOffice;

  @Column(name = "address")
  private String address;

  @OneToMany(mappedBy = "office", fetch = FetchType.LAZY)
  private List<WorkingPlace> workingPlace;
}
