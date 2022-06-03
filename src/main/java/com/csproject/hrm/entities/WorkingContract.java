package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(name = "working_contract_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "company_name")
    private String companyName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private ContractType contractType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "base_salary")
    private String baseSalary;

    @Column(name = "contract_url")
    private String contractUrl;

    @OneToOne(mappedBy = "workingContract", fetch = FetchType.LAZY)
    private WorkingPlace workingPlace;

    @OneToMany(mappedBy = "workingContract", fetch = FetchType.LAZY)
    private List<WorkingInformation> workingInformation;
}
