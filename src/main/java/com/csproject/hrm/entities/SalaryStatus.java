package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ERequestStatus;
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
@Table(name = "salary_status")
public class SalaryStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "status_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private ERequestStatus eRequestStatus;

  @OneToOne(mappedBy = "salaryStatus", fetch = FetchType.LAZY)
  private SalaryMonthly salaryMonthly;
}
