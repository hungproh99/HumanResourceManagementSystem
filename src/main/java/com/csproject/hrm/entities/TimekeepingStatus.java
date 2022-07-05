package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ETimekeepingStatus;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "timekeeping_status")
public class TimekeepingStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private ETimekeepingStatus eTimekeepingStatus;

  @OneToOne(mappedBy = "timekeepingStatus", fetch = FetchType.LAZY)
  private ListTimekeepingStatus listTimekeepingStatus;
}