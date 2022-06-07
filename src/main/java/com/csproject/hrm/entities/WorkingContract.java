package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "working_contract")
public class WorkingContract {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "working_contract_id")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Column(name = "company_name")
  private String companyName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "contract_type")
  private ContractType contractType;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "base_salary")
  private BigDecimal baseSalary;

  @Column(name = "contract_url")
  private String contractUrl;

  @OneToMany(mappedBy = "workingContract", fetch = FetchType.LAZY)
  private List<WorkingInformation> workingInformation;

  @Column(name = "contract_status")
  @Type(type = "boolean")
  private Boolean contractStatus;

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
